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

import java.util.List;

import org.azyva.dragom.model.config.NodeType;

/**
 * Represents a classification node at runtime.
 *
 * @author David Raymond
 */
public interface ClassificationNode extends Node {
  /**
   * @return List of all the child {@link Node}'s.
   */
  List<Node> getListChildNode();

  /**
   * Returns a child {@link Node}.
   *
   * @param name Name of the child Node.
   * @return Child Node. null if no child of the specified name is currently
   *   defined.
   */
  Node getNodeChild(String name);

  /**
   * Traverses the Node hierarchy rooted at this ClassificationNode.
   *
   * @param nodeTypeFilter NodeType to visit. If null, all NodeType are visited.
   * @param indDepthFirst Indicates to perform a depth-first traversal instead of
   *   parent-first.
   * @param nodeVisitor {@link NodeVisitor#visitNode} is called for each Node
   *   visited.
   * @return NodeVisitor.VisitControl. NodeVisitor.VisitControl.CONTINUE,
   *   NodeVisitor.VisitControl.ABORT and NodeVisitor.VisitControl.SKIP_CURRENT_BASE
   *   can be returned to indicate how the traversal ended.
   */
  NodeVisitor.VisitControl traverseNodeHierarchy(NodeType nodeTypeFilter, boolean indDepthFirst, NodeVisitor nodeVisitor);
}
