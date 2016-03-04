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

import org.azyva.dragom.model.Module;

/**
 * Base interface for module plugins..
 *
 * @author David Raymond
 */
public interface ModulePlugin extends NodePlugin {
	/**
	 * Returns the {@link Module} to which this ModulePlugin is attached.
	 * <p>
	 * The object returned should be the same as that returned by {NodePlugin#getNode}
	 * but cast as a Module.
	 *
	 * @return See description.
	 */
	Module getModule();
}
