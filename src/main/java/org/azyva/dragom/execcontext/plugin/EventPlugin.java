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
import org.azyva.dragom.model.Model;
import org.azyva.dragom.model.Node;
import org.azyva.dragom.model.event.NodeEvent;
import org.azyva.dragom.model.event.NodeEventListener;
import org.azyva.dragom.model.plugin.NodePlugin;

/**
 * Manages {@link NodeEvent}'s at the {@link ExecContext} level.
 *
 * @author David Raymond
 */
public interface EventPlugin extends ExecContextPlugin {
	/**
	 * Registers a {@link NodeEventListener} for {@link NodeEvent}'s raised on a
	 * {@link Node}.
	 * <p>
	 * The NodeEventListener is associated with the specified Node, but within the
	 * ExecContext. It can be bound to workspace or tool scope depending on
	 * indTransient.
	 * <p>
	 * It is also possible to register NodeEventListener's directly on a Node, and
	 * thus within the {@link Model}.
	 * <p>
	 * Generally {@link Event}'s are raised (by {@link NodePlugin}'s, tools or other)
	 * by calling {@link Node#raisehNodeEvent} which after having
	 * dispatched the Event to EventListener's registered within Node's, calls the
	 * {@link #raiseEvent} method to dispatch the Event to EventListener's registered
	 * within the ExecContext.
	 *
	 * @param node Node.
	 * @param nodeEventListener NodeEventListener.
	 * @param indChildrenAlso Indicates if
	 *   {@link NodeEvent} raised on children should be dispatched to the
	 *   NodeEventListener.
	 * @param indTransient Indicates if the NodeEventListener is to be bound to tool
	 *   scope, as opposed to workspace scope.
	 */
	public <NodeEventClass extends NodeEvent> void registerListener(Node node, NodeEventListener<NodeEventClass> nodeEventListener, boolean indChildrenAlso, boolean indTransient);

	/**
	 * Raises a {@link NodeEvent}.
	 * <p>
	 * The NodeEvent is dispatched to all
	 * {@link NodeEventListener}'s interested in it which have been registered within
	 * the {@link ExecContext} using {@link #registerListener}.
	 * <p>
	 * The NodeEvent sub-interface (ModuleEvent or ClassificationNodeEvent) must match
	 * the type of Node.
	 *
	 * @param nodeEvent NodeEvent.
	 */
	public void raiseNodeEvent(NodeEvent nodeEvent);
}
