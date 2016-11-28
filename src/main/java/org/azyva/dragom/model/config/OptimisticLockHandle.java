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

import org.azyva.dragom.model.Model;
import org.azyva.dragom.model.MutableNode;
import org.azyva.dragom.model.Node;

/**
 * Represents an optimistic lock held on an entity.
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/Optimistic_concurrency_control">Optimistic concurrency control</a>
 * for a general discussion about optimistic locking.
 * <p>
 * In Dragom, optimistic locking is used mainly by {@link MutableNodeConfig} and
 * {@link MutableNode}, the latter being what is generally seen by applications
 * using Dragom.
 * <p>
 * The idea is that an application that allow modifying the configuration data
 * underlying a {@link Model} is expected to the {@link Node}'s to the user in a
 * hierarchical manner, allowing him to edit their properties on a Node by Node
 * basis. The user can look at Node properties, modify them and then save the
 * changes. At that point it is pertinent to ensure that the same Node properties
 * have not been changed by another user. To do that, the application obtains an
 * OptimisticLockHandle representing the state of the Node data. When saving the
 * data, it provides the same OptimisticLockHandle which allows the object to
 * validate the state of the data has not changed since it was read.
 * <p>
 * Typically, a last modification timestamp or unique revision number is maintained
 * within an OptimisticLockHandle implementation.
 * <p>
 * An OptimisticLockHandle is mutable. When configuration data is saved
 * successfully (the state of the OptimisticLockHandle corresponds to the state of
 * the data it represents), it is updated to reflect the new state of the data so
 * that the caller can save changed data repetitively withouth having to read them
 * again.
 * <p>
 * Optimistic locking is managed externally to {@link NodeConfigTransferObject},
 * even though the data represented by an OptimisticLockHandle is generally the
 * state of a NodeConfigTransferObject. The reason is to support the fact that
 * applications manage multiple independent groups of properties for a given Node,
 * for which independent NodeConfigTransferObject's are typically managed. But when
 * one is saved, it must not rendre the lock representing the others since they all
 * relate to the same conversation the user has. The caller can therefore obtain a
 * single OptimisticLockHandle that it shares among the multiple groups of data.
 *
 * @author David Raymond
 */
public interface OptimisticLockHandle {
	/**
	 * Indicates if the OptimisticLockHandle is actually locked, meaning that it
	 * represents some state.
	 * <p>
	 * An OptimisticLockHandle that is not locked is expected to get automatically
	 * locked to the state of data that is obtained (using a "get" method).
	 *
	 * @return Indicates if the OptimisticLockHandle is locked.
	 */
	boolean isLocked();

	/**
	 * Clears the lock.
	 */
	void clearLock();
}
