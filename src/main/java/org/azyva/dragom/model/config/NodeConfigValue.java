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

import org.azyva.dragom.model.plugin.NodePlugin;

/**
 * Mutable value object for a {@link NodeConfig} data.
 * <p>
 * {@link MutableNodeConfig} and its sub-interfaces return and take as argument
 * this interface to allow getting and setting atomically data. See
 * {@link MutableConfig}.
 * <p>
 * This interface is similar to {@link NodeConfig} but serves a different
 * purpose.
 * <p>
 * Since this interface represents a value object, implementations are generally
 * straightforward, and in many cases, {@link SimplNodeConfigValue} will be
 * adquate. But implementations subclass SimpleNodeCOnfigValue to add fields for
 * managing concurrency, such as a last modification timestamp if the
 * configuration data is persisted in a database and Dragom is used in a
 * multi-user application context.
 *
 * @author David Raymond
 */
public interface NodeConfigValue {
	/**
	 * @return Name.
	 */
	String getName();

	/**
	 * Sets the name.
	 *
	 * @param name See description.
	 */
	void setName(String name);

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
	public List<PropertyDefConfig> getListPropertyDefConfig();

	/**
	 * Removes a {@link PropertyDefConfig}.
	 *
	 * @param name Name of the PropertyDefConfig.
	 */
	public void removePropertyDefConfig(String name);

	/**
	 * Sets a {@link PropertyDefConfig}.
	 * <p>
	 * If one already exists with the same name, it is overwritten. Otherwise it is
	 * added.
	 * <p>
	 * Mostly any implementation of PropertyDefConfig can be used, although
	 * {@link SimplePropertyDefConfig} is generally the better choice.
	 *
	 * @param propertyDefConfig PropertyDefConfig.
	 * @return Indicates if a new PropertyDefConfig was added (as opposed to an
	 *   existing one having been overwritten.
	 */
	public boolean setPropertyDefConfig(PropertyDefConfig propertyDefConfig);

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
	public PluginDefConfig getPluginDefConfig(Class<? extends NodePlugin> classNodePlugin, String pluginId);

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
	public boolean isPluginDefConfigExists(Class<? extends NodePlugin> classNodePlugin, String pluginId);

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
	public List<PluginDefConfig> getListPluginDefConfig();

	/**
	 * Removes a {@link PropertyDefConfig}.
	 *
	 * @param name Name of the PropertyDefConfig.
	 */
	public void removePlugingDefConfig(Class<? extends NodePlugin> classNodePlugin, String pluginId);

	/**
	 * Sets a {@link PluginDefConfig}.
	 * <p>
	 * If one already exists with the same {@link PluginKey}, it is overwritten.
	 * Otherwise it is added.
	 * <p>
	 * Mostly any implementation of PluginDefConfig can be used, although
	 * {@link SimplePluginDefConfig} is generally the better choice.
	 *
	 * @param pluginDefConfig PluginDefConfig.
	 * @return Indicates if a new PluginDefConfig was added (as opposed to an existing
	 *   one having been overwritten.
	 */
	public boolean setPluginDefConfig(PluginDefConfig pluginDefConfig);
}
