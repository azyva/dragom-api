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

package org.azyva.dragom.reference;


/**
 * ReferencePathMatcher that matches a ReferencePath if its inner
 * ReferencePathMatcher does not.
 *
 * @author David Raymond
 */
public class ReferencePathMatcherNot implements ReferencePathMatcher {
	private ReferencePathMatcher referencePathMatcher;

	/**
	 * Constructor.
	 */
	public ReferencePathMatcherNot(ReferencePathMatcher referencePathMatcher) {
		this.referencePathMatcher = referencePathMatcher;
	}

	/**
	 * @return Inner ReferencePathMatcher.
	 */
	public ReferencePathMatcher getReferencePathMatcher() {
		return this.referencePathMatcher;
	}

	public void setReferencePathMatcher(ReferencePathMatcher referencePathMatcher) {
		this.referencePathMatcher = referencePathMatcher;
	}

	/**
	 * Verifies if the ReferencePathMatcherNot matches a ReferencePath.
	 *
	 * A ReferencePathMatcherNot matches a ReferencePath its inner
	 * ReferencePathMatcher does not.
	 *
	 * @param referencePath ReferencePath.
	 * @return true if the ReferencePathMatcherNot matches the ReferencePath.
	 */
	@Override
	public boolean matches(ReferencePath referencePath) {
		return !this.referencePathMatcher.matches(referencePath);
	}

	/**
	 * Verifies if the ReferencePathMatcherNot can potentially match children of a
	 * ReferencePath.
	 *
	 * A ReferencePathMatcherNot can match children of a ReferencePath if its inner
	 * ReferencePathMatcher cannot.
	 *
	 * @param referencePath ReferencePath.
	 * @return true if children of the ReferencePath can be matched by the
	 *   ReferencePathMatcheNot.
	 */
	@Override
	public boolean canMatchChildren(ReferencePath referencePath) {
		return !this.referencePathMatcher.canMatchChildren(referencePath);
	}
}
