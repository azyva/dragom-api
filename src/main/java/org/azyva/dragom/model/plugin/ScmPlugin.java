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

package org.azyva.dragom.model.plugin;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.azyva.dragom.model.ArtifactVersion;
import org.azyva.dragom.model.Module;
import org.azyva.dragom.model.Version;

// TODO: What about merges? Should this plugin handle merges? Are they similar enough between SCM?
// TODO: It is not the responsibility of this plugin to manage the version specified within the source code of the module. See VersionManagerPlugin.
// TODO: Some methods require a pathModuleWorkspace. The user should be able to decide to keep this path or make it temporary only.
public interface ScmPlugin extends ModulePlugin {
	/**
	 * Commit attribute to store the equivalent static {@link Version} associated with
	 * a commit. Generally used on commits that revert the adjustment to the
	 * {@link ArtifactVersion} after the new static version was created.
	 * <p>
	 * This is often not directly used by ScmPlugin implementations, but this is still
	 * the most logical place to declare this constant.
	 */
	String COMMIT_ATTR_EQUIVALENT_STATIC_VERSION = "dragom-equivalent-static-version";

	/**
	 * Commit attribute to store the base {@link Version} of a new Version.
	 * <p>
	 * This applies to both static and dynamic Version's.
	 * <p>
	 * This is often not directly used by ScmPlugin implementations, but this is still
	 * the most logical place to declare this constant.
	 */
	public static final String COMMIT_ATTR_BASE_VERSION = "dragom-base-version";

	/**
	 * Commit attribute indicating that the commit changes the {@link ArtifactVersion}
	 * of the {@link Module}. The possible values are true and false, although if a
	 * commit does not change the ArtifactVersion this attribute will generally not be
	 * included.
	 * <p>
	 * This is often not directly used by ScmPlugin implementations, but this is still
	 * the most logical place to declare this constant.
	 */
	public static final String COMMIT_ATTR_VERSION_CHANGE = "dragom-version-change";

	/**
	 * Commit attribute indicating that the commit changes the {@link Version} or
	 * {@link ArtifactVersion} of a reference in the {@link Module}. The possible
	 * values are true and false, although if a commit does not change the Version
	 * or ArtifactVersion of a reference this attribute will generally not be
	 * included.
	 * <p>
	 * This is often not directly used by ScmPlugin implementations, but this is still
	 * the most logical place to declare this constant.
	 */
	public static final String COMMIT_ATTR_REFERENCE_VERSION_CHANGE = "dragom-reference-version-change";

	// Represents a commit.
	public class Commit {
		// Id of the commit in a SCM-dependent format.
		public String id;
		// Message of the commit.
		public String message;
		// Static Version based on this commit.
		public Version[] arrayVersionStatic;
		// Attributes of the commit.
		public Map<String, String> mapAttr;

		@Override
		public String toString() {
			return "ScmPlugin.Commit [id=" + this.id + (this.message == null ? "" : ", message=" + this.message) + ']';
		}
	}
//TODO Should we include more information about the commit? Author, timestamp?

	public class CommitPaging {
		public int startIndex;
		public int maxCount;
		public int returned;
		public boolean indDone;

		public CommitPaging() {
			this.maxCount = -1;
		}

		public CommitPaging(int maxCount) {
			this.maxCount = maxCount;
		}
	}

	public enum GetListCommitFlagEnum {
		IND_INCLUDE_MESSAGE,
		IND_INCLUDE_VERSION_STATIC,
		IND_INCLUDE_MAP_ATTR,
		IND_UPDATE_START_INDEX
	};

	// Represents base information about a version.
	public class BaseVersion {
		// Version for which the base is described.
		public Version version;
		// Base Version of this version.
		public Version versionBase;
		// Id of the commit at which the base version was when the version was created.
		public String commitId;
	}

	/**
	 * Flags that can be passed to isSync.
	 */
	public enum IsSyncFlagEnum {
		/**
		 * Refers to changes that may be present locally in the workspace but not in the
		 * remote repository.
		 */
		LOCAL_CHANGES,

		/**
		 * Refers to changes that may be present in the remote repository but not locally
		 * in the workspace.
		 */
		REMOTE_CHANGES;

		public static final EnumSet<IsSyncFlagEnum> LOCAL_CHANGES_ONLY = EnumSet.of(LOCAL_CHANGES);
		public static final EnumSet<IsSyncFlagEnum> REMOTE_CHANGES_ONLY = EnumSet.of(REMOTE_CHANGES);
		public static final EnumSet<IsSyncFlagEnum> ALL_CHANGES = EnumSet.of(LOCAL_CHANGES, REMOTE_CHANGES);
	}

