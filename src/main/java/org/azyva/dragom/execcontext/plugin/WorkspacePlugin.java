/*
 * Copyright 2015 - 2017 AZYVA INC. INC.
 *
 * This file is part of Dragom.
 *
 * Dragom is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Dragom is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Dragom.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.azyva.dragom.execcontext.plugin;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Set;

import org.azyva.dragom.model.Module;
import org.azyva.dragom.model.ModuleVersion;
import org.azyva.dragom.model.Version;

/**
 * TODO: The workspace implementation can and should know about the various types of
 * directories (the various WorkspaceDir and SystemWorkspaceDir classes) and act accordingly.
 * If it does not know about a specific class, maybe a default generic implementation is feasible.
 * Maybe the workspace dir class could help in implementing such a defaul behavior (random vs fixed directory for instance).
 *
 * @author David Raymond
 */

public interface WorkspacePlugin extends ExecContextPlugin {
  /**
   * Enumerates the modes when obtaining a {@link WorkspaceDir}.
   */
  public enum GetWorkspaceDirMode {
    /**
     * The workspace directory must not exist. Mutually exclusive with MUST_NOT_EXIST,
     * but none can be specified if existence is not important.
     */
    MUST_EXIST,

    /**
     * The workspace directory must already exist. Mutually exclusive with MUST_EXIT,
     * but none can be specified if existence is not important.
     */
    MUST_NOT_EXIST,

    /**
     * Create the workspace directory if it does not exist. Otherwise do not attempt
     * to create the workspace directory. Can be combined with MUST_NOT_EXIST to force
     * the creation of a new workspace directory. Combining with MUST_EXIST is not
     * useful but works as expected.
     */
    CREATE_IF_NOT_EXIST,

    /**
     * Can be specified with CREATE_IF_NOT_EXIST. When specified the behavior is
     * similar to CREATE_IF_NOT_EXIST, but the actual path is not created if it does
     * not exist. This is to support cases where if the workspace directory is new,
     * the path itself must be created by the caller as opposed to starting with an
     * newly created empty path. For example cloning a Git repository requires that
     * the path does not exist. In such as case, teh caller must create it immediately
     * or delete the workspace directory using the deleteWorkspaceDir method.
     *
     * If the caller specifies DO_NOT_CREATE_PATH and CREATE_IF_NOT_EXIST only it can
     * easily detect if the path is new or already exists simply by the existence of
     * the path itself. But if DO_NOT_CREATE_PATH is not specified , the path will
     * always exist upon exit of the method and it may be harder for the caller to
     * distinguish between new and existing paths.
     *
     * Cannot be specified if CREATE_IF_NOT_EXIST is not specified since if the
     * workspace directory exists, the corresponding path must exist (since if ever
     * the caller specifies this flag it must respect the contract to immediately
     * create the path).
     */
    DO_NOT_CREATE_PATH,

    /**
     * Resets the workspace directory if it exists, as if it was created new (empties
     * it). Otherwise, if the workspace directory exists, reuse it as is. Can be
     * combined with MUST_EXIST to force the reset of the workspace directory.
     * Combining with MUST_NOT_EXIST is not useful but works as expected.
     */
    RESET_IF_EXIST;

    /**
     * EnumSet of GetWorkspaceDirMode which means to get an existing
     * {@link WorkspaceDir} and fail if it does not exist.
     */
    public static final EnumSet<GetWorkspaceDirMode> ENUM_SET_GET_EXISTING = EnumSet.noneOf(GetWorkspaceDirMode.class);

    /**
     * EnumSet of GetWorkspaceDirMode which means to get an existing
     * {@link WorkspaceDir} or create a new one if it does not exist.
     */
    public static final EnumSet<GetWorkspaceDirMode> ENUM_SET_GET_EXISTING_OR_CREATE = EnumSet.of(CREATE_IF_NOT_EXIST);

