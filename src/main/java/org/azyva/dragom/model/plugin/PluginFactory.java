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

import org.azyva.dragom.model.Node;
import org.azyva.dragom.model.config.Config;

// TODO: Instantiation should not be heavy as an instance is create each time a NodePlugin need to be instantiated.
// If instances are heavy, it is the responsibility of the implementing class to cache initialization data.
// Implementations of this class can implement the getInstance() static method to implement the singleton pattern and avoid having multiple instances created.
//   optional. If not implemented, instances are expected to implement PluginFactory.
// Plugins instantiated using PluginFactory must not be cached as Factory can be dynamic.
public interface PluginFactory {
	/**
	 * Returns the default {@link NodePlugin} interface supported as a Class.
	 * <p>
	 * Most PluginFactory support a single NodePlugin interface and allowing the
	 * PluginFactory to specify that default NodePlugin interface allows
	 * {@link Config} to avoid having to redundantly specify both the class of the
	 * NodePlugin and of the PluginFactory.
	 * <p>
	 * null can be returned to indicate that no default NodePlugin Class is supported,
	 * generally implying that multiple NodePlugin Class's are supported and none is
	 * considered more important than the other. In this case Config must explicitly
	 * specify the NodePlugin class.
	 *
	 * @return Default Class of the default NodePlugin supported by the PluginFactory.
	 *   Can be null.
	 */
	Class<? extends NodePlugin> getDefaultClassNodePlugin();

	/**
	 * Returns the default plugin ID associated with a {@link NodePlugin} class.
	 * <p>
	 * null can be returned to indicate that no default plugin ID is recommended. In
	 * that case {@link Config} must explicitely specify the plugin ID.
	 * <p>
	 * The plugin ID within Config allows having multiple NodePlugin of the same
	 * interface on the same {@link Node}. Generally it is up to Config to specify the
	 * plugin ID of the various NodePlugin's. But some NodePlugin's may by convention
	 * generally be identified using a given plugin ID which can be returned by this
	 * method. This avoid having to specify the plugin ID in Config.
	 *
	 * @return Default plugin ID. Can be null.
	 */
	String getDefaultPluginId(Class<? extends NodePlugin> classNodePlugin);

	/**
	 * Indicates if a {@link NodePlugin} is supported.
	 * <p>
	 * This method is useful mostly for configuration tools that interact with
	 * the user and that may want to validate user input.
	 *
	 * @param classNodePlugin Class of the NodePlugin.
	 * @return Indicates if the NodePlugin is supported.
	 */
	<NodePluginInterface extends NodePlugin> boolean isPluginSupported(Class<NodePluginInterface> classNodePlugin);

	/**
	 * Returns a {@link NodePlugin} of the specified Class for a given {@link Node}.
	 * <p>
	 * Generally a new NodePlugin instance is returned as Node caches instantiated
	 * NodePlugin's.
	 *
	 * @param classNodePlugin Class of the NodePlugin.
	 * @param node Node.
	 * @return NodePlugin.
	 */
	<NodePluginInterface extends NodePlugin> NodePluginInterface getPlugin(Class<NodePluginInterface> classNodePlugin, Node node);
}
