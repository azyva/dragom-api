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

import java.nio.file.Path;
import java.util.List;

import org.azyva.dragom.apiutil.ByReference;
import org.azyva.dragom.model.ArtifactVersion;
import org.azyva.dragom.model.Version;
import org.azyva.dragom.reference.Reference;

/**
 * Manages the references a module has to other modules within its sources.
 *
 * References and dependencies are similar, and this plugin could very well have
 * been named DependencyManagerPlugin. But the name ReferenceManagerPlugin
 * reflects the fact that a module has references to other modules, but that these
 * references are not necessarily dependencies. For example in a Maven module, the
 * <dependencyManagement> section of the POM contains references to other modules,
 * but these are dependencies only if the POM also includes a reference to the
 * module in the <dependencies> section. Similarly, in the <dependencies> section,
 * if a module is identified without a version, that is not considered a reference
 * from the point of view of this plugin.
 *
 * Only direct references are managed by this plugin. If a module A references
 * another module B which in turn references module C, module C's references are
 * not considered by this plugin, although they would probably be by tools like
 * Maven (transitive dependencies). Transitive references are not useful in the
 * context of Dragom.
 *
 * References can be at the artifact or the source level. References at the source
 * level (in which case the ArtifactGroupId and artifact version is not included)
 * must always be to modules known to Dragom. Otherwise would be a configuration
 * error in the model. This is logical since source-level references imply that
 * the sources of the referenced module are required for building modules
 * referencing it.
 *
 * References at the artifact level can be to modules unknown to Dragom. In such a
 * case the reference will not include a NodePath or a Version.
 *
 * Note that version expressed using the class Version are considered to be
 * source-level versions, even though they are not simple strings and provide some
 * level of abstraction. The reason is that the mapping between them is handled
 * internally in the ScmPlugin for the module.
 *
 * The way references of a module are expressed depends on implementation
 * details of the module, which includes, but is not limited, to the build tool
 * used. Different implementations of this plugin support these differing
 * implementation details.
 *
 * If Maven is used as the build tool, references will be expressed within the POM
 * file of the module. If the module is an aggregator module with submodules, the
 * aggregator module and the submodules are viewed as a single module from the
 * point of view of Dragom and the references managed by this plugin include those
 * of the aggregator module as well as those of its submodules and are presented
 * in a ways that is abstract to the caller, since aggregator are implementation
 * detail of the module.
 *
 * @author David Raymond
 */
//TODO: Explain the fact that references can be within the model or external.
//In the case of external, they cannot be identified with NodePath.
//This will likely be useful for external library reference convergence.
public interface ReferenceManagerPlugin extends ModulePlugin {
	List<Reference> getListReference(Path pathModuleWorkspace);
	//TODO: Probably have overloaded methods that can filter the returned references.
	//But not too many overloads since they would need to be implemented by all plugins.
	//Maybe better to have fields in references that would allow the caller to easily filter
	//A path to the module workspace is required. It is the caller's responsibility to provide it.

	/**
	 * Updates the version of a reference using a global version.
	 *
	 * The reference must have been returned by the getListReference method of the
	 * same plugin instance (for the same module, obviously).
	 *
	 * The reference must match exactly a reference within the module, including the
	 * version. This ensures that what the caller requests to modify is what will
	 * really be modified. Said in another way, the module should generally not have
	 * been modified between the call to getListReference that returned the reference
	 * and the call to this method.
	 *
	 * The reference must be at the source level or must include a source-level
	 * component.
	 *
	 * Note that if references are managed at the artifact level within the Module the
	 * ArtifactVersionMapperPlugin may be used to map the specified Version to an
	 * ArtifactVersion and it can happen that the ArtifactVersion is not changed even
	 * though the source-level Version reference is different. In such a case, false
	 * is returned.
	 *
	 * @param pathModuleWorkspace Path to the module within the workspace.
	 * @param reference Reference to modify.
	 * @param version New version.
	 * @param byReferenceReference The new Reference as would be returned by
	 *   {@link #getListReference} will be stored there. Can be null.
	 * @return Indicates if the reference was really updated. false is returned if it
	 *   already had the specified value.
	 */
	boolean updateReferenceVersion(Path pathModuleWorkspace, Reference reference, Version version, ByReference<Reference> byReferenceReference);

	/**
	 * Updates the version of a reference using an artifact version.
	 *
	 * The reference must have been returned by the getListReference method of the
	 * same plugin instance (for the same module, obviously).
	 *
	 * The reference must match exactly a reference within the module, including the
	 * version. This ensures that what the caller requests to modify is what will
	 * really be modified. Said in another way, the module should generally not have
	 * been modified between the call to getListReference that returned the reference
	 * and the call to this method.
	 *
	 * The reference must be at the artifact level or must include an artifact level
	 * component.
	 *
	 * @param pathModuleWorkspace Path to the module within the workspace.
	 * @param reference Reference to modify.
	 * @param artifactVersion New ArtifactVersion.
	 * @param byReferenceReference The new Reference as would be returned by
	 *   {@link #getListReference} will be stored there. Can be null.
	 * @return Indicates if the reference was really updated. false is returned if it
	 *   already had the specified value.
	 */
	boolean updateReferenceArtifactVersion(Path pathModuleWorkspace, Reference reference, ArtifactVersion artifactVersion, ByReference<Reference> byReferenceReference);
}
