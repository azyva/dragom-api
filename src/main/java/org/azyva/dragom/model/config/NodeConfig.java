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

package org.azyva.dragom.model.config;

import java.util.List;

import org.azyva.dragom.model.ClassificationNode;
import org.azyva.dragom.model.Module;
import org.azyva.dragom.model.plugin.NodePlugin;

/**
 * Configuration of a {@link Node}.
 * <p>
 * A NodeConfig can represent a {@link Module} or a {@link ClassificationNode}, as
 * specified by @{link #getNodeType}. If a NodeConfig represents a Module, it will
 * also implement {@link ModuleConfig}. If a NodeConfig represents a
 * ClassificationNode, it will also implement {@link ClassificationNode}.
 *
 * @author David Raymond
 * @see Config
 */
public interface NodeConfig {
	/**
	 * @return Name.
	 */
	String getName();

	/**
	 * @return NodeType.
	 */
	NodeType getNodeType();

	/**
	 * Returns a {@link PropertyDefConfig}.
	 * <p>
	 * If the PropertyDefConfig exists but is defined with the value field set to
	 * null, a PropertyDefConfig is returned (instead of returning null).
	 *
	 * @param name Name of the PropertyDefConfig.
	 * @return PropertyDefConfig. null if the PropertyDefConfig does not exist.
	 */
	PropertyDefConfig getPropertyDefConfig(String name);

	/**
	 * Verifies if a {@link PropertyDefConfig} exists.
	 * <p>
	 * If the PropertyDefConfig exists but is defined with the value field set to
	 * null, true is returned.
	 * <p>
	 * Returns true if an only if {@link #getPropertyDefConfig} does not return null.
	 *
	 * @param name Name of the PropertyDefConfig.
	 * @return Indicates if the PropertyDefConfig exists.
	 */
	boolean isPropertyExists(String name);

	/**
	 * Returns a List of all the {@link PropertyDefConfig}'s.
	 * <p>
	 * If no PropertyDefConfig is defined for the NodeConfig, an empty List is
	 * returned (as opposed to null).
	 * <p>
	 * The order of the PropertyDefConfig is generally expected to be as defined
	 * in the underlying storage for the configuration, hence the List return type.
	 * But no particular order is actually guaranteed.
	 *
	 * @return See description.
	 */
	List<PropertyDefConfig> getListPropertyDefConfig();

	/**
	 * Returns a {@link PluginDefConfig}.
	 * <p>
	 * If the PluginDefConfig exists but is defined with the pluginClass field set to
	 * null, a PluginDefConfig is returned (instead of returning null).
	 *
	 * @param classNodePlugin Class of the {@link NodePlugin} interface.
	 * @param pluginId Plugin ID to distinguish between multiple instances of the same
	 *   plugin. Can be null to get a PluginDefConfig whose field pluginId is null.
	 * @return PluginDefConfig. null if the PluginDefConfig does not exist.
	 */
	PluginDefConfig getPluginDefConfig(Class<? extends NodePlugin> classNodePlugin, String pluginId);

	/**
	 * Verifies if a {@link PluginDefConfig} exists.
	 * <p>
	 * If the PluginDefConfig exists but is defined with the pluginClass field set to
	 * null, true is returned.
	 * <p>
	 * Returns true if an only if {@link #getPluginDefConfig(Class<? extends NodePlugin>, String}
	 * does not return null.
	 *
	 * @param name Name of the PluginyDef.
	 * @return Indicates if the PluginDefConfig exists.
	 */
	boolean isPluginDefConfigExists(Class<? extends NodePlugin> classNodePlugin, String pluginId);

	/**
	 * Returns a List of all the {@link PluginDefConfig}'s.
	 * <p>
	 * If no PluginDefConfig is defined for the NodeConfig, an empty Set is returned
	 * (as opposed to null).
	 * <p>
	 * The order of the PluginDefConfig is generally expected to be as defined
	 * in the underlying storage for the configuration, hence the List return type.
	 * But no particular order is actually guaranteed.
	 *
	 * @return See description.
	 */
	List<PluginDefConfig> getListPluginDefConfig();
}
