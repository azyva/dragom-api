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

package org.azyva.dragom.model.plugin;

import java.io.Writer;

import org.azyva.dragom.model.Module;
import org.azyva.dragom.model.ModuleVersion;
import org.azyva.dragom.model.Version;

/**
 * Allows building a {@link Module} remotely.
 * <p>
 * In many setups, some builds are performed in a controlled environment, as
 * opposed to a workspace on a user's workstation. For example, official release
 * builds for static {@link Version}'s for which the resulting artifacts can
 * potentially be deployed to a production environment, or that can be reused in
 * such builds of other Module's, generally need to be performed in such a
 * controlled environment typically provided by a tool such as Jenkins.
 * <p>
 * This plugin provides an abstract interface to such remote build environments so
 * that they can be triggered by BuildRemote from dragom-core.
 *
 * @author David Raymond
 */
public interface RemoteBuilderPlugin extends ModulePlugin {
	/**
	 * Represents a remote build and allows obtaining its status and interacting with
	 * it.
	 */
	public interface RemoteBuildHandle {
		/**
		 * Possible remote build statuses.
		 */
		public enum RemoteBuildStatus {
			/**
			 * Indicates that this build cannot be performed remotely.
			 * <p>
			 * If the build cannot be performed remotely, this must be its initial and only
			 * status after calling the {@link RemoteBuilderPlugin#submitBuild}.
			 */
			CANNOT_BUILD_REMOTELY,

			/**
			 * Indicates that the build is waiting in some queue.
			 */
			QUEUED,

			/**
			 * Indicates that the build is running.
			 */
			RUNNING,

			/**
			 * Indicates that the build is completed.
			 */
			COMPLETED
		}

		/**
		 * @return ModuleVersion associated with the remote build.
		 */
		ModuleVersion getModuleVersion();

		/**
		 * @return Human-readable description of the location where the remote build
		 *   executes. For example, this can include the URL of a job build in Jenkins.
		 */
		String getLocation();

		/**
		 * Returns the {@link RemoteBuildStatus}.
		 * <p>
		 * If the last known status of the build is such that it can change, this method
		 * should query the remote build system for the new status.
		 * <p>
		 * However, if the last known status of the build is
		 * {@link RemoteBuildStatus#CANNOT_BUILD_REMOTELY} or
		 * {@link RemoteBuildStatus#COMPLETED}, it is expected to be returned immediately
		 * without wasting time querying the remote build system.
		 *
		 * @return RemoteBuildStatus.
		 */
		RemoteBuildStatus getRemoteBuildStatus();

		/**
		 * Returns the reason why the build cannot be performed remotely.
		 * <p>
		 * Can only be called if getRemoteBuildStatus returned
		 * RemoteBuildStatus.CANNOT_BUILD_REMOTELY.
		 *
		 * @return See description.
		 */
		String getCannotBuildRemotelyReason();

		/**
		 * Blocks until the remote build is completed.
		 */
		void waitCompleted();

		/**
		 * Indicates the build was successful, as opposed to a failure.
		 * <p>
		 * If the build is not completed, this method blocks until it is.
		 *
		 * @return Indicates the build was successful.
		 */
		boolean isSuccess();

		/**
		 * Writes the log of the build in the provided Writer.
		 * <p>
		 * If the build is not completed, this method blocks until it is.
		 *
		 * @param writerLog Writer.
		 */
		void getLog(Writer writerLog);
	}

	/**
	 * Indicates if the {@link Version} if the {@link Module} needs to be built.
	 * <p>
	 * Typically for a static Version, the plugin would verify if that Version has
	 * already been built by looking for the corresponding ArtifactVersion in some
	 * artifact repository.
	 * <p>
	 * For a dynamic Version, this is not so obvious. Generally, remote building
	 * dynamic Version's from Dragom is not expected to be a common use case as
	 * dynamic Version's are expected to be built automatically by some continuous
	 * integration tool in response to commits performed in the SCM, all this without
	 * the help of Dragom.
	 * <p>
	 * Still, remote building dynamic Version's may in some cases make sense. If such
	 * a practice is common in a given environment, the plugin should probably query
	 * the remote building system whether there are new changes in the SCM compared to
	 * the last build. Otherwise, the plugin may always return true for dynamic
	 * Version's.
	 *
	 * @param version Version of the {@link Module}.
	 * @return Indicates if there is something to build.
	 */
	boolean isBuildNeeded(Version version);

	/**
	 * Schedules the remote build.
	 * <p>
	 * An instance of this plugin is associated with a specific {@link Module}. But
	 * not a specific {@link Version}, which must be specified.
	 *
	 * @param version Version of the {@link Module} to build.
	 * @return RemoteBuildHandle.
	 */
	RemoteBuildHandle submitBuild(Version version);
}
