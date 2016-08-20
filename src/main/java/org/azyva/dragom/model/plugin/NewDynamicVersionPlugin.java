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

import org.azyva.dragom.apiutil.ByReference;
import org.azyva.dragom.model.Version;

/**
 * This plugin is used by tools when a new dynamic Version needs to be created. It
 * is responsible for establishing what that new Version will be.
 *
 * It is up to the plugin implementation to use whatever strategy is appropriate
 * for establishing the new Version.
 *
 * The strategy can include interacting with the user through the
 * UserInteractionCallbackPlugin plugin within the ExecContext.
 *
 * The ScmPlugin can also be interrogated for existing Versions if the strategy
 * calls for it.
 *
 * The plugin is not responsible for actually creating the Version.
 *
 * The new Version can actually exist if the strategy can involve reusing existing
 * Version.
 *
 * @author David Raymond
 */
public interface NewDynamicVersionPlugin extends ModulePlugin {
	/**
	 * Gets a new dynamic Version to be created or switched to based on an existing
	 * Version which can be static (e.g., bugfix Version based on a release Version)
	 * or dynamic (e.g., feature Version based on a project Version).
	 *
	 * The caller is responsible for verifying if the new dynamic Version exists. If
	 * it does the Version returned in byReferenceVersionBase (if any) must be
	 * ignored. But the caller must not infer whether the dynamic Version exists or
	 * not based on whether a base Version was returned.
	 *
	 * It may be the case that the existing Version is already dynamic and that no new
	 * Version needs to be created. In that version must be returned.
	 *
	 * @param version Base version.
	 * @param byReferenceVersionBase The base Version to use for creating the new
	 *   dynamic Version will be stored there.
	 * @return Dynamic Version. May be the same as the original version.
	 */
	Version getVersionNewDynamic(Version version, ByReference<Version> byReferenceVersionBase);
}
