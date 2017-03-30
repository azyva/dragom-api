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

import org.azyva.dragom.model.Model;

/**
 * Root of a {@link Model} configuration.
 * <p>
 * The Model used during the execution of tools is based on configuration data
 * provided by this interface and its members.
 * <p>
 * Config data exposed through this interface is considered static at runtime.
 * However, classes and child classes implementing this interface and child
 * interfaces may also implement {@link MutableConfig} and mutable child
 * interfaces allowing the configuration data to be changed.
 *
 * @author David Raymond
 */
public interface Config {
  /**
   * @return Root {@link ClassificationNodeConfig}.
   */
  ClassificationNodeConfig getClassificationNodeConfigRoot();

  /**
   * Flushes the Model configuration to persistent storage.
   *
   * See {@link Model#flush}.
   *
   * <p>If the configuration is stored in persistent storage which is not updated in
   * sync with the changes, this method should be implemented and should persist the
   * configuration.
   *
   * <p>If this method does not make sense in a certain context (e.g.: configuration
   * stored in a DB), it should do nothing and simply return.
   */
  void flush();
}
