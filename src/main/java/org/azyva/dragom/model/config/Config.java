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

package org.azyva.dragom.model.config;

import org.azyva.dragom.model.Model;

/**
 * Root of a {@link Model} configuration.
 * <p>
 * The Model used during the execution of tools is based on configuration data
 * provided by this interface and its members.
 * <p>
 * Currently Config data is static at runtime. It is conceivable that this
 * interface and its members eventually support modification of the Config, either
 * through additional member methods or separate interfaces implemented by the
 * classes implementing Config. Also runtime change events may eventually be
 * supported so that the Model can adjust to configuration changes at runtime.
 *
 * @author David Raymond
 */
public interface Config {
	/**
	 * @return Root {@link ClassificationNodeConfig}.
	 */
	ClassificationNodeConfig getClassificationNodeConfigRoot();
}
