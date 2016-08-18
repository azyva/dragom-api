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

/**
 * Extension of {@link NodeConfig} that allows changing the configuration data.
 *
 * @author David Raymond
 * @see MutableConfig
 */
public interface MutableNodeConfig extends NodeConfig {
	/**
	 * @return Indicates that the MutableNodeConfig has just been created and that
	 *   setNodeConfigValue has not been called yet.
	 */
	boolean isNew();

	/**
	 * @return {@link NodeConfigValue}.
	 */
	NodeConfigValue getNodeConfigValue();

	/**
	 * Sets the {@link NodeConfigValue}.
	 *
	 * @param nodeConfigValue NodeConfigValue.
	 */
	void setNodeConfigValue(NodeConfigValue nodeConfigValue);

	/**
	 * Deletes the {@link MutableNodeConfig}. If the implementation has a parent
	 * object (.e.g.: MutableClassificationNodeConfig or MutableConfig), it must
	 * ensure the parent is adjusted.
	 * <p>
	 * Once deleted, a MutableNodeConfig must not be used anymore.
	 */
	void delete();
}