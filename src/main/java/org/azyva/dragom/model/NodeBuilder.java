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
 * Allows building a {@link Node} within a {@link Model} which can be completed
 * dynamically.
 * <p>
 * This is a super-interface of {@link ModuleBuilder} and
 * {@link ClassificationNodeBuilder}.
 * <p>
 * There are similarities between the functionality provided by this interface and
 * that provided by {@link MutableNode} and its sub-interfaces
 * {@link MutableClassificationNode} and {@link MutableModule}. They both allow
 * new Node's to be created. But MutableNode and its sub-interfaces are related to
 * changing the configuration data for a {@link Model}, whereas the new Node's
 * created by this interface remain dynamic and not supported by underlying
 * configuration data.
 * <p>
 * Maybe these two functionalities could eventually be supported with only one set
 * of interfaces. But for now, they are kept separate.
 *
 * @param <NodeSubType> {@link Node} subclass.
 * @author David Raymond
 */
public interface NodeBuilder<NodeSubType extends Node> {
  /**
   * Sets the parent {@link ClassificationNode} of the {@link Node}.
   *
   * @param classificationNodeParent Parent ClassificationNode.
   * @return This NodeBuilder.
   */
  NodeBuilder<NodeSubType> setClassificationNodeParent(ClassificationNode classificationNodeParent);

  /**
   * Sets the name of the {@link Node}.
   *
   * @param name Name.
   * @return This NodeBuilder.
   */
  NodeBuilder<NodeSubType> setName(String name);

  /**
   * Sets a property of the {@link Node}.
   *
   * @param name Property name.
   * @param value Property value.
   * @param indOnlyThisNode Indicates if the property applies only to this Node, as
   *   opposed to descendent Node's as well.
   * @return This NodeBuilder.
   */
  NodeBuilder<NodeSubType> setProperty(String name, String value, boolean indOnlyThisNode);


  /**
   * Returns the {@link Node}, but partially created.
   *
   * <p>Useful to validate the NodeBuilder before officially creating the Node.
   *
   * @return Partial {@link Node}.
   */
  NodeSubType getPartial();

  /**
   * @return Created Node.
   */
  NodeSubType create();
}
