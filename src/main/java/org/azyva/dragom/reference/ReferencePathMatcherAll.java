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
 * Dummy ReferencePathMatcher that matches all ReferencePath's.
 *
 * This can be useful when a ReferencePathMatcher is not specified by the user
 * implying that all ReferencePath's need to be considered.
 *
 * @author David Raymond
 */
public class ReferencePathMatcherAll implements ReferencePathMatcher {
  /**
   * Verifies if the ReferencePathMatcherAll matches a ReferencePath.
   *
   * Always returns true.
   *
   * @param referencePath ReferencePath.
   * @return true.
   */
  @Override
  public boolean matches(ReferencePath referencePath) {
    return true;
  }

  /**
   * Verifies if the ReferencePathMatcherAll can potentially match children of a
   * ReferencePath.
   *
   * Always returns true.
   *
   * @param referencePath ReferencePath.
   * @return true.
   */
  @Override
  public boolean canMatchChildren(ReferencePath referencePath) {
    return true;
  }
}
