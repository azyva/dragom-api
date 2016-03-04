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

import org.azyva.dragom.execcontext.ExecContext;
import org.azyva.dragom.model.Model;
import org.azyva.dragom.model.Node;

/**
 * Base interface for all node plugins.
 * <p>
 * A NodePlugin will actually be either a {@link ClassificationNodePlugin} or a
 * {@link ModulePlugin}. It cannot only implement NodePlugin.
 * <p>
 * During its execution, a NodePlugin can access the {@link ExecContext} of the
 * tool on behalf of which it is executing. This includes accessing properties
 * ({@link ExecContext#getProperty, etc.) and transient data
 * ({@link ExecContext#getTransientData}, etc.). But this must be done in a way
 * that is coherent with the fact that a NodePlugin is part of the {@link Model}
 * and as such must be considered in Model scope (static). In particular, a
 * NodePlugin must not affect its internal state based on ExecContext information,
 * which is at a lower level scope.
 *
 * @author David Raymond
 */
public interface NodePlugin {
	/**
	 * @return Node to which this NodePlugin is attached.
	 */
	Node getNode();
}
