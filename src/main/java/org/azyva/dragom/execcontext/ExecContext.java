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

package org.azyva.dragom.execcontext;

import java.util.Set;

import org.azyva.dragom.execcontext.plugin.ExecContextPlugin;
import org.azyva.dragom.execcontext.plugin.UserInteractionCallbackPlugin;
import org.azyva.dragom.execcontext.plugin.WorkspacePlugin;
import org.azyva.dragom.model.Model;

/**
 * Provides contextual information and behavior during the execution of tools.
 *
 * <p>Classes generally use ExecContextHolder from dragom-core to retrieve an
 * ExecContext when needed.
 *
 * <p>The initialization phase of tools generally use {@link ExecContextFactory}
 * to instantiate or retrieve existing ExecContext's that will be set within
 * ExecContextHolder and used throughout the tool execution.
 * DefaultExecContextFactory from dragom-core is an implementation of
 * ExecContextFactory.
 *
 * <p>Although it is not entirely explicit, ExecContext's conceptually manage 3
 * information scopes:
 * <ul>
 * <li>Global: Information that is considered constant among workspaces and tool
 *     invocations. Global information essentially includes the
 *     {@link Model};
 * <li>Workspace: Information that is specific to a workspace. Such information
 *     can be changed during the execution of tools and is expected to be
 *     persisted within the workspace. Workspace information includes the
 *     non-transient properties ({@link #getProperty}, etc.). It also includes any
 *     information which
 *     {@link ExecContextPlugin}'s decide to manage with workspace scope;
 * <li>Tool: Information that is local to a tool execution. Tools and plugins that
 *     execute within the context of a tool can manage such information which is
 *     expected to be released when the tool execution ends. Tool information
 *     includes the transient data ({@link #getTransientData}, etc.). It
 *     also includes any information which ExecContextPlugin's decide to manage
 *     with tool scope.
 * </ul>
 * The distinction between the various scopes can be somewhat blurry, especially
 * if each tool execution is performed in its own JVM, which is often the case. In
 * such a case, it can be argued to some extent that all information is transient
 * given that when the JVM terminates, all information vanishes with it, at least
 * from memory. But it is possible to configure multiple tool executions to share
 * a JVM instance which would behave as a background service with which a front-
 * end would interact to start tool executions
 * (<a href="http://www.martiansoftware.com/nailgun/">NaigGun</a> can be useful in
 * that regard). In such cases the distinction between scopes become clearer. In
 * particular, it is more obvious that care must be taken to distinguish between
 * workspace and tool scopes as even if the JVM is not terminated at the end of a
 * tool execution, another tool execution must not see the tool-scope properties.
 * Such sharing of a single JVM instance can be done to improve tool
 * initialization time (JVM startup, Model initialization, etc.).
 * <p>
 * ExecContext provide access to ExecContextPlugin's
 * ({@link #getExecContextPlugin}). During tool execution, the tool and
 * {org.azyva.dragom.model.plugin.NodePlugin} collaborate to provide the required
 * Functionality. But in doing so they need to have access to the various aspects
 * of the ExecContext which, apart from general workspace properties, are provided
 * by plugins, such as {@link WorkspacePlugin} for workspace directories and
 * {@link UserInteractionCallbackPlugin}) for user interaction.
 * <p>
 * A relation between ExecContextPlugin's and information scope exists. See
 * {@link ToolLifeCycleExecContext} for more information.
 * <p>
 * ExecContextPlugin's can access workspace properties (getProperty, etc.) and
 * transient data (getTransientData, etc.), and there is no explicit and enforced
 * relation between an ExecContextPlugin scope and the scope of information it
 * accessed. However, it is the plugin implementation's responsibility to remain
 * coherent. For example an ExecContextPlugin bound to the workspace can access
 * transient data during its execution, but this data should not affect its
 * internal state. Otherwise, a tool execution could unpredictably affect the
 * behavior of another tool execution.
 * <p>
 * Generally a workspace is understood to correspond to a file system directory,
 * and it probably always will be the case. However, ExecContext does not make
 * this assumption. This is up to the ExecContextFactory implementation.
 * <p>
 * An ExecContext implementation that supports the concept of workspace directory
 * should implement {@link WorkspaceExecContext} and its corresponding
 * ExecContextFactory should implement {@link WorkspaceExecContextFactory}.
 * <p>
 * But one important ExecContextPlugin is {@link WorkspacePlugin} which does
 * assume that a workspace corresponds to a directory. So WorkspacePlugin
 * implementations will generally make assumptions about ExecContext
 * implementations' support for the concept of workspace directory. Other
 * ExecContextPlugin can also make such assumptions as required.
 * <p>
 * To indicate that an ExecContext implementation supports the workspace directory
 * concept it should implement {@link WorkspaceExecContext}, on top of
 * ExecContext. Similarly, the corresponding ExecContextFactory should implement
 * {@link WorkspaceExecContextFactory}.
 * <p>
 * In such a case where the ExecContext implementation supports the concept of
 * workspace directory, the workspace properties are expected to be stored in a
 * Properties file within the workspace directory.
 * <p>
 * Workspace properties and transient data are not handled symmetrically.
 * Workspace properties are simple String's since they are meant to be persisted
 * within the workspace. Granted, Object's can also be persisted (if they are
 * serializable), but it was decided to support only String properties for the
 * sake of simplicity and human interaction. However transient data does not need
 * to be persisted and so are general Object's, allowing tools and plugins to
 * store arbitrary data in tool scope.
 *
 * @author David Raymond
 */
