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

import org.azyva.dragom.model.plugin.NodePlugin;

/**
 * Both {@link NodeConfig} implementations and {@link Node} need to maintain Map's
 * of {@link PluginDefConfig} or {@link NodePlugin}. This class is used for keys
 * within these Map's.
 * <p>
 * It is used internally and not expected to be useful to external callers.
 */
public class PluginKey {
	Class<? extends NodePlugin> classNodePlugin;
	String pluginId;

	/**
	 * Constructor.
	 *
	 * @param classNodePlugin {@link NodePlugin} Class.
	 * @param pluginId Plugin ID to distinguish between multiple instances of the same
	 *   NodePlugin.
	 */
	public PluginKey(Class<? extends NodePlugin> classNodePlugin, String pluginId) {
		this.classNodePlugin = classNodePlugin;
		this.pluginId = pluginId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result;

		result = 1;
		result = (prime * result) + this.classNodePlugin.hashCode();
		result = (prime * result) + ((this.pluginId == null) ? 0 : this.pluginId.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (other == null) {
			return false;
		}

		if (!(other instanceof PluginKey)) {
			return false;
		}

		PluginKey pluginKeyOther = (PluginKey)other;

		if (this.classNodePlugin != pluginKeyOther.classNodePlugin) {
			return false;
		}

		if (this.pluginId == null) {
			if (pluginKeyOther.pluginId != null) {
				return false;
			}
		} else if (!this.pluginId.equals(pluginKeyOther.pluginId)) {
			return false;
		}

		return true;
	}
};
