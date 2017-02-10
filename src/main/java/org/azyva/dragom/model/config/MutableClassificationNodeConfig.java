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
   * Creates a new uninitialized {@link MutableClassificationNodeConfig}.
   *
   * <p>Creation is finalized and visible only once
   * {@link MutableClassificationNodeConfig#setNodeConfigTransferObject} is called,
   * replacing any MutableNodeConfig with the same name that may already be set.
   *
   * <p>No method other than
   * {@link MutableClassificationNodeConfig#getNodeConfigTransferObject} and
   * {@link MutableClassificationNodeConfig#setNodeConfigTransferObject} can be
   * called until creation is finalized.
   *
   * @return MutableClassificationNodeConfig.
   */
  MutableClassificationNodeConfig createChildMutableClassificationNodeConfig();

  /**
   * Creates a new uninitialized {@link MutableModuleConfig}.
   *
   * <p>Creation is finalized and visible only once
   * {@link MutableModuleConfig#setNodeConfigTransferObject} is called, replacing
   * any MutableNodeConfig with the same name that may already be set.
   *
   * <p>No method other than
   * {@link MutableModuleConfig#getNodeConfigTransferObject} and
   * {@link MutableModuleConfig#setNodeConfigTransferObject} can be called until
   * creation is finalized.
   *
   * @return MutableModuleConfig.
   */
  MutableModuleConfig createChildMutableModuleConfig();
}