public interface ExecContext {
	/**
	 * Returns the {@link Model} Model.
	 * <p>
	 * The Model is at global scope. During the configuration of the ExecContext
	 * during a tool initialization, a shared instance Model can be set (if
	 * tool executions share a single JVM instance).
	 *
	 * @return See description.
	 */
	Model getModel();

	/**
	 * Returns an {@link ExecContextPlugin}.
	 * <p>
	 * ExecContextPlugin's are identified by their (interface) class.
	 *
	 * @param <ExecContextPluginInterface> Interface of the ExecContextPlugin.
	 * @param classExecContextPluginInterface Class of the ExecContextPlugin.
	 * @return ExecContextPlugin. null if not found.
	 */
	<ExecContextPluginInterface extends ExecContextPlugin> ExecContextPluginInterface getExecContextPlugin(Class<ExecContextPluginInterface> classExecContextPluginInterface);

	/**
	 * @return Set of all initialization properties.
	 */
	Set<String> getSetInitProperty();

	/**
	 * Returns an initialization property.
	 *
	 * @param name Name of the property.
	 * @return Value of the property.
	 */
	String getInitProperty(String name);

	/**
	 * Returns a workspace property.
	 *
	 * @param name Name of the property.
	 * @return Value of the property.
	 */
	String getProperty(String name);

	/**
	 * Sets a workspace property.
	 *
	 * @param name Name of the property.
	 * @param value Value of the property. Can be null, in which case the behavior is
	 *   expected to be identical to {@link #removeProperty}.
	 */
	void setProperty(String name, String value);

	/**
	 * Returns a Set of all workspace property names having a given prefix.
	 *
	 * @param prefix Prefix of the properties. Can be null in which case all
	 *   properties are returned.
	 * @return See description.
	 */
	Set<String> getSetProperty(String prefix);

	/**
	 * Removes a workspace property.
	 *
	 * @param name Name of the property.
	 */
	void removeProperty(String name);

	/**
	 * Removes all properties having a given prefix.
	 *
	 * @param prefix Prefix of the properties.
	 */
	void removeProperties(String prefix);

	/**
	 * Returns a transient data.
	 *
	 * @param name Name of the data.
	 * @return Value of the data.
	 */
	Object getTransientData(String name);

	/**
	 * Sets a transient data.
	 * <p>
	 * Transient data are generally very specific to and implementation details of the
	 * class that sets them. The name of a transient data should therefore be prefixed
	 * with the name of the caller class in order to avoid name clashes.
	 *
	 * @param name Name of the data.
	 * @param value Value of the data. Can be null, in which case the data is
	 *   effectively removed.
	 */
	void setTransientData(String name, Object value);

	/**
	 * @return Name given to the ExecContext so that it can be identified by users.
	 *   Typically if the ExecContext is based on a workspace directory the name
	 *   could include a reference to that directory.
	 */
	String getName();

	/**
	 * If the ExecContext is cached, releases it so that a subsequent request for the
	 * same ExecContext (for instance an ExecContext based on the same workspace) will
	 * recreate it.
	 * <p>
	 * This method is not a regular release method that is meant to be called whenever
	 * a tool is done with the ExecContext. It is a special method meant to be called
	 * in exceptional situations when the user wants to do as if the ExecContext had
	 * never been created, such as if data on disk has changed outside of the control
	 * of the ExecContext.
	 * <p>
	 * If the ExecContext is not cached, this should be a noop operation.
	 */
	void release();
}