    /**
     * EnumSet of GetWorkspaceDirMode which means to get an existing
     * {@link WorkspaceDir} or create a new one if it does not exist. If the path does
     * not exist, it is not created. Useful for {@link WorkspaceDirUserModuleVersion}
     * where the path is expected to be created when performing the checkout from the
     * SCM.
     */
    public static final EnumSet<GetWorkspaceDirMode> ENUM_SET_GET_EXISTING_OR_CREATE_NO_PATH = EnumSet.of(CREATE_IF_NOT_EXIST, DO_NOT_CREATE_PATH);

    /**
     * EnumSet of GetWorkspaceDirMode which means to create a new {@link WorkspaceDir}
     * and fail if it already exists.
     */
    public static final EnumSet<GetWorkspaceDirMode> ENUM_SET_CREATE_NEW = EnumSet.of(MUST_NOT_EXIST, CREATE_IF_NOT_EXIST);

    /**
     * EnumSet of GetWorkspaceDirMode which means to create a new {@link WorkspaceDir}
     * and fail if it already exists. The path is not created. Useful for
     * {@link WorkspaceDirUserModuleVersion} where the path is expected to be created
     * when performing the checkout from the SCM.
     */
    public static final EnumSet<GetWorkspaceDirMode> ENUM_SET_CREATE_NEW_NO_PATH = EnumSet.of(MUST_NOT_EXIST, CREATE_IF_NOT_EXIST, DO_NOT_CREATE_PATH);
  }

  /**
   * Enumerates the possible Workspace directory access modes for the
   * getWorkspaceDir method.
   *
   * @author David Raymond
   *
   */
  public enum WorkspaceDirAccessMode {
    /**
     * Indicates the caller simply wants to peek at the Workspace directory. No
     * reservation is actually performed, and the Workspace directory will be
     * returned even if it is being accessed for writing (READ_WRITE). The caller
     * is advised to not perform extensive operations on the Workspace directory that
     * could invalidate it.
     */
    PEEK,

    /**
     * Indicates the caller will read from the Workspace directory and does not want
     * it to be modified until it is released.
     */
    READ,

    /**
     * Indicates the caller will modify the Workspace directory.
     */
    READ_WRITE;
  }

  /**
   * @return Path to the workspace.
   */
  Path getPathWorkspace();

  /**
   * @return Indicates if multiple {@link Version}'s of the same {@link Module} can
   *   exist in user workspace directories.
   */
  // TODO: What about multiple different modules mapped to the same path (same module name, but different classification)
  // For now lets say that we cannot inquire about this. And that the below applies to both user and system.
  boolean isSupportMultipleModuleVersion();

  /**
   * Verifies if a {@link WorkspaceDir} exists.
   *
   * <p>The path to the WorkspaceDir may or may not exist. Only the knowledge of the
   * WorkspaceDir within the workspace is verified.
   *
   * @param workspaceDir WorkspaceDir.
   * @return See description.
   */
  boolean isWorkspaceDirExist(WorkspaceDir workspaceDir);

  /**
   * Returns a conflicting {@link WorkspaceDir} with a given WorkspaceDir or null if
   * none.
   *
   * <p>A conflicting WorkspaceDir is one which maps to the same path.
   *
   * <p>If {@link #isSupportMultipleModuleVersion} and the specified WorkspaceDir is
   * for the same {@link Module} but a different {@link Version}, null should be
   * returned.
   *
   * <p>If it is for a different Module which would map to the same path as an
   * existing WorkspaceDir, that WorkspaceDir should be returned.
   *
   * @param workspaceDir WorkspaceDir.
   * @return See description.
   */
  WorkspaceDir getWorkspaceDirConflict(WorkspaceDir workspaceDir);

