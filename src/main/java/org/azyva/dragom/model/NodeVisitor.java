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

package org.azyva.dragom.model;



/**
 * Used by {@link ClassificationNode#traverseNodeHierarchy} while traversing
 * the {@link Node} hierarchy rooted at a {@link ClassificationNode}.
 *
 * @author David Raymond
 */

public interface NodeVisitor {
  /**
   * Action during the traversal.
   */
  public enum VisitAction {
    /**
     * Indicates we are stepping into a {@link Node} during the traversal. A
     * matching {@link #STEP_OUT} will occur when the traversal of the Node is
     * complete.
     */
    STEP_IN,

    /**
     * Indicates we are stepping out of a {@link Node} during the traversal. A
     * matching {@link #STEP_IN} will have occurred previously.
     */
    STEP_OUT,

    /**
     * Represents the actual visit of the {@link Node}. A {@link #STEP_IN} will have
     * occurred previously for the parent Node. But if traversal is depth-first
     * STEP_IN for all the Node in the {@link NodePath} will have also occurred with
     * no intervening visit for the intermediate Node's.
     * <p>
     * {@link #STEP_OUT} will occur thereafter for the parent Node, after having
     * visited all of its child Node's.
     */
    VISIT
  }

  /**
   * Controls the subsequent visit actions during a traversal. This is the type
   * returned by {@link NodeVisitor#visitNode}.
   */
  public enum VisitControl {
    /**
     * Continue.
     */
    CONTINUE,

    /**
     * Aborts the traversal.
     */
    ABORT,

    /**
     * Skip the children and continue.
     * <p>
     * Not valid when {@link NodeVisitor.VisitAction#STEP_OUT}.
     * <p>
     * Not valid when {@link NodeVisitor.VisitAction#VISIT} for depth-first traversal.
     */
    SKIP_CHILDREN,

    /**
     * Skip the current base {@link NodePath} and continue with the next one, if any.
     */
    SKIP_CURRENT_BASE
  }

  /**
   * Called for each {@link Node} during traversal.
   *
   * @param node Node visited.
   * @param visitAction VisitAction.
   * @return VisitControl.
   */
  VisitControl visitNode(VisitAction visitAction, Node node);
}
