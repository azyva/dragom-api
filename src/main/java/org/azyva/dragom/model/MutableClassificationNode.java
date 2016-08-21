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

import org.azyva.dragom.model.config.ClassificationNodeConfig;
import org.azyva.dragom.model.config.MutableClassificationNodeConfig;
import org.azyva.dragom.model.config.MutableConfig;

/**
 * Extension of {@link ClassificationNodeConfig} that allows changing the
 * configuration data.
 *
 * @author David Raymond
 * @see MutableConfig
 */
public interface MutableClassificationNode extends ClassificationNode, MutableNode {
	/**
	 * Creates a new uninitialized {@link MutableClassificationNode}.
	 * <p>
	 * Creation is finalized and visible only once
	 * {@link MutableClassificationNodeConfig#setNodeConfigTransferObject} is
	 * called, replacing any MutableClassificationNodeConfig with the same name
	 * that may already be set.
	 *
	 * @return MutableClassificationNode.
	 */
	MutableClassificationNode createChildMutableClassificationNode();

	/**
	 * Creates a new uninitialized {@link MutableModule}.
	 * <p>
	 * Creation is finalized and visible only once
	 * {@link MutableModule#setNodeConfigTransferObject} is called, replacing any
	 * MutableModuleConfig with the same name that may already be set.
	 *
	 * @return MutableModule.
	 */
	MutableModule createChildMutableModule();
}
