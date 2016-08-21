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

import org.azyva.dragom.model.config.Config;
import org.azyva.dragom.model.config.DuplicateNodeException;
import org.azyva.dragom.model.config.MutableNodeConfig;
import org.azyva.dragom.model.config.NodeConfigTransferObject;
import org.azyva.dragom.model.config.OptimisticLockException;

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
	 * @return {@link NodeConfigTransferObject}.
	 */
	NodeConfigTransferObject getNodeConfigTransferObject();

	/**
	 * Sets the {@link NodeConfigTransferObject}.
	 * <p>
	 * It is possible that the MutableNode cannot be changed, in which case false is
	 * returned. The reason for not being able to change the MutableNode is generally
	 * because of concurrency (it was changed between the call to
	 * {@link #getNodeConfigTransferObject} and {@link setNodeConfigTransferObject}.
	 * This functionality generally comes from the similar fonctionality in
	 * {@link MutableNodeConfig#setNodeConfigTransferObject}.
	 *
	 * @param nodeConfigTransferObject NodeConfigTransferObject.
	 * @return Indicates if the {@link MutableNode} could be changed.
	 */
	/**
	 * Sets the {@link NodeConfigTransferObject}.
	 * <p>
	 * If the implementation supports throwing OptimisticLockException, it is
	 * generally because it exposes the similar functionality in
	 * {@link MutableNodeConfig#setNodeConfigTransferObject}.
	 *
	 * @param nodeConfigTransferObject NodeConfigTransferObject.
	 * @throws OptimisticLockException When the implementation detects that the
	 *   configuration data was changed since the call to
	 *   {@link #getNodeConfigTransferObject}. This detection is optional.
	 * @throws DuplicateNodeExcpeption When the new configuration data would introduce
	 *   a duplicate {@link MutableNode}.
	 */
	void setNodeConfigTransferObject(NodeConfigTransferObject nodeConfigTransferObject) throws OptimisticLockException, DuplicateNodeException;

	/**
	 * Deletes the MutableNode. If the implementation has a parent object object
	 * (i.e., it is a child of {@link MutableClassificationNode} or the root
	 * {@link MutableClassificationNode} of {@link Model}), it must ensure the
	 * parent is adjusted.
	 */
	void delete();

	/**
	 * Indicates if the MutableNode has been deleted.
	 * <p>
	 * A MutableNode can be deleted only if {@link #delete} is called. Specifically an
	 * implementation must not rely on removing MutableNode's from internal caches to
	 * force their recreation when changes to configuration data could affect their
	 * state. Instead, the implementation must ensure that created MutableNode's
	 * remain valid, but can reset their internal state if required.
	 * <p>
	 * delete could be called, followed by the recreation of the MutableNode. But
	 * delete is an interface method and is intended to be called by an external
	 * caller, not by the implementation itself.
	 * <p>
	 * A special case exists. If the MutableNode has been created dynamically using
	 * {@link NodeBuilder} and is converted in to one based on
	 * {@link MutableNodeConfig}, the former can be deleted. This is getting close to
	 * a hack since this case is hard to handle cleanly. In general, it is expected
	 * that an application allowing to manage persistent {@link MutalConfig} data will
	 * not include tasks that would cause the dynamic creation of MutableNode's, so
	 * this case would not occur. And tasks which can benefit from the dynamic
	 * creation of {@link Node}'s are expected to be tools that consume the
	 * {@link Config} data to perform jobs on reference graphs and in turn, these do
	 * not modify the Config, so this case would not occur either.
	 *
	 * @return Indicates if the MutableNode has been deleted.
	 */
	boolean isDeleted();
}
