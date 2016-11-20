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

import org.azyva.dragom.model.NodePath;


/**
 * Represents a system workspace directory that contains a module.
 *
 * This is useful for plugins which manage module workspace directories that are
 * not version-specific, such as the Git ScmPlugin.
 *
 * This class does not include the plugin that created the workspace directory
 * as only one SCM plugin will generally interact with such a workspace directory
 * since it is module specific and one SCM plugin is associated with a given
 * module.
 *
 * TODO: Maybe the need for really private directories will eventually arise, in which case, the owner plugin will be part of the class.
 *
 * @author David Raymond
 */

public class WorkspaceDirSystemModule implements WorkspaceDir {
	private NodePath nodePath;

	//TODO: Maybe should have the repository URL to support version-specific forks.

	public WorkspaceDirSystemModule(NodePath nodePath) {
		this.nodePath = nodePath;
	}

	public NodePath getNodePath() {
		return this.nodePath;
	}

	/**
	 * @return String to help recognize the {@link WorkspaceDir} instance, in logs for
	 *   example.
	 */
	@Override
	public String toString() {
		return "WorkspaceDirSystemModule [nodePath=" + this.nodePath +"]";
	}

	/**
	 * Override of hashCode to make instances of this class usable efficiently as
	 * map keys. This implementation was generated automatically using Eclipse and then
	 * simplified.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result;

		result = 1;
		result = (prime * result) + this.nodePath.hashCode();

		return result;
	}

	/**
	 * Override of equals to make to make instances of this class usable as map keys
	 * and in other contexts where value equality semantics are required.
	 */
	@Override
	public boolean equals(Object other) {
		WorkspaceDirSystemModule workspaceDirSystemModuleOther;

		if (this == other) {
			return true;
		}

		if (!(other instanceof WorkspaceDirSystemModule)) {
			return false;
		}

		workspaceDirSystemModuleOther = (WorkspaceDirSystemModule)other;

		return this.nodePath.equals(workspaceDirSystemModuleOther.nodePath);
	}

}
