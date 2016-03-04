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

package org.azyva.dragom.model.plugin;

import org.azyva.dragom.model.ClassificationNode;
import org.azyva.dragom.model.Model;
import org.azyva.dragom.model.Module;
import org.azyva.dragom.model.config.Config;

/**
 * This {@link ClassificationNodePlugin} allows the {@link Model} to evolve
 * dynamically at runtime, generally based on information obtained from an
 * external system such as a SCM.
 * <p>
 * The idea is that configuring and maintaining the configuration for each
 * {@link Module} individually using {@link Config} and its members can be tedious
 * for an organization having many Module's. Using an implementation of
 * UndefinedDescendantNodeManagerPlugin, and indirectly
 * {@link FindModuleByArtifactGroupIdPlugin} and {@link ArtifactInfoPlugin}, it is
 * possible to only configure the main {@link ClassificationNode}'s of the Module
 * hierarchy and let the plugin dynamically add new requested ClassificationNode's
 * and Module's by validating their existence in an external system such as a SCM.
 *
 * @author David Raymond
 */
public interface UndefinedDescendantNodeManagerPlugin extends ClassificationNodePlugin {
	/**
	 * Requests that a {@link ClassificationNode} be dynamically created.
	 * <p>
	 * null must be returned to indicate that the requested ClassificationNode cannot
	 * be created because it does not exist or any other reason.
	 * <p>
	 * If a ClassificationNode is returned, it must be completely created, installed
	 * within the {@link Model} (using {@link ClassificationNode#addChildNode}) and
	 * ready to use.
	 *
	 * @param name Name of the ClassificationNode.
	 * @return Newly created ClassificationNode. null if the ClassificationNode cannot
	 *   be created.
	 */
	ClassificationNode requestClassificationNode(String name);

	/**
	 * Requests that a {@link Module} be dynamically created.
	 * <p>
	 * null must be returned to indicate that the requested Module cannot be created
	 * because it does not exist or any other reason.
	 * <p>
	 * If a Module is returned, it must be completely created, installed within the
	 * {@link Model} (using {@link ClassificationNode#addChildNode}) and ready to use.
	 *
	 * @param name Name of the Module.
	 * @return Newly created Module. null if the Module cannot be created.
	 */
	Module requestModule(String name);
}
