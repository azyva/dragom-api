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

package org.azyva.dragom.model.plugin;

import java.util.List;

import org.azyva.dragom.model.ArtifactGroupId;
import org.azyva.dragom.model.NodePath;

/**
 * Helps in finding a module whose build produces a given ArtifactGroupId.
 *
 * Such a plugin is in theory not strictly required since finding a module whose
 * build produces a given ArtifactGroupId can be done by visiting every module in
 * the hierarchy and using its ArtifactInfoPlugin to ask it whether its build
 * produces the given ArtifactGroupId.
 *
 * However in order to support dynamically created modules, classification nodes
 * must be able to determine which child Module, that may not already be
 * created, may produce the given ArtifactGroupId in their builds presumably based
 * on the NodePath of the classification node. But since classification nodes
 * should not generally know about exceptions, a module whose build exceptionally
 * produces an ArtifactGroupId should be defined explicitly in the hierarchy and
 * not be created dynamically.
 *
 * Also, limiting the tree traversal to classification nodes is a useful
 * optimization.
 *
 * @author David Raymond
 */
public interface FindModuleByArtifactGroupIdPlugin extends ClassificationNodePlugin {
	/**
	 * Returns the (NodePath of the) modules whose build possibly produce an
	 * ArtifactGroupId.
	 *
	 * The reason for returning module NodePath's is that the modules may not be
	 * created and it is the responsibility of the caller to attempt to create the
	 * modules, which may not even exist (because they do not exist in the SCM).
	 *
	 * The build of the Module returned do not necessarily produce the given
	 * ArtifactGroupId. This must be verified the ArtifactInfoPlugin of the Module.
	 * The ClassificationNode cannot know for sure if the build of a Module produces a
	 * given ArtifactGroupId.
	 *
	 * If the ClassificationNode does not know of any Module whose build may produce
	 * the given ArtifactGroupId, either null or an empty List can be equivalently
	 * returned.
	 *
	 * Generally the modules returned should be immediate children of the
	 * classification node associated with the plugin. But this is not mandatory.
	 *
	 * @param artifactGroupId ArfactGroupId.
	 * @return See description.
	 */
	List<NodePath> getListModulePossiblyProduceArtifactGroupId(ArtifactGroupId artifactGroupId);
}
