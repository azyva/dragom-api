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

import java.io.Reader;
import java.util.Map;

import org.azyva.dragom.model.Module;
import org.azyva.dragom.model.ModuleVersion;
import org.azyva.dragom.model.NodePath;
import org.azyva.dragom.model.Version;
import org.azyva.dragom.reference.ReferenceGraph;

/**
 * Abstracts the mechanism for obtaining the information about the Jenkins job
 * corresponding to the module.
 * <p>
 * This plugin is used by SetupJenkinsJobs from dragom-core. SetupJenkinsJobs
 * takes care of interacting with Jenkins for creating jobs (and folders). It
 * takes care of the Jenkins base URL and credentials. It manages persisting the
 * items that are created so that jobs can be created and updated across multiple
 * invocations while maintaining a common context. These are considered common
 * services which do not need to be abstracted.
 * <p>
 * This plugin takes care of providing the information about a Jenkins job to
 * SetupJenkinsJobs, such as the job's full name and its configuration data. These
 * are specific to each situation and need to be abstracted into a plugin.
 * <p>
 * Jenkins jobs can often be created in different contexts. If this is the case,
 * then it is up to the implementations of this plugin to support that such a
 * context be specified externally, such as with a runtime property, and that
 * different parameters be associated with the various contexts.
 *
 * @author David Raymond
 */
public interface JenkinsJobInfoPlugin extends ModulePlugin {
	/**
	 * Returns the full name of the job.
	 * <p>
	 * This is used by SetupJenkinsJobs from dragom-core to know which job to create.
	 * It can also be used by implementations themselves of this plugin to obtain the
	 * downstream jobs that handle building {@link Module}'s which depend on the
	 * current Module so that the configuration of the latter can, if appropriate,
	 * contain these references.
	 *
	 * @param versionDynamic Dynamic Version of the ModuleVersion for which a job must
	 *   be created. A ModuleVersion is not passed as a parameter since the
	 *   {@link NodePath} in the ModuleVersion would be redundant with the NodePath of
	 *   the {@link Module} to which the instance of this plugin is associated.
	 * @return See description.
	 */
	String getJobFullName(Version versionDynamic);

	/**
	 * @return Indicates if the parent folder in the job full name returned by
	 *   {@link #getJobFullName} is not static, is specific to some projects and could
	 *   not exist and need to be created.
	 */
	boolean isHandleParentFolderCreation();

	/**
	 * Returns the full name of the template to use for creating the job, as in
	 * TopFolder/SubFolder/template. By template, we mean a template as implemented
	 * by the Template Plugin in CloudBees Jenkins Platform.
	 * <p>
	 * null is returned to indicate that the job must be created as a regular
	 * non-templatized job.
	 * <p>
	 * If null is not returned, {@link #getMapTemplateParam} is expected to be called to
	 * obtain the template parameters.
	 * <p>
	 * If null is returned, {@link #getReaderConfig} is expected to be called to
	 * obtain the complete job configuration.
	 *
	 * @return See description.
	 */
	String getTemplate();

	/**
	 * Returns a Map of the parameters to use with the template returned by
	 * {@link getTemplate} to create a job.
	 * <p>
	 * If getTemplate returns null, this method will not be called.
	 *
	 * @param referenceGraph ReferenceGraph which contains the {@link ModuleVersion}
	 *   for which a job must be created.
	 * @param version Version of the ModuleVersion for which a job must be created. A
	 *   ModuleVersion is not passed as a parameter since the {@link NodePath} in the
	 *   ModuleVersion would be redundant with the NodePath of the {@link Module} to
	 *   which the instance of this plugin is associated.
	 * @return See description.
	 */
	Map<String, String> getMapTemplateParam(ReferenceGraph referenceGraph, Version version);

	/**
	 * Returns a Reader from which the XML configuration of the job to be created can
	 * be read.
	 * <p>
	 * If {@link getTemplate} does not return null, this method will not be called.
	 * <p>
	 * Providing the XML configuration as a Reader seems to be the most flexible way
	 * to return it, and allow the implementation to have control over the efficiency
	 * in handling the XML data.
	 *
	 * @param referenceGraph ReferenceGraph which contains the {@link ModuleVersion}
	 *   for which a job must be created.
	 * @param version Version of the ModuleVersion for which a job must be created. A
	 *   ModuleVersion is not passed as a parameter since the {@link NodePath} in the
	 *   ModuleVersion would be redundant with the NodePath of the {@link Module} to
	 *   which the instance of this plugin is associated.
	 * @return See description.
	 */
	Reader getReaderConfig(ReferenceGraph referenceGraph, Version version);
}
