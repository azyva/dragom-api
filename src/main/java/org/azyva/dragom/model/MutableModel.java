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

import org.azyva.dragom.model.config.MutableConfig;
import org.azyva.dragom.model.config.NodeConfigTransferObject;

/**
 * Extension of {@link Model} that allows changing the underlying configuration
 * data from which the Model is created.
 * <p>
 * A Model and child classes which implement this interface and child interfaces
 * will generally be based on {@link MutableConfig} and mutable child interfaces,
 * although this is not strictly required since only
 * {@link NodeConfigTransferObject} is actually exposed through these interfaces.
 * The reason for not exposing the underlying MutableConfig and mutable child
 * interfaces is that changes to the MutableConfig and child interfaces cannot be
 * done without the MutableModel knowing about it since an active MutableModel can
 * contain many dynamically instantiated objects (e.g., plugins) whose
 * configuration depend on the MutableConfig, and if it changes, the MutableModel
 * must make appropriate adjustments, such as clearing various caches to force the
 * recreation of the dynamically instantiated objects.
 * <p>
 * Runtime change events may eventually be supported so that the MutableModel can
 * adjust to configuration changes at runtime.
 *
 */
public interface MutableModel extends Model {
	/**
	 * Creates a new uninitialized root {@link MutableClassificationNode}.
	 * <p>
	 * Creation is finalized and visible only once
	 * {@link MutableClassificationNode#setClassificationNodeConfigValue} is
	 * called, replacing any root MutableClassificationNode that may already be
	 * set.
	 *
	 * @return Root MutableClassificationNodeConfig.
	 */
	MutableClassificationNode createClassificationNodeConfigRoot();

}
