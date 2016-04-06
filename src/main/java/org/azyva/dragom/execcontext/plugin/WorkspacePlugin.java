/*
 * Copyright 2015 AZYVA INC.
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

/**
 * TODO: The workspace implementation can and should know about the various types of
 * directories (the various WorkspaceDir and SystemWorkspaceDir classes) and act accordingly.
 * If it does not know about a specific class, maybe a default generic implementation is feasible.
 * Maybe the workspace dir class could help in implementing such a defaul behavior (random vs fixed directory for instance).
 *
 * @author David Raymond
 */

public interface WorkspacePlugin extends ExecContextPlugin {
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

		public static final EnumSet<GetWorkspaceDirMode> GET_EXISTING = EnumSet.noneOf(GetWorkspaceDirMode.class);
		public static final EnumSet<GetWorkspaceDirMode> GET_EXISTING_OR_CREATE = EnumSet.of(CREATE_IF_NOT_EXIST);
		public static final EnumSet<GetWorkspaceDirMode> GET_EXISTING_OR_CREATE_NO_PATH = EnumSet.of(CREATE_IF_NOT_EXIST, DO_NOT_CREATE_PATH);
		public static final EnumSet<GetWorkspaceDirMode> CREATE_NEW = EnumSet.of(MUST_NOT_EXIST, CREATE_IF_NOT_EXIST);
		public static final EnumSet<GetWorkspaceDirMode> CREATE_NEW_NO_PATH = EnumSet.of(MUST_NOT_EXIST, CREATE_IF_NOT_EXIST, DO_NOT_CREATE_PATH);
	};

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

	Path getPathWorkspace();

	boolean isWorkspaceDirExist(WorkspaceDir workspaceDir);

	//TODO if workspace dir cannot be created for whatever reason, exception, even if it is because of conflit.
	//Fow now. Eventually, maybe the caller would be interested in knowing if fail because of conflict behave
	//gracefully in that case. But for now, make it simple.
	//TODO: For temporay random system directories, the workspace would not consider the mode,
	// or would validate the mode to have a certain logical value given that a new random directory is always create
	//TODO: Document access mode, and the fact that in theory should never get access conflit because of the lack of
	//circular dependencies. But this is a failfast mechanism just in case.
	// Still, multiple readers are allowed.
	Path getWorkspaceDir(WorkspaceDir workspaceDir, EnumSet<GetWorkspaceDirMode> enumSetGetWorkspaceDirMode, WorkspaceDirAccessMode workspaceDirAccessMode);

	void releaseWorkspaceDir(Path pathWorkspaceDir);

	void updateWorkspaceDir(WorkspaceDir workspaceDir, WorkspaceDir workspaceDirNew);

	// If path exist, must delete it.
	void deleteWorkspaceDir(WorkspaceDir workspaceDir);

	// workspaceDirClass can be null to list all workspace directories.
	Set<WorkspaceDir> getSetWorkspaceDir(Class<? extends WorkspaceDir> workspaceDirClass);

	// workspaceDirClass can be null to list all workspace directories.
	// workspaceDir can be such that multiple match exist. They are all returned.
	// Useful for WorkspaceDirUserModuleVersion where version is not set.
	Set<WorkspaceDir> getSetWorkspaceDir(WorkspaceDir workspaceDirIncomplete);

	// Determines if a path to a workspace directory is known to the workspace.
	// get.. raises an exception if not know.
	boolean isPathWorkspaceDirExists(Path pathWorkspaceDir);

	//Useful so that a method that has a hold on a workspace directory can know where it comes from (UserModuleVersion or SystemModule)
	WorkspaceDir getWorkspaceDirFromPath(Path pathWorkspaceDir);
}
