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

import org.azyva.dragom.model.MutableNode;

/**
 * Thrown by {@link MutableNodeConfig} and {@link MutableNode} in relation to the
 * handling of {@link OptimisticLockHandle}.
 * <p>
 * We do not reuse the existing javax.persistence.OptimisticLockException since:
 * <p>
 * <li>It relates specifically to JPA and MutableNodeConfig data are not necessarily
 *     managed by JPA;</li>
 * <li>It is a RuntimeException whereas in Dragom it is a regular Exception to force
 *     the caller to handle it.</li>
 * <p>
 * This exception does not have the usual constructors taking a message and/or
 * throwable parameter. The caller is expected to handle the exception and know the
 * context in which it is thrown.
 *
 * @author David Raymond
 */
public class OptimisticLockException extends RuntimeException {
	// To keep the compiler from complaining.
	static final long serialVersionUID = 0;

	/**
	 * Constructor.
	 */
	public OptimisticLockException() {
	}
}
