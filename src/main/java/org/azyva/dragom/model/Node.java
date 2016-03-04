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

package org.azyva.dragom.model;

import java.util.List;

import org.azyva.dragom.execcontext.ExecContext;
import org.azyva.dragom.execcontext.plugin.EventPlugin;
import org.azyva.dragom.model.config.NodeTypeEnum;
import org.azyva.dragom.model.event.NodeEvent;
import org.azyva.dragom.model.event.NodeEventListener;
import org.azyva.dragom.model.plugin.NodePlugin;

/**
 * Represents a node at runtime.
 * <p>
 * Two type of Node's exist each represented by a different subclass:
 * <p>
 * <li>{@link ClassificationNode}</li>
 * <li>{@link Module}</li>
 *
 * @author David Raymond
 */
public interface Node {
//TODO:	review comments. For raiseEvent, should it really be specified that ExecContext listeners are considered?
//			For properties, should we mention that inheritance should be considered by implementations?
	/**
	 * @return Parent {@link ClassificationNode}. null in the case of the root
	 *   ClassificationNode.
	 */
	ClassificationNode getClassificationNodeParent();

	/**
	 * @return Name. This is equivalent to getNodeConfig().getName().
	 */
	String getName();

	/**
	 * @return Model.
	 */
	Model getModel();

	/**
	 * @return NodeTypeEnum.
	 */
	NodeTypeEnum getNodeType();

	/**
	 * @return NodePath. null for the root {@link ClassificationNode}.
	 */
	public NodePath getNodePath();

	/**
	 * Returns the value of a property.
	 *
	 * @param name Name of the property.
	 * @return Value of the property.
	 */
	public String getProperty(String name);

	/**
	 * Gets the specified {@link NodePlugin} for this Node.
	 * <p>
	 * The NodePlugin must exist. {@link #isNodePluginExists} can be used to verify if
	 * a NodePlugin exists before getting it.
	 *
	 * @param classNodePlugin Class of the NodePlugin.
	 * @param pluginId Plugin ID to distinguish between multiple instances of the same
	 *   NodePlugin.
	 * @return NodePlugin. The type is as specified by classNodePlugin.
	 */
	<NodePluginInterface extends NodePlugin> NodePluginInterface getNodePlugin(Class<NodePluginInterface> classNodePlugin, String pluginId);

	/**
	 * Verifies if a {@link NodePlugin exists for this Node, without instantiating it.
	 *
	 * @param classNodePlugin Class of the NodePlugin.
	 * @param pluginId Plugin ID to distinguish between multiple instances of the same
	 *   NodePlugin.
	 * @return Indicates if the NodePlugin exists.
	 */
	boolean isNodePluginExists(Class<? extends NodePlugin> classNodePlugin, String pluginId);

	/**
	 * Returns the List of plugin IDs of all NodePlugin's of the specified class
	 * available.
	 *
	 * @param classNodePlugin Class of the NodePlugin.
	 * @return List of plugin IDs.
	 */
	public List<String> getListPluginId(Class<? extends NodePlugin> classNodePlugin);

	/**
	 * Registers a {@link NodeEventListener}.
	 * <p>
	 * The NodeEventListener is registered within the Node, and thus within the
	 * {@link Model}. It is also possible to register NodeEventListener's in the
	 * {@link ExecContext}.
	 *
	 * @param nodeEventListener NodeEventListener.
	 * @param indChildrenAlso Indicates if {@link NodeEvent} raised on children should
	 *   be dispatched to the NodeEventListener.
	 */
	<NodeEventClass extends NodeEvent> void registerListener(NodeEventListener<NodeEventClass> nodeEventListener, boolean indChildrenAlso);

	/**
	 * Raises a {@link NodeEvent}.
	 * <p>
	 * The NodeEvent is dispatched to all registered {@link NodeEventListener}'s
	 * interested in it.
	 * <p>
	 * NodeEventListener's registered in the {@link ExecContext} using
	 * {@link EventPlugin#registerListener} are also considered.
	 * <p>
	 * The NodeEvent sub-interface (ModuleEvent or ClassificationNodeEvent) must match
	 * the type of Node.
	 * <p>
	 * The NodeEvent must be dispatched on {@link NodeEvent#getNode}.
	 *
	 * @param nodeEvent NodeEvent.
	 */
	void raiseNodeEvent(NodeEvent nodeEvent);
}
