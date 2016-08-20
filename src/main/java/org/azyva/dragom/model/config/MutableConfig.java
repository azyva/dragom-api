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

import org.azyva.dragom.model.Model;

/**
 * Extension of {@link Config} that allows changing the configuration data.
 * <p>
 * The design orientation of the mutable extension to Config and child interfaces
 * is to allow applying changes atomically to the various {@link NodeConfig}. That
 * is why transfer objects are used.
 * <p>
 * The reason for implementing atomocity semantics are two-fold:
 * <p>
 * <li>If the configuration data is meant to be persisted, it is expected that
 *     persisting operations be performed for each mutation and mutating whole objects
 *     at a time provides more efficiency than persisting each individual change;</li>
 * <li>We want to support configuration data undergoing changes while a {@link Model}
 *     is active and requiring the associated Model to react to whole object changes
 *     provides more efficiency than reacting to each individual change.<li>
 * <p>
 * Runtime change events may eventually be supported so that the Model can adjust
 * to configuration changes at runtime.
 */
public interface MutableConfig extends Config {
	/**
	 * Creates a new uninitialized root {@link MutableClassificationNodeConfig}.
	 * <p>
	 * Creation is finalized and visible only once
	 * {@link MutableClassificationNodeConfig#setNodeConfigTransferObject} is called,
	 * replacing any root MutableClassificationNodeConfig that may already be set.
	 *
	 * @return Root MutableClassificationNodeConfig.
	 */
	MutableClassificationNodeConfig createClassificationNodeConfigRoot();
}
