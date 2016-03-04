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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a List of ReferencePathMatcher which matches a ReferencePath if all
 * of the ReferencePathMatcher's within the List match, or, as a special case, if
 * the List is empty.
 *
 * @author David Raymond
 */
public class ReferencePathMatcherAnd implements ReferencePathMatcher {
	/**
	 * List of the ReferencePathMatcher.
	 */
	private List<ReferencePathMatcher> listReferencePathMatcher;

	/**
	 * Default constructor.
	 */
	public ReferencePathMatcherAnd() {
		this.listReferencePathMatcher = new ArrayList<ReferencePathMatcher>();
	}

	/**
	 * Returns the List of ReferencePathMatcher's.
	 *
	 * Currently this class does not provide a safe encapsulation of the List of
	 * ReferencePathMatcher's. Manipulation of the List other than adding a new
	 * ReferencePathMatcher needs to be done by modifying the List returned by this
	 * method.
	 *
	 * @return See description.
	 */
	public List<ReferencePathMatcher> getListReferencePathMatcher() {
		return this.listReferencePathMatcher;
	}

	/**
	 * Adds a ReferencePathMatcher to the List.
	 *
	 * @param referencePathMatcher ReferencePathMatcher.
	 */
	public void addReferencePathMatcher(ReferencePathMatcher referencePathMatcher) {
		this.listReferencePathMatcher.add(referencePathMatcher);
	}

	/**
	 * Verifies if a ReferencePathMatcherAnd matches a ReferencePath.
	 *
	 * A ReferencePathMatcherAnd matches a ReferencePath if all ReferencePathMatcher's
	 * within the List match the ReferencePath.
	 *
	 * @param referencePath ReferencePath.
	 * @return true if the ReferencePathMatcherAnd matches the ReferencePath.
	 */
	@Override
	public boolean matches(ReferencePath referencePath) {
		for (ReferencePathMatcher referencePathMatcher: this.listReferencePathMatcher) {
			if (!referencePathMatcher.matches(referencePath)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Verifies if the ReferencePathMatcherAnd can potentially match children of a
	 * ReferencePath.
	 *
	 * A ReferencePathMatcherAnd can match children of a ReferencePath if all
	 * ReferencePathMatcher's within the List can match children of the ReferencePath.
	 *
	 * @param referencePath ReferencePath.
	 * @return true if children of the ReferencePath can be matched by the
	 *   ReferencePathMatcherAnd.
	 */
	@Override
	public boolean canMatchChildren(ReferencePath referencePath) {
		for (ReferencePathMatcher referencePathMatcher: this.listReferencePathMatcher) {
			if (!referencePathMatcher.canMatchChildren(referencePath)) {
				return false;
			}
		}

		return true;
	}
}
