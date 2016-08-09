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

package org.azyva.dragom.apiutil;

/**
 * There are cases where a method needs to return an object instance but not
 * through its return value since the return value has another use. This generic
 * class allows passing and returning objects by reference in a type-safe manner.
 *
 * Being a simple reference wrapper around an existing type, the object member is
 * public and no accessor methods are provided.
 *
 * One case where this is used in Dragom is for returning Version.
 *
 * @author David Raymond
 */
public class ByReference<Type> {
	/**
	 * The referenced object.
	 */
	public Type object;

	@Override
	public String toString() {
		return "Reference: " + ((this.object == null) ? "null" : this.object.toString());
	}
}
