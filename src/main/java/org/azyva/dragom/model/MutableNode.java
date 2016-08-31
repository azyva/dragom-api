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
import org.azyva.dragom.model.config.MutableConfig;
import org.azyva.dragom.model.config.MutableNodeConfig;
import org.azyva.dragom.model.config.NodeConfigTransferObject;
import org.azyva.dragom.model.config.OptimisticLockException;
import org.azyva.dragom.model.config.OptimisticLockHandle;

/**
 * Extension of {@link Node} that allows changing the configuration data.
 * <p>
 * Methods in this interface are similar to those in {@link MutableNodeConfig}.
 * This is normal since what is mutable is the configuration data, which
 * MutableNode simply exposes. Note however that only {@link NodeConfigData} is
 * exposed, and not MutableNodeConfig itself. This could allow a MutableNode
 * implementation to not be based on MutableNodeConfig, although the inverse is
 * generally the case.
 * <p>
 * {@link OptimisticLockHandle} is used to handle optimistic locking. Support for
 * optimistic locking is not required. But if not supported, implementations must
 * respect the contract imposed by OptimisticLockHandle and the methods that use
 * it, doing as if the lock was always valid.
 * <p>
 * Optimistic lock management is generally delegated to the similar methods in
 * {@link MutableNodeConfig} on which a Node is generally based.
 *
 * @author David Raymond
 * @see MutableModel
 */
public interface MutableNode extends Node {
	/**
	 * See {@link MutableNodeConfig#isNew}.
	 *
	 * @return Indicates that the MutableNode has just been created and that
	 *   {@link #setNodeConfigTransferObject} has not been called yet.
	 */
	boolean isNew();

	/**
	 * Creates an {@link OptimisticLockHandle}.
	 * <p>
	 * See {@link MutableNodeConfig#createOptimisticLockHandle}.
	 *
	 * @param indLock Indicates if the OptimisticLockHandle must be initially locked.
	 * @return OptimisticLockHandle.
	 */
	OptimisticLockHandle createOptimisticLockHandle(boolean indLock);

	/**
	 * Verifies if the lock held by the an {@link OptimisticLockHandle} is valid,
	 * meaning that its state corresponds to the state of the data it represents.
	 * <p>
	 * See {@link MutableNodeConfig#isOptimisticLockValid}.
	 *
	 * @param optimisticLockHandle OptimisticLockHandle.
	 * @return Indicates if the lock is valid.
	 */
	boolean isOptimisticLockValid(OptimisticLockHandle optimisticLockHandle);

	/**
	 * Returns the {@link NodeConfigTransferObject}.
	 * <p>
	 * See {@link MutableNodeConfig#getNodeConfigTransferObject}.
	 *
	 * @param optimisticLockHandle OptimisticLockHandle. Can be null.
	 * @return {@link NodeConfigTransferObject}.
	 * @throws OptimisticLockException. This is a RuntimeException that may be of
	 *   interest to the caller.
	 */
	NodeConfigTransferObject getNodeConfigTransferObject(OptimisticLockHandle optimisticLockHandle) throws OptimisticLockException;

	/**
	 * Sets the {@link NodeConfigTransferObject}.
	 * <p>
	 * See {@link MutableNodeConfig#setNodeConfigTransferObject}.
	 *
	 * @param nodeConfigTransferObject NodeConfigTransferObject.
	 * @param optimisticLockHandle OptimisticLockHandle. Can be null.
	 * @throws OptimisticLockException. This is a RuntimeException that may be of
	 *   interest to the caller.
	 * @throws DuplicateNodeExcpeption. This is a RuntimeException that may be of
	 *   interest to the caller.
	 */
	void setNodeConfigTransferObject(NodeConfigTransferObject nodeConfigTransferObject, OptimisticLockHandle optimisticLockHandle) throws OptimisticLockException, DuplicateNodeException;

	/**
	 * Deletes the MutableNode.
	 * <p>
	 * See {@link MutableNodeConfig#delete}.
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
	 * {@link NodeBuilder} and is converted into one based on
	 * {@link MutableNodeConfig}, the former can be deleted. This is getting close to
	 * a hack since this case is hard to handle cleanly. In general, it is expected
	 * that an application allowing to manage persistent {@link MutableConfig} data
	 * will not include tasks that would cause the dynamic creation of MutableNode's,
	 * so this case would not occur. And tasks which can benefit from the dynamic
	 * creation of {@link Node}'s are expected to be tools that consume the
	 * {@link Config} data to perform jobs on reference graphs and in turn, these do
	 * not modify the Config, so this case would not occur either.
	 *
	 * @return Indicates if the MutableNode has been deleted.
	 */
	boolean isDeleted();
}
