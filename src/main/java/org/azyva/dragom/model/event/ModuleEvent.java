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

import org.azyva.dragom.model.Module;

/**
 * Base class for all events that can be raised on {@link Module}'s
 *
 * @author David Raymond
 */
public abstract class ModuleEvent extends NodeEvent {
  /**
   * Constructor.
   *
   * @param module on which the event is raised.
   */
  public ModuleEvent(Module module) {
    super(module);
  }

  /**
   * @return Module on which this ModuleEvent is raised.
   */
  public Module getModule() {
    return (Module)this.getNode();
  }

  /**
   * @return String to help recognize the {@link ModuleEvent} instance, in logs for
   *   example.
   */
  @Override
  public String toString() {
    return "ModuleEvent [module=" + this.getNode() + "]";
  }
}
