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
 * Configuration of a property within a {@link NodeConfig}.
 *
 * @author David Raymond
 * @see Config
 */
public interface PropertyDefConfig {
  /**
   * Returns the
   *
   * @return Name.
   */
  String getName();

  /**
   * @return Value. null to avoid inheritance.
   */
  String getValue();

  /**
   * @return Indicates that this PropertyDefConfig applies specifically to the
   *   {@link NodeConfig} on which it is defined, as opposed to being inherited by
   *   child NodeConfig when interpreted by the {@link Model}.
   */
  boolean isOnlyThisNode();
}
