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
 * <p>
 * {@link OptimisticLockHandle} is used to handle optimistic locking. Support for
 * optimistic locking is not required. But if not supported, implementations must
 * respect the contract imposed by OptimisticLockHandle and the methods that use
 * it, doing as if the lock was always valid.
 * <p>
 * If optimistic locking is supported, it generally is by including a last
 * modification timestamp or unique revision number in the OptimisticLockHandle
 * implementation.
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
	 * Creates an {@link OptimisticLockHandle}.
	 * <p>
	 * Implementations which do not support optimistic lock management should return
	 * an OptimisticLockHandle that reports being in the requested state, with no
	 * relation to the state of any data.
	 *
	 * @param indLock Indicates if the OptimisticLockHandle must be initially locked.
	 * @return OptimisticLockHandle.
	 */
	OptimisticLockHandle createOptimisticLockHandle(boolean indLock);

	/**
	 * Verifies if the lock held by the an {@link OptimisticLockHandle} is valid,
	 * meaning that its state corresponds to the state of the data it represents.
	 * <p>
	 * Implementations which do not support optimistic lock management should return
	 * true.
	 *
	 * @param optimisticLockHandle OptimisticLockHandle.
	 * @return Indicates if the lock is valid.
	 */
	boolean isOptimisticLockValid(OptimisticLockHandle optimisticLockHandle);

	/**
	 * Returns the {@link NodeConfigTransferObject}.
	 * <p>
	 * If optimisticLockHandle is null, no optimistic lock is managed.
	 * <p>
	 * If optimisticLockHandle is not null and is locked
	 * ({@link OptimisticLockHandle#isLocked}), its state must correspond to the state
	 * of the data it represents, otherwise {@link OptimisticLockException} is thrown.
	 * <p>
	 * If optimisticLockHandle is not null and is not locked, it is simply locked to
	 * the current state of the data.
	 * <p>
	 * Implementations which do not support optimistic lock management simply change
	 * the state of the {@link OptimisticLockHandle} to locked if optimisticLockHandle
	 * is not null.
	 *
	 * @param optimisticLockHandle OptimisticLockHandle. Can be null.
	 * @return {@link NodeConfigTransferObject}.
	 * @throws OptimisticLockException Can be thrown only if optimisticLockHandle is
	 *   not null and is locked. This is a RuntimeException that may be of interest to
	 *   the caller.
	 */
	NodeConfigTransferObject getNodeConfigTransferObject(OptimisticLockHandle optimisticLockHandle) throws OptimisticLockException;

	/**
	 * Sets the {@link NodeConfigTransferObject}.
	 * <p>
	 * If the instance has a parent object (i.e., it is a child of
	 * {@link MutableClassificationNodeConfig} or the root
	 * MutableClassificationNodeConfig of {@link MutableConfig}), it must ensure the
	 * parent is adjusted.
	 * <p>
	 * If the instance is new ({@link #isNew}), it is finalized and installed within
	 * the parent, if appropriate.
	 * <p>
	 * If optimisticLockHandle is null, no optimistic lock is managed.
	 * <p>
	 * If optimisticLockHandle is not null, it must be locked
	 * ({@link OptimisticLockHandle#isLocked}) and its state must correspond to the
	 * state of the data it represents, otherwise {@link OptimisticLockException} is
	 * thrown. Upon return, it must be updated to correspond to the new state of the
	 * data it represents.
	 * <p>
	 * Implementations which do not support optimistic lock management can simply
	 * ignore optimisticLockHandle, or better, validate that if it is not null, it is
	 * locked.
	 *
	 * @param nodeConfigTransferObject NodeConfigTransferObject.
	 * @param optimisticLockHandle OptimisticLockHandle. Can be null.
	 * @throws OptimisticLockException Can be thrown only if optimisticLockHandle is
	 *   not null. This is a RuntimeException that may be of interest to
	 *   the caller.
	 * @throws DuplicateNodeExcpeption When the new configuration data would introduce
	 *   a duplicate {@link MutableNode} within the parent. This is a RuntimeException
	 *   that may be of interest to the caller.
	 */
	void setNodeConfigTransferObject(NodeConfigTransferObject nodeConfigTransferObject, OptimisticLockHandle optimisticLockHandle) throws OptimisticLockException, DuplicateNodeException;

	/**
	 * Deletes the MutableNodeConfig.
	 * <p>
	 * If the instance has a parent object (i.e., it is a child of
	 * {@link MutableClassificationNodeConfig} or the root
	 * MutableClassificationNodeConfig of {@link MutableConfig}), it must ensure the
	 * parent is adjusted.
	 * <p>
	 * Once deleted, a MutableNodeConfig must not be used anymore.
	 */
	void delete();
}
