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

import java.io.Writer;
import java.nio.file.Path;

import org.azyva.dragom.execcontext.plugin.UserInteractionCallbackPlugin;
import org.azyva.dragom.model.Module;

/**
 * Allows building (the source code of) a {@link Module}.
 * <p>
 * The notion of build is broad. It is up to implementations of this plugin to define
 * exactly what is a build or what happens during a build.
 * <p>
 * In particular the build of a Module generally involves taking source files and
 * generating other files from them (compiling, packaging, etc.). But it can also
 * involve executing unit tests on the compiled code after building. This can even
 * very from Module to Module for the same plugin implementation. These different
 * facets of builds are not exposed by this plugin.
 * <p>
 * Generally builds generate files from source files and the concept of cleaning
 * the build environment for a Module makes sense. When this is so the {@link
 * #isCleanSupported} should return true and {@link #isSomethingToClean} and
 * {@link #clean} should be be implemented.
 * <p>
 * Generally the build process generates a log. {@link #build} accepts a Writer
 * allowing the plugin implementation to provide this log if desired. Build tools
 * are expected to provide this Writer and in turn provide the generated log to the
 * user, often at the console. The log can also be generated by the build process
 * within a log file within the Module workspace directory. It is up to the plugin
 * implementation to decide about whether to provide the build log through the
 * Writer or a file, or both.
 * <p>
 * {@link UserInteractionCallbackPlugin} must not be used during the build (or
 * clean) process since the provided Writer will generally have been obtained from
 * {@link UserInteractionCallbackPlugin#provideInfoWithWriter} and using
 * UserInteractionCallbackPlugin directly would violate its contract.
 * <p>
 * Generally a build executes within a process of its own so that not to be
 * dependent on the Dragom execution environnement.
 * <p>
 * Builds are expected to be non interactive, or at least not expect user input to
 * be provided via some standard input stream to the build process. If user input
 * is required during a build (and it is not possible to provide the information
 * using input parameters to the build process), it is the responsibility of the
 * plugin implementation to handle the situation typically by monitoring the
 * output stream of the build process and providing the required information
 * programmatically to its input stream. The infomration can be obtained from the
 * user, but using facilities provided by the framework such as
 * {@link UserInteractionCallbackPlugin}.
 *
 * @author David Raymond
 */
public interface BuilderPlugin extends ModulePlugin {
	/**
	 * Indicates if the {@link Module} has source files that are more recent than
	 * files generated by the previous build, or if the build of the Module would
	 * generate new files.
	 *
	 * @param pathModuleWorkspace Path to the Module within the workspace.
	 * @return Indicates if there is something to build.
	 */
	boolean isSomethingToBuild(Path pathModuleWorkspace);

	/**
	 * Builds the {@link Module}, generating files from the source files.
	 * <p>
	 * The buildContext parameter allows callers to give some indication of the
	 * context in which the build is performed. Here are examples of possible build
	 * contexts:
	 * <p>
	 * <li>new-static-version: When the Module is built before creating a new static
	 *     version using {@link CreateStaticVersionTool};</li>
	 * <li>build: When the Module is build by {@link BuildTaskPluginImpl}.</li>
	 * <p>
	 * It is up to the plugin implementation whether and how to take the context
	 * into consideration. See {@link MavenBuilderPluginImpl} for an example.
	 *
	 * @param pathModuleWorkspace Path to the Module within the workspace.
	 * @param buildContext Build context. Can be null.
	 * @param writerLog Writer where the log of the build process can be written. Can
	 *   be null if the caller is not interested in the log. The Writer must not be
	 *   closed, giving the caller control over its lifecycle.
	 * @return Indicates if the build process was successful.
	 */
	boolean build(Path pathModuleWorkspace, String buildContext, Writer writerLog);

	/**
	 * @return Indicates if the {@link Module} supports the notion of cleaning,
	 *   regardless of its workspace directory.
	 */
	boolean isCleanSupported();

	/**
	 * Indicates if there is something (build-related) to clean in the workspace
	 * directory for the {@link Module}.
	 *
	 * @param pathModuleWorkspace Path to the Module within the workspace.
	 * @return Indicates if there is something to clean.
	 */
	boolean isSomethingToClean(Path pathModuleWorkspace);

	/**
	 * Cleans the workspace directory of the Module by removing files that are
	 * generated by builds of the Module, was opposed to source files.
	 *
	 * @param pathModuleWorkspace Path to the Module within the workspace.
	 * @param writerLog Writer where the log of the clean process can be written. Can
	 *   be null if the caller is not interested in the log. The Writer must not be
	 *   closed, giving the caller control over its lifecycle.
	 * @return Indicates if the clean process was successful.
	 */
	boolean clean(Path pathModuleWorkspace, Writer writerLog);
}
