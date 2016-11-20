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

package org.azyva.dragom.reference;

/**
 * Interface for ReferencePath matchers.
 *
 * The main implementation of this interface is provided by
 * ReferencePathMatcherByElement. But other implementations exist to provide
 * boolean arithmetics with ReferencePathMatcher's:
 *
 * - ReferencePathMatcherAnd
 * - ReferencePathMatcherOr
 * - ReferencePathMatcherNot
 *
 * @author David Raymond
 */
public interface ReferencePathMatcher {
	/**
	 * Verifies if a ReferencePathMatcher matches a ReferencePath.
	 *
	 * @param referencePath ReferencePath.
	 * @return true if the ReferencePath is matched by the ReferencePathMatcher.
	 */
	public boolean matches(ReferencePath referencePath);

	/**
	 * Verifies if a ReferencePathMatcher can potentially match children of a
	 * ReferencePath.
	 *
	 * @param referencePath ReferencePath.
	 * @return true if the ReferencePath is matched by the ReferencePathMatcher.
	 */
	public boolean canMatchChildren(ReferencePath referencePath);
}
