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

import java.util.Properties;
import java.util.Set;

import org.azyva.dragom.execcontext.plugin.ExecContextPlugin;
import org.azyva.dragom.execcontext.plugin.RuntimePropertiesPlugin;
import org.azyva.dragom.execcontext.plugin.ToolLifeCycleExecContextPlugin;

/**
 * Implemented by {@link ExecContext} implementations that need to be aware of
 * tool life cycle.
 * <p>
 * During tool initialization, if the ExecContext implements this interface,
 * {@link #startTool} should be called. Similarly for {@link #endTool} during tool
 * termination.
 * <p>
 * This allow the ExecContext to properly honor
 * {@link ExecContextPlugin} desire to be informed of tool life cycle when their
 * implementation implements {@link ToolLifeCycleExecContextPlugin}.
 * <p>
 * It also allows the tool to pass initialization Properties to the ExecContext,
 * and any class during the execution of the tool to access these properties in a
 * read-only manner.
 * <p>
 * Note that runtime Properties provided by {@link RuntimePropertiesPlugin} can
 * come from initialization properties, but not all initialization properties are
 * runtime Properties.
 * <p>
 * If this interface is not implemented by an ExecContext implementation, the
 * implementation should treat all ExecContextPlugin's as being transient, which
 * can do no harm apart from reduced performance due to not reusing instances when
 * a single JVM is used for multiple tool executions.
 * <p>
 * This interface, together with ToolLifeCycleExecContextPlugin, allow expressing
 * a relation between ExecContextPlugin's and ExecContext information scope.

 * @author David Raymond
 */
public interface ToolLifeCycleExecContext {
	/**
	 * Indicates that a tool execution is about to start.
	 * <p>
	 * Should be called by tools during initialization when the ExecContext
	 * implementation implements this interface.
	 *
	 * @param propertieInit Initialization properties specific to the tool.
	 */
	void startTool(Properties propertiesInit);

	/**
	 * Indicates that a tool execution has ended.
	 * <p>
	 * Should be called by tools during termination when the ExecContext
	 * implementation implements this interface.
	 */
	void endTool();

	/**
	 * Tool properties are those provided by a tool when calling
	 * {@link #startTool}.
	 *
	 * @return Set of all tool properties.
	 */

	Set<String> getSetToolProperty();

	/**
	 * Returns the value of an tool property.
	 * <p>
	 * Tool properties are those provided by a tool when calling
	 * {@link #startTool}.
	 *
	 * @param name Name of the property.
	 * @return Value of the property.
	 */
	String getToolProperty(String name);
}
