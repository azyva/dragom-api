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

package org.azyva.dragom.model.event;

import org.azyva.dragom.model.Node;

/**
 * Base class for all events that can be raised on {@link Node}'s.
 *
 * @author David Raymond
 */
public abstract class NodeEvent {
	private Node node;

	/**
	 * Constructor.
	 *
	 * @param {@link Node} on which the event is raised.
	 */
	public NodeEvent(Node node) {
		this.node = node;
	}

	/**
	 * @return Node on which this NodeEvent is raised.
	 */
	public Node getNode() {
		return this.node;
	}

	/**
	 * @return String to help recognize the NodeEvent instance, in logs for example.
	 */
	@Override
	public String toString() {
		return "NodeEvent [node=" + this.node + "]";
	}
}
