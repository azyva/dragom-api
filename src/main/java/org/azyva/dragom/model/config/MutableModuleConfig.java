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

package org.azyva.dragom.model.config;

/**
 * Extension of {@link ModuleConfig} that allows changing the configuration data.
 * <p>
 * This interface is an empty extension of MutableNodeConfig since the only
 * configuration data that can be changed for ModuleConfig is that of
 * {@link MutableNodeConfig}.
 *
 * @author David Raymond
 * @see MutableConfig
 */
public interface MutableModuleConfig extends ModuleConfig, MutableNodeConfig {
}
