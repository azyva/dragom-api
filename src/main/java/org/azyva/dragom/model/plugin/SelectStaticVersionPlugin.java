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

import org.azyva.dragom.model.Version;

/**
 * This plugin is used by tools when a static Version needs to be selected. It
 * is responsible for selecting what that Version will be.
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
public interface SelectStaticVersionPlugin extends ModulePlugin {
  /**
   * Selected a static Version based on an existing dynamic Version.
   * <p>
   * The static Version may or may not exist.
   *
   * @param versionDynamic Base dynamic Version.
   * @return Static Version.
   */
  Version selectStaticVersion(Version versionDynamic);
}
