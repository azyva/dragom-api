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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.azyva.dragom.model.plugin.NodePlugin;

/**
 * Mutable value object for a {@link NodeConfig} data.
 * <p>
 * This class is similar to {@link SimpleNodeConfig} but serves a different
 * purpose.
 *
 * @author David Raymond
 */
public abstract class NodeConfigValue {
	/**
	 * Name.
	 */
	private String name;

	/**
	 * Map of PropertyDefConfig.
	 */
	private Map<String, PropertyDefConfig> mapPropertyDefConfig;

	/**
	 * Map of PluginDefConfig.
	 */
	private Map<PluginKey, PluginDefConfig> mapPluginDefConfig;

	/**
	 * Constructor.
	 */
	public NodeConfigValue() {
		// LinkedHashMap are used to preserve insertion order.
		this.mapPropertyDefConfig = new LinkedHashMap<String, PropertyDefConfig>();
		this.mapPluginDefConfig = new LinkedHashMap<PluginKey, PluginDefConfig>();
	}

	/**
	 * @return Name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name See description.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public abstract NodeType getNodeType();

	/**
	 * Returns a {@link PropertyDefConfig}.
	 * <p>
	 * If the PropertyDefConfig exists but is defined with the value field set to
	 * null, a PropertyDefConfig is returned (instead of returning null).
	 *
	 * @param name Name of the PropertyDefConfig.
	 * @return PropertyDefConfig. null if the PropertyDefConfig does not exist.
	 */
	public PropertyDefConfig getPropertyDefConfig(String name) {
		return this.mapPropertyDefConfig.get(name);
	}

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
	public boolean isPropertyExists(String name) {
		return this.mapPropertyDefConfig.containsKey(name);
	}

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
	public List<PropertyDefConfig> getListPropertyDefConfig() {
		// A copy is returned to prevent the internal Map from being modified by the
		// caller. Ideally, an unmodifiable List view of the Collection returned by
		// Map.values should be returned, but that does not seem possible.
		return new ArrayList<PropertyDefConfig>(this.mapPropertyDefConfig.values());
	}

	/**
	 * Removes a {@link PropertyDefConfig}.
	 *
	 * @param name Name of the PropertyDefConfig.
	 */
	public void removePropertyDefConfig(String name) {
		this.mapPropertyDefConfig.remove(name);
	}

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
	public boolean setPropertyDefConfig(PropertyDefConfig propertyDefConfig) {
		return (this.mapPropertyDefConfig.put(propertyDefConfig.getName(), propertyDefConfig) == null);
	}

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
	public PluginDefConfig getPluginDefConfig(Class<? extends NodePlugin> classNodePlugin, String pluginId) {
		return this.mapPluginDefConfig.get(new PluginKey(classNodePlugin, pluginId));
	}

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
	public boolean isPluginDefConfigExists(Class<? extends NodePlugin> classNodePlugin, String pluginId) {
		return this.mapPluginDefConfig.containsKey(new PluginKey(classNodePlugin, pluginId));
	}

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
	public List<PluginDefConfig> getListPluginDefConfig() {
		// A copy is returned to prevent the internal Map from being modified by the
		// caller. Ideally, an unmodifiable List view of the Collection returned by
		// Map.values should be returned, but that does not seem possible.
		return new ArrayList<PluginDefConfig>(this.mapPluginDefConfig.values());
	}

	/**
	 * Removes a {@link PropertyDefConfig}.
	 *
	 * @param name Name of the PropertyDefConfig.
	 */
	public void removePlugingDefConfig(Class<? extends NodePlugin> classNodePlugin, String pluginId) {
		this.mapPropertyDefConfig.remove(new PluginKey(classNodePlugin, pluginId));
	}

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
	public boolean setPluginDefConfig(PluginDefConfig pluginDefConfig) {
		PluginKey pluginKey;

		pluginKey = new PluginKey(pluginDefConfig.getClassNodePlugin(), pluginDefConfig.getPluginId());

		return (this.mapPluginDefConfig.put(pluginKey, pluginDefConfig) == null);
	}
}
