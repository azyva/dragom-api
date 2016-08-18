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

package org.azyva.dragom.model;

import org.azyva.dragom.model.config.NodeConfigValue;

/**
 * Extension of {@link Node} that allows changing the configuration data.
 *
 * @author David Raymond
 * @see MutableModel
 */
public interface MutableNode extends Node {
	/**
	 * @return Indicates that the MutableNode has just been created and that
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
	 * Deletes the MutableNode. If the implementation has a parent object
	 * (.e.g.: MutableClassificationNode or MutableModel), it must ensure the
	 * the parent is adjusted.
	 */
	void delete();

	/**
	 * @return Indicates if the MutableNode is destroyed (either deleted using
	 *   {@link MutableNode#delete} or removed from its parent cache because of a
	 *   configuration data change so that it gets recreated when needed.
	 */
	boolean isDestroyed();
}
