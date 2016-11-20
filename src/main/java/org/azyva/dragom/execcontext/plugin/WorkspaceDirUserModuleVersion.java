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

import org.azyva.dragom.model.ModuleVersion;

/**
 * Represents a user workspace directory that contains a {@link ModuleVersion}.
 *
 * During a checkout operation, for example, such directories are created for
 * the user.
 *
 * Depending on the SCM from which the module comes from, that directory will
 * generally also be a workspace for module managed by this SCM. For example, if
 * the module comes from a Git repository, the directory will be a Git clone
 * of the repository for the module, with the specified version checked out.
 *
 * This class does not include the plugin that created the workspace directory
 * since it belongs to the user and any plugin can potentially interact with it
 * one behalf of the user. However only one SCM plugin will generally interact
 * with such a workspace directory since it is module specific and one SCM plugin
 * is associated with a given module.
 *
 * This class wraps ModuleVersion which already provides the required information
 * and behavior. But it must implement the marker interface WorkspaceDir, which is
 * why ModuleVersion is not used directly.
 *
 * @author David Raymond
 */
public class WorkspaceDirUserModuleVersion implements WorkspaceDir {
	private ModuleVersion moduleVersion;

	// TODO: The fields cannot be null.
	public WorkspaceDirUserModuleVersion(ModuleVersion moduleVersion) {
		this.moduleVersion = moduleVersion;
	}

	public ModuleVersion getModuleVersion() {
		return this.moduleVersion;
	}

	/**
	 * @return String to help recognize the {@link WorkspaceDir} instance, in logs for
	 *   example.
	 */
	@Override
	public String toString() {
		return "WorkspaceDirUserModuleVersion [moduleVersion=" + this.moduleVersion + "]";
	}

	/**
	 * Override of hashCode to make instances of this class usable efficiently as
	 * map keys. This implementation was generated automatically using Eclipse and then
	 * simplified.
	 */
	@Override
	public int hashCode() {
		return this.moduleVersion.hashCode();
	}

	/**
	 * Override of equals to make to make instances of this class usable as map keys
	 * and in other contexts where value equality semantics are required.
	 */
	@Override
	public boolean equals(Object other) {
		WorkspaceDirUserModuleVersion workspaceDirUserModuleVersionOther;

		if (this == other) {
			return true;
		}

		if (!(other instanceof WorkspaceDirUserModuleVersion)) {
			return false;
		}

		workspaceDirUserModuleVersionOther = (WorkspaceDirUserModuleVersion)other;

		return this.moduleVersion.equals(workspaceDirUserModuleVersionOther.moduleVersion);
	}
}
