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

import org.azyva.dragom.apiutil.ByReference;
import org.azyva.dragom.model.ModuleVersion;
import org.azyva.dragom.model.Version;
import org.azyva.dragom.reference.ReferencePath;

/**
 * This plugin is used by tools when a dynamic Version needs to be selected.
 * It is responsible for selecting what that Version will be.
 *
 * It is up to the plugin implementation to use whatever strategy is appropriate
 * for selecting the Version.
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
public interface SelectDynamicVersionPlugin extends ModulePlugin {
  /**
   * Selects a dynamic Version based on an existing Version which can be static
   * (e.g., hotfix Version based on a release Version) or dynamic (e.g., feature
   * Version based on a project Version).
   *
   * The caller is responsible for verifying if the selected dynamic Version exists.
   * If it does the Version returned in byReferenceVersionBase (if any) must be
   * ignored. But the caller must not infer whether the dynamic Version exists or
   * not based on whether a base Version was returned.
   *
   * It may be the case that the existing Version is already dynamic and that no new
   * Version needs to be created. In that case version must be returned.
   *
   * @param version Original version.
   * @param byReferenceVersionBase The base Version to use for creating the new
   *   dynamic Version will be stored there.
   * @param referencePath ReferencePath of the {@link ModuleVersion}.
   *   Implementations can use this information to, for example, validate that the
   *   ModuleVersion exists within a ReferencePath that contains only static
   *   Versions (for hotfixes).
   * @return Dynamic Version. May be the same as the original version. Can be null
   *   to indicate to the caller that the existing Version should not be changed.
   */
  Version selectDynamicVersion(Version version, ByReference<Version> byReferenceVersionBase, ReferencePath referencePath);
}