  //TODO if workspace dir cannot be created for whatever reason, exception, even if it is because of conflit.
  //Fow now. Eventually, maybe the caller would be interested in knowing if fail because of conflict behave
  //gracefully in that case. But for now, make it simple.
  //TODO: For temporay random system directories, the workspace would not consider the mode,
  // or would validate the mode to have a certain logical value given that a new random directory is always create
  //TODO: Document access mode, and the fact that in theory should never get access conflit because of the lack of
  //circular dependencies. But this is a failfast mechanism just in case.
  // Still, multiple readers are allowed.
  /**
   * Returns the Path corresponding to the {@link WorkspaceDir}.
   *
   * <p>Null cannot be returned. An exception is raised if thw WorkspaceDir does not
   * exist or cannot be created.
   *
   * <p>The WorkspaceDir must be released with {@link #releaseWorkspaceDir} unless
   * {@link WorkspaceDirAccessMode#PEEK} was specified.
   *
   * @param workspaceDir WorkspaceDir.
   * @param enumSetGetWorkspaceDirMode EnumSet of GetWorkspaceDirMode.
   * @param workspaceDirAccessMode WorkspaceDirAccessMode.
   * @return See description.
   */
  Path getWorkspaceDir(WorkspaceDir workspaceDir, EnumSet<GetWorkspaceDirMode> enumSetGetWorkspaceDirMode, WorkspaceDirAccessMode workspaceDirAccessMode);

  /**
   * Releases a {@link WorkspaceDir} given it corresponding Path.
   *
   * <p>The WorkspaceDir must have been obtained with {@link #getWorkspaceDir}.
   *
   * @param pathWorkspaceDir Path to the WorkspaceDir.
   */
  void releaseWorkspaceDir(Path pathWorkspaceDir);

  /**
   * Gets the current {@link WorkspaceDirAccessMode} for a {@link WorkspaceDir}
   * given its Path.
   *
   * @param pathWorkspaceDir Path to the WorkspaceDir.
   * @return WorkspaceDirAccessMode.
   */
  WorkspaceDirAccessMode getWorkspaceDirAccessMode(Path pathWorkspaceDir);

  /**
   * Updates a {@link WorkspaceDir}.
   *
   * <p>To be used when the {@link ModuleVersion} within the path corresponding to
   * a WorkspaceDir is changed.
   *
   * <p>The WorkspaceDir must be accessed for
   * {@link WorkspaceDirAccessMode#READ_WRITE}.
   *
   * @param workspaceDir Current WorkspaceDir.
   * @param workspaceDirNew New WorkspaceDir.
   */
  void updateWorkspaceDir(WorkspaceDir workspaceDir, WorkspaceDir workspaceDirNew);

  /**
   * Deletes a {@link WorkspaceDir} and its corresponding path, if it exists.
   *
   * <p>The WorkspaceDir must be accessed for
   * {@link WorkspaceDirAccessMode#READ_WRITE}.
   *
   * @param workspaceDir WorkspaceDir.
   */
  void deleteWorkspaceDir(WorkspaceDir workspaceDir);

  /**
   * Returns the Set of all {@link WorkspaceDir}'s of a given WorkspaceDir
   * subclass.
   *
   * @param workspaceDirClass WorkspaceDir subclass. Can be null to return all
   *   WorkspaceDir.
   * @return See description.
   */
  Set<WorkspaceDir> getSetWorkspaceDir(Class<? extends WorkspaceDir> workspaceDirClass);

  // workspaceDirClass can be null to list all workspace directories.
  // workspaceDir can be such that multiple match exist. They are all returned.
  // Useful for WorkspaceDirUserModuleVersion where version is not set.
  /**
   * Returns the Set of all {@link WorkspaceDir}'s matching some potentially
   * incomplete WorkspaceDir definition.
   *
   * <p>An example of an incomplete WorkspaceDir definition is a
   * {@link WorkspaceDirUserModuleVersion} with a null {@link Version}.
   *
   * @param workspaceDirIncomplete Incomplete WorkspaceDir.
   * @return See description.
   */
  Set<WorkspaceDir> getSetWorkspaceDir(WorkspaceDir workspaceDirIncomplete);

  /**
   * Verifies if a {@link WorkspaceDir} exists given its Path.
   *
   * @param pathWorkspaceDir Path to the WorkspaceDir.
   * @return See description.
   */
  boolean isPathWorkspaceDirExists(Path pathWorkspaceDir);

  /**
   * Returns a {@link WorkspaceDir} given its Path.
   *
   * <p>Useful for a method that has a hold on the path of a WorkspaceDir and needs
   * to know the corresponding WorkspaceDir.
   *
   * @param pathWorkspaceDir Path to the WorkspaceDir.
   * @return See description.
   */
  WorkspaceDir getWorkspaceDirFromPath(Path pathWorkspaceDir);
}
