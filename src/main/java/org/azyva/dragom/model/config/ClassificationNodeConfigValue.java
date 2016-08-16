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

/**
 * Mutable value object for a {@link ClassificationNodeConfig} data.
 * <p>
 * This class is similar to {@link SimpleClassificationNodeConfig} but serves a
 * different purpose.
 * <p>
 * Child {@link NodeConfigValue}'s are not included in this class as the parent-
 * child relationship between {@link MutableNodeConfig}'s is managed separately
 * with methods in {@link MutableClassificationNodeConfig}.
 *
 * @author David Raymond
 */
public class ClassificationNodeConfigValue extends NodeConfigValue {
	/**
	 * Constructor.
	 */
	public ClassificationNodeConfigValue() {
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.CLASSIFICATION;
	}

}
