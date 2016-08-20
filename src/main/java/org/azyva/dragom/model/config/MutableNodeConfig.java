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

import org.azyva.dragom.model.MutableNode;

/**
 * Extension of {@link NodeConfig} that allows changing the configuration data.
 *
 * @author David Raymond
 * @see MutableConfig
 */
public interface MutableNodeConfig extends NodeConfig {
	/**
	 * @return Indicates that the MutableNodeConfig has just been created and that
	 *   {@link #setNodeConfigTransferObject} has not been called yet.
	 */
	boolean isNew();

	/**
	 * @return {@link NodeConfigTransferObject}.
	 */
	NodeConfigTransferObject getNodeConfigTransferObject();

	/**
	 * Sets the {@link NodeConfigTransferObject}.
	 * <p>
	 * If the implementation supports throwing OptimisticLockException, it generally
	 * does so by including a hidden last modification timestamp field within the
	 * NodeConfigTransferObject.
	 *
	 * @param nodeConfigTransferObject NodeConfigTransferObject.
	 * @throws OptimisticLockException When the implementation detects that the
	 *   configuration data was changed since the call to
	 *   {@link getNodeConfigTransferObject}. This detection is optional.
	 * @throws DuplicateNodeExcpeption When the new configuration data would introduce
	 *   a duplicate {@link MutableNode} within the parent.
	 */
	void setNodeConfigTransferObject(NodeConfigTransferObject nodeConfigTransferObject) throws OptimisticLockException, DuplicateNodeException;

	/**
	 * Deletes the MutableNodeConfig. If the implementation has a parent object
	 * (i.e., it is a child of {@link MutableClassificationNodeConfig} or the root
	 * {@link MutableClassificationNodeConfig} of {@link MutableConfig}), it must
	 * ensure the parent is adjusted.
	 * <p>
	 * Once deleted, a MutableNodeConfig must not be used anymore.
	 */
	void delete();
}
