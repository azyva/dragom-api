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
 * Extension of {@link ClassificationNodeConfig} that allows changing the
 * configuration data.
 *
 * @author David Raymond
 * @see MutableConfig
 */
public interface MutableClassificationNodeConfig extends ClassificationNodeConfig, MutableNodeConfig {
	/**
	 * @return {@link ClassificationNodeConfigValue}.
	 */
	ClassificationNodeConfigValue getClassificationNodeConfigValue();

	/**
	 * Sets the {@link ClassificationNodeConfigValue}.
	 *
	 * @param classificationNodeConfigValue ClassificationNodeConfigValue.
	 */
	void setClassificationNodeConfigValue(ClassificationNodeConfigValue classificationNodeConfigValue);

	/**
	 * Deletes a child {@link MutableNodeConfig} ({@link MutableModuleConfig} or
	 * {@link MutableClassificationNodeConfig}.
	 *
	 * @param childNodeName Child node name.
	 */
	void deleteChildNodeConfig(String childNodeName);

	/**
	 * Creates a new uninitialized {@link MutableModuleConfig}.
	 * <p>
	 * Creation is finalized and visible only once
	 * {@link MutableModuleConfig#setModuleConfigValue} is called, replacing any
	 * MutableNodeConfig with the same name that may already be set.
	 *
	 * @return MutableModuleConfig.
	 */
	MutableModuleConfig createChildModuleConfig();

	/**
	 * Creates a new uninitialized {@link MutableClassificationNodeConfig}.
	 * <p>
	 * Creation is finalized and visible only once
	 * {@link MutableClassificationNodeConfig#setClassificationNodeConfigValue} is
	 * called, replacing any MutableNodeConfig with the same name that may already be
	 * set.
	 *
	 * @return MutableClassificationNodeConfig.
	 */
	MutableClassificationNodeConfig createChildClassificationNodeConfig();
}
