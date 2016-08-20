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

package org.azyva.dragom.model;

import org.azyva.dragom.model.config.Config;


/**
 * Represents the runtime model. This interface and its members are used during
 * the execution of tools.
 * <p>
 * An important member of a Model is the root ClassificationNode of the Model. But
 * Model offers useful methods that are more global than those offered by
 * {@link Node}, {@link ClassificationNode} and {@link Module}.
 * <p>
 * A Model and the child classes which implement this interface and child
 * interfaces will generally be based on {@link Config} and child
 * interfaces, although this is not strictly required.
 * <p>
 * The methods in this interface and child interfaces are similar to those in
 * Config and child interfaces, which may seem redundant. The idea is that Dragom
 * separates the Model from its Config so that Config implementations can be used
 * interchangeably using the same Model implementation.
 * <p>
 * A Model is essentially static in the sense that the properties of
 * Node's cannot be changed once the Model is created, except if the Model is a
 * {@link MutableModel}). However, a Model can implement
 * {@link ModelNodeBuilderFactory} if it allows new Node's to be dynamically
 * created. But once such new Node's are created, they become static as other
 * Node's.
 *
 * @author David Raymond
 */
public interface Model {
	/**
	 * @return Root ClassificationNode.
	 */
	Node getClassificationNodeRoot();

	/**
	 * Returns the {@link ClassificationNode} corresponding to a {@link NodePath}.
	 * <p>
	 * This method follows the path of ClassificationNode's given the specified
	 * NodePath until the leaf ClassificationNode of the NodePath is reached. If any
	 * ClassificationNode along the path does not exist, null is returned.
	 *
	 * @param nodePath NodePath of the ClassificationNode to return. Must be partial.
	 * @return ClassificationNode. null if no ClassificationNode corresponding to the
	 *   specified NodePath exist.
	 */
	ClassificationNode getClassificationNode(NodePath nodePath);

	/**
	 * Returns the {@link Module} corresponding to a {@link NodePath}.
	 * <p>
	 * This methods gets the parent {@link ClassificationNode} as described for
	 * {@link #getClassificationNode} ({@link NodePath#getPartialParentNodePath})
	 * and looks up the requested Module.
	 *
	 * @param nodePath NodePath of the Module to return. Must not be partial.
	 * @return Module. null if no Module corresponding to the specified NodePath
	 *   exist.
	 */
	Module getModule(NodePath nodePath);

	/**
	 * Finds and returns the {@link Module} whose build produces an
	 * {@link ArtifactGroupId}.
	 *
	 * @param artifactGroupId ArtifactGroupId for which to find a {@link Module}.
	 * @return Module. null if no Module found.
	 */
	Module findModuleByArtifactGroupId(ArtifactGroupId artifactGroupId);
}
