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

package org.azyva.dragom.execcontext.plugin;

import org.azyva.dragom.execcontext.ExecContext;
import org.azyva.dragom.model.Node;

/**
 * Provides runtime properties during the execution of tools.
 * <p>
 * Modifications to runtime properties are not meant to be persisted across tool
 * executions. If a tool needs to manage persistent properties, it should
 * explicitly use {@link ExecContext#getProperty} and
 * {@link ExecContext#setProperty} or other means.
 *
 * @author David Raymond
 */
public interface RuntimePropertiesPlugin extends ExecContextPlugin {
	/**
	 * Gets the value of a property associated with a node.
	 * <p>
	 * It is up to the plugin implementation to use the appropriate property
	 * resolution algorithm. Such an algorithm can involve:
	 * <p>
	 * <li>Evaluating the property as an expression</li>
	 * <li>Using the properties of the Node as default values for RuntimeProperties
	 *     that may not be explicitly defined</li>
	 * <li>Implementing property inheritance</li>
	 *
	 * @param node Node in the context of which the property is requested. May be
	 *   null as equivalent to specifying the Model root Node.
	 * @param name Name of the property.
	 * @return Value of the property. null if no such property.
	 */
	String getProperty(Node node, String name);

	/**
	 * Sets the value of a property associated with a node.
	 * <p>
	 * Properties sets in this way are expected to take precedence.
	 *
	 * @param node Node in the context of which the property is set. May be null as
	 *   equivalent to specifying the Model root Node.
	 * @param name Name of the property.
	 * @param value Value of the property.
	 */
	void setProperty(Node node, String name, String value);
}
