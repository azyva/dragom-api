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

import java.io.Reader;
import java.util.Map;

import org.azyva.dragom.model.Module;
import org.azyva.dragom.model.ModuleVersion;
import org.azyva.dragom.model.NodePath;
import org.azyva.dragom.model.Version;
import org.azyva.dragom.reference.ReferenceGraph;

/**
 * Abstracts the details of creating a job in Jenkins.
 * <p>
 * This plugin is used by {@link SetupJenkinsJobs} which takes care of the details
 * of communicating with Jenkins (base URL, security, where to create the job,
 * etc.). This plugin only handes the actual job configuration.
 *
 * @author David Raymond
 */
public interface JenkinsJobCreationPlugin extends ModulePlugin {
	/**
	 * Returns the full name of the template to use for creating the job, as in
	 * TopFolder/SubFolder/template. By template, we mean templates as implemented
	 * by the Template Plugin in CloudBees Jenkins Platform.
	 * <p>
	 * null is returned to indicate that the job must be created as a regular
	 * non-templatized job.
	 * <p>
	 * If null is not returned, {@link #getTemplateParams} is expected to be called to
	 * obtain the template parameters.
	 * <p>
	 * If null is returned, {@link #getConfig} is expected to be called to obtain the
	 * complete job configuration.
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
	Map<String, String> getTemplateParams(ReferenceGraph referenceGraph, Version version);

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
	 * @return
	 */
	Reader getConfig(ReferenceGraph referenceGraph, Version version);
}
