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

package org.azyva.dragom.model.configdao;

import java.util.List;

import org.azyva.dragom.model.config.NodeType;
import org.azyva.dragom.model.config.PluginDefConfig;
import org.azyva.dragom.model.config.PluginKey;
import org.azyva.dragom.model.config.PropertyDefConfig;

public interface NodeConfig {
  NodeType getNodeType();

  String getName();

  PropertyDefConfig getPropertyDefConfig(String name);

  boolean isPropertyDefConfigExists(String name);

  List<PropertyDefConfig> getListPropertyDefConfig();

  PluginDefConfig getPluginDefConfig(PluginKey pluginKey);

  boolean isPluginDefConfigExists(PluginKey pluginKey);

  List<PluginDefConfig> getListPluginDefConfig();
}
