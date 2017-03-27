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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a List of ReferencePathMatcher which matches a ReferencePath if any
 * of the ReferencePathMatcher within the List matches, or, as a special case, if
 * the List is empty.
 *
 * @author David Raymond
 */
public class ReferencePathMatcherOr implements ReferencePathMatcher {
  /**
   * List of the ReferencePathMatcher.
   */
  private List<ReferencePathMatcher> listReferencePathMatcher;

  /**
   * Default constructor.
   */
  public ReferencePathMatcherOr() {
    this.listReferencePathMatcher = new ArrayList<ReferencePathMatcher>();
  }

  /**
   * Returns the List of ReferencePathMatcher's.
   *
   * Currently this class does not provide a safe encapsulation of the List of
   * ReferencePathMatcher's. Manipulation of the List other than adding a new
   * ReferencePathMatcher needs to be done by modifying the List returned by
   * this method.
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

  @Override
  public boolean matches(ReferencePath referencePath) {
    if (this.listReferencePathMatcher.size() == 0) {
      return true;
    }

    for (ReferencePathMatcher referencePathMatcher: this.listReferencePathMatcher) {
      if (referencePathMatcher.matches(referencePath)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean canMatchChildren(ReferencePath referencePath) {
    if (this.listReferencePathMatcher.size() == 0) {
      return true;
    }

    for (ReferencePathMatcher referencePathMatcher: this.listReferencePathMatcher) {
      if (referencePathMatcher.canMatchChildren(referencePath)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean matchesAllChildren(ReferencePath referencePath) {
    if (this.listReferencePathMatcher.size() == 0) {
      return true;
    }

    for (ReferencePathMatcher referencePathMatcher: this.listReferencePathMatcher) {
      if (referencePathMatcher.matchesAllChildren(referencePath)) {
        return true;
      }
    }

    return false;
  }
}
