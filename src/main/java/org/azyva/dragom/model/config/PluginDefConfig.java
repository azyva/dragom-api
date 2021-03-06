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

package org.azyva.dragom.model.config;

import org.azyva.dragom.model.Model;
import org.azyva.dragom.model.Node;
import org.azyva.dragom.model.plugin.NodePlugin;
import org.azyva.dragom.model.plugin.NodePluginFactory;

/**
 * Configuration of a {@link NodePlugin} within a {@link NodeConfig}.
 * <p>
 * A PluginDefConfig has the classNodePlugin and pluginId properties. These must
 * be provided (pluginId can be null though, null being a possible value). But
 * that does not necessarily mean that the configuration store must explicitly
 * specify them as they can be inferred by the class specified by the pluginClass
 * property.
 * <p>
 * This is so that it is not necessary in the the {@link Config} to specify the
 * plugin interface or plugin ID in the case the specified plugin implementation
 * class supports a default {@link NodePlugin} or plugin ID.
 * <p>
 * If pluginClass refers to a class that implements {@link NodePluginFactory} (factory
 * design pattern) {@link NodePluginFactory#getDefaultClassNodePlugin} must be used by
 * implementations to get the value of the classNodePlugin property when not
 * defined. If pluginClass refers to a class that directly implements a sub-
 * interface of {@link NodePlugin} (constructor design pattern), it is up to the
 * implementation to implement a default mechanism for the classNodePlugin
 * property. But the following is recommended:
 * <ul>
 * <li>If the class implements the static method getDefaultClassNodePlugin with no
 *     parameter, call this method to get the default value of the property
 *     classNodePlugin;</li>
 * <li>Otherwise, use the first NodePlugin sub-interface as the default.</li>
 * </ul>
 * Similarly for the pluginId property:
 * <p>
 * If pluginClass refers to a class that implements NodePluginFactory,
 * NodePluginFactory.getDefaultPluginId must be used implementations to get the value
 * of the pluginId property when not defined. If pluginClass refers to a class
 * that directly implements a sub-interface of NodePlugin, it is up to the
 * implementation to implement a default mechanism for the pluginId property. But
 * the following is recommended:
 * <ul>
 * <li>If the class implements the static method getDefaultPluginId with the
 *     Class of the NodePlugin as parameter (property classNodePlugin), call this
 *     method to get the default value of the property pluginId;</li>
 * <li>Otherwise, use null as the value of the pluginId property.</li>
 * </ul>
 * The strategy described above for obtaining the default values for the
 * classNodePlugin and pluginId properties is implemented by
 * Util.getDefaultClassNodePlugin and Util.getDefaultPluginId from dragom-core.
 * which implementations can use.
 * <p>
 * During the design of {@link Config} and its members, it was debated whether the
 * responsibility for handling the default values for the classNodePlugin and
 * pluginId properties should be given to Config or more centrally to
 * {@link Node}. It was finally decided to give that responsibility to the
 * implementations of this interface, even if it means that the strategy needs to
 * be implemented in multiple places, for the following reasons:
 * <ul>
 * <li>Simpler implementation of Node;</li>
 * <li>From the point of view of the consumers of Config (essentially the Node
 *     class) it makes sense to not have to worry about these default details and
 *     see a clean configuration;</li>
 * <li>The duplication of code is not a big problem as most of the strategy is
 *     implemented by Util.getDefaultClassNodePlugin and
 *     Util.getDefaultPluginId.</li>
 * </ul>
 *
 * @author David Raymond
 * @see Config
 */
public interface PluginDefConfig {
  /**
   * @return Class of the NodePlugin.
   */
  Class<? extends NodePlugin> getClassNodePlugin();

  /**
   * @return Plugin ID. Can be null.
   */
  String getPluginId();

  /**
   * Returns the plugin implementation class name.
   * <p>
   * null to avoid inheritance.
   * <p>
   * This class must either implement {@link NodePluginFactory} or have a constructor
   * and implement the {@link NodePlugin} and the appropriate sub-interface
   * identified by {@link #getClassNodePlugin}.
   *
   * @return See description.
   */
  String getPluginClass();

  /**
   * @return Indicates that this PluginDefConfig applies specifically to the
   *   {@link NodeConfig} on which it is defined, as opposed to being inherited by
   *   child NodeConfig when interpreted by the {@link Model}.
   */
  boolean isOnlyThisNode();
}