	// TODO: Can be used on a temporary module definition, in the process of verifying if a dynamically created modules exists. Its parent does not need to include it as a child.
	boolean isModuleExists();

	Version getDefaultVersion();

	// TODO Absolutely requires a pathModuleWorkspace
	// Path must be empty. In fact, I think path should not exist. (see checkout tool).
	// Version must exist (verify before...). Caller responsibility to specify version (no null!)
	// Could we specify to export instead of checkout? Maybe name the method "get" or "retrieve"
	// Caller responsibility to setup workspace (since it specifies the path where to checkout.
	// Workspace must be reserved.
	void checkout(Version version, Path pathModuleWorkspace);

	// Checkout in a system workspace directory.
	// If Git, can reuse directories which correspond to modules, not specific versions (may need to switch version)
	//   Since this is Git-specific, we cannot let tools manage this. It must be the plugin. Hence this method.
	// The other checkout is user level. This one is more for system tasks.
	Path checkoutSystem(Version version);

	boolean isVersionExists(Version version);

	// TODO Absolutely requires a pathModuleWorkspace
	// Probably have to distinguish between update required and commit/push required.
	// maybe 2 methods.
	// *** Only for dynamic version.
	boolean isSync(Path pathModuleWorkspace, EnumSet<IsSyncFlagEnum> enumSetIsSyncFlagEnum);

	// TODO Absolutely requires a pathModuleWorkspace
	// fetch then merge (pull)
	boolean update(Path pathModuleWorkspace);

	// TODO Absolutely requires a pathModuleWorkspace
	Version getVersion(Path pathModuleWorkspace);

	/**
	 * Gets the list of Commits for a {@link Version}.
	 * <p>
	 * Enumeration stops at the first commit after creation of the Version.
	 * <p>
	 * If Version is static, the commits enumerated are those of the dynamic Version
	 * from which the static version was created, up to the first commit after the
	 * creation of that base Version.
	 *
	 * @param versionDynamic Dynamic Version.
	 * @param commitPaging CommitPaging information passed and returned. Can be null
	 *   in which case all Commit are returned.
	 * @param enumSetGetListCommitFlag EnumSet of GetListCommitFlag. Can be null.
	 * @return
	 */
	List<Commit> getListCommit(Version version, CommitPaging commitPaging, EnumSet<GetListCommitFlagEnum> enumSetGetListCommitFlagEnum);

	/**
	 * Gets the list of {@link Commit}'s reachable from some {@link Version} but not
	 * from some other Version.
	 *
	 * @param versionSrc Version for which the Commit's must be returned.
	 * @param versionDest Version whose Commit's must be excluded.
	 * @param commitPaging {@link CommitPaging} information passed and returned. Can
	 *   be null in which case all Commit are returned.
	 * @param enumSetGetListCommitFlag EnumSet of GetListCommitFlag. Can be null.
	 * @return
	 */
	List<Commit> getListCommitDiverge(Version versionSrc, Version versionDest, CommitPaging commitPaging, EnumSet<GetListCommitFlagEnum> enumSetGetListCommitFlagEnum);

	// Version from which a version was created. Can be null (for default version).
	BaseVersion getBaseVersion(Version version);

	/**
	 * Gets the list of all static Version of the module.
	 */
	List<Version> getListVersionStatic();

	// TODO Absolutely requires a pathModuleWorkspace
	// TODO With master vs development projects, destroying the original Git repository may be necessary (with possible loss of changes, unpushed or stashed)
	// TODO Maybe switching should not be offered (controlled at a higher level?)
	// TODO But then switching makes sense for Git since it is much more efficient than redoing a clone.
	void switchVersion(Path pathModuleWorkspace, Version version);

	// TODO: No: Maybe always specify a pathModuleWorkspace to take the original version from
	// Use origin version from workspace.
	void createVersion(Path pathModuleWorkspace, Version versionTarget, boolean indSwitch);

	// TODO Absolutely requires a pathModuleWorkspace
	// not sure how to handle the push vs no push. For now, let's keep it simple and always push.
	// then again, mayby no harm done if use this extra parameter.
	// maybe use a more generic name for push: transferToServer?
	// No, for now always push.
	// Probably caller must ensure sync. If commit failure because unsynced, exception.
	void commit(Path pathModuleWorkspace, String message, Map<String, String> mapCommitAttr);

	// Message can be null to let default.
	// If message not null, it is prepended to default message.
	// return false if conflict.
	boolean merge(Path pathModuleWorkspace, Version versionSrc, String message);

	boolean merge(Path pathModuleWorkspace, Version versionSrc, List<Commit> listCommitExclude, String message);

	String getScmType();

	// The SCM URL may be dependent on the version (master vs developpement projects in Stash)
	// not sure how to handle that.
	String getScmUrl(Path pathModuleWorkspace);
}
