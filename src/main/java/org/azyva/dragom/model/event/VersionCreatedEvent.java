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

package org.azyva.dragom.model.event;

import org.azyva.dragom.model.Module;
import org.azyva.dragom.model.Version;
import org.azyva.dragom.model.plugin.ScmPlugin;

/**
 * {@link ModuleEvent} representing the creation of a new {@link Version}.
 * <p>
 * Although this is an abstract class and {@link ScmPlugin}'s are expected to post
 * subclasses, {@link NodeEventListener} can be registered for this base
 * ModuleEvent class.
 *
 * @author David Raymond
 */
public abstract class VersionCreatedEvent extends ModuleEvent {
	private Version version;

	/**
	 * Constructor.
	 *
	 * @param module {@link Module} on which this {@link ModuleEvent} is raised.
	 * @param version {@link Version} that was created.
	 */
	public VersionCreatedEvent(Module module, Version version) {
		super(module);
		this.version = version;
	}

	/**
	 * @return Version that was created.
	 */
	public Version getVersion() {
		return this.version;
	}

	/**
	 * @return String to help recognize the {@link ModuleEvent} instance, in logs for
	 *   example.
	 */
	@Override
	public String toString() {
		return "VersionCreatedEvent [module=" + this.getModule() + ", version=" + this.version + "]";
	}
}
