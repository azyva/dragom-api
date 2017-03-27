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
 * ReferencePathMatcher that matches a ReferencePath if its inner
 * ReferencePathMatcher does not.
 *
 * @author David Raymond
 */
public class ReferencePathMatcherNot implements ReferencePathMatcher {
  /**
   * ReferencePathMatcher.
   */
  private ReferencePathMatcher referencePathMatcher;

  /**
   * Constructor.
   *
   * @param referencePathMatcher ReferencePathMatcher.
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

  /**
   * @param referencePathMatcher ReferencePathMatcher.
   */
  public void setReferencePathMatcher(ReferencePathMatcher referencePathMatcher) {
    this.referencePathMatcher = referencePathMatcher;
  }

  @Override
  public boolean matches(ReferencePath referencePath) {
    return !this.referencePathMatcher.matches(referencePath);
  }

  /**
   * A ReferencePathMatcherNot can match children only if the inner
   * ReferencePathMatcher does not match all children.
   *
   * @param referencePath ReferencePath.
   * @return true if the ReferencePathMatcher can match children of the
   *   ReferencePath.
   */
  @Override
  public boolean canMatchChildren(ReferencePath referencePath) {
    return !this.referencePathMatcher.matchesAllChildren(referencePath);
  }

  /**
   * A ReferencePathMatcherNot matches all children only if the inner
   * ReferencePathMatcher cannot match any.
   *
   * @param referencePath ReferencePath.
   * @return true if the ReferencePathMatcher matches all children of the
   *   ReferencePath.
   */
  @Override
  public boolean matchesAllChildren(ReferencePath referencePath) {
    return !this.referencePathMatcher.canMatchChildren(referencePath);
  }
}
