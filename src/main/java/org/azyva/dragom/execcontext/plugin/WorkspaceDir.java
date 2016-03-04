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

/**
 * This is a marker interface for classes representing workspace directories.
 *
 * WorkspacePlugin directories generally fall in two broad categories:
 *
 * - User workspace directory: Directory that is created by the system for the
 *   user. It belongs to the user. Tools and plugins can interact with a user
 *   workspace directory on behalf of the user. The lifecycle of such directories
 *   is generally controlled by the user as these directories belong to the user.
 *   A typical example is a directory that contains a module version checked out
 *   by a tool.
 *
 * - System workspace directory: Directory that is created by the system,
 *   generally a plugin, for its own use. Such a directory is generally hidden to
 *   the user. The lifecycle of such a directory is controlled by the system or
 *   plugins. System workspace directories can be private to a specific plugin or
 *   shared among multiple plugins. A typical exemple is a directory that contains
 *   a Git clone of a module repository that is required by a plugin to inspect the
 *   module's files, but has not been requested by the user.
 *
 * Only one marker interface is used for all types of workspace directories,
 * despite the two categories mentionned above as each class is handled
 * specifically anyways. However, classes marked by this interface can include in
 * their name the work "user" or "system" to make things easier for developers.
 *
 * Plugins can manipulate workspace directories as required. System workspace
 * directories can be converted or copied to user workspace directories to avoid
 * useless server accesses.
 *
 * The different types of workspace directories, as represented by the different
 * classes marked by this interface, can and should be known to workspace
 * implementations. If a workspace directory class is not known to a workspace
 * implementation, the workspace can implement a generic behavior.
 *
 * TODO: Maybe the workspace dir class could help in implementing such a default behavior (random vs fixed directory for instance).
 * Maybe have predefined methods in this interface.
 *
 * @author David Raymond
 */

public interface WorkspaceDir {
}
