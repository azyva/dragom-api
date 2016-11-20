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
import org.azyva.dragom.model.Version;
import org.azyva.dragom.model.VersionType;

/**
 * [@link ModuleEvent} representing the creation of a new static Version.
 *
 * It is expected that the ScmPlugin's post such events.
 *
 * @author David Raymond
 */
public class StaticVersionCreatedEvent extends VersionCreatedEvent {
	/**
	 * Constructor.
	 *
	 * @param module {@link Module} on which the {@link ModuleEvent} is raised.
	 * @param versionStatic Static {@link Version} that was created.
	 */
	public StaticVersionCreatedEvent(Module module, Version versionStatic) {
		super(module, versionStatic);

		if (versionStatic.getVersionType() != VersionType.STATIC) {
			throw new RuntimeException("Invalid version type.");
		}
	}

	/**
	 * @return String to help recognize the {@link ModuleEvent} instance, in logs for
	 *   example.
	 */
	@Override
	public String toString() {
		return "StaticVersionCreatedEvent [module=" + this.getModule() + ", version=" + this.getVersion() + "]";
	}
}
