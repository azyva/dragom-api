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

import org.azyva.dragom.model.config.ClassificationNodeConfigValue;
import org.azyva.dragom.model.config.Config;
import org.azyva.dragom.model.config.ModuleConfigValue;
import org.azyva.dragom.model.config.MutableConfig;

/**
 * Extension of {@link Model} that allows changing the underlying configuration
 * data from which the Model is created.
 * <p>
 * A Model which implements this interface will generally be based on
 * {@link MutableConfig}, although this is not strictly required since only the
 * {@link ModuleConfigValue} and {@link ClassificationNodeConfigValue} are
 * actually exposed through the model interfaces.
 * <p>
 * The methods in the mutable model interfaces are similar to those int he mutable
 * configuration interfaces, and this may seem redundant. The idea is that Dragom
 * separates the Model from its {@link Config} implementation can be used
 * interchangeably using the same Model implementation. And changes to the
 * Config cannot be done without the Model knowing about it since an active Model
 * can contains many dynamically instantiated objects such as plugins whose
 * configuration depend on the Model Config, and the Config changes, the Model
 * must make appropriate adjustments, such as clearing various caches to force the
 * recreation of the dynamically instantiated objects.
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
