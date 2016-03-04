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

import java.text.ParseException;

import org.azyva.dragom.model.plugin.ScmPlugin;

/**
 * Represents a specific {@link Version} of a {@link Module}. Includes a
 * {@link NodePath} (which cannot not be partial) and a Version.
 * <p>
 * Implements value semantics. Instances are immutable.
 * <p>
 * Implements {@link hashCode} and {@link equals} so that instances can be used as
 * Map keys.
 * <p>
 * ModuleVersion support the literal form
 * &lt;NodePath&gt;:&lt;Version&gt; where
 * &lt;NodePath&gt; is the NodePath in String
 * format and &lt;Version&gt; is the Version in literal form.
 * <p>
 * Example: Acme/module:D/master

 * @author David Raymond
 */
public class ModuleVersion {
	private NodePath nodePath;
	private Version version;

	/**
	 * Private constructor used only by the method {@link #parse}.
	 */
	private ModuleVersion() {
	}

	/**
	 * Constructor using the individual fields.
	 *
	 * @param nodePath NodePath. Cannot be partial.
	 * @param version Version. Can be null to represent the default Version of a
	 *   Module.
	 */
	public ModuleVersion(NodePath nodePath, Version version) {
		if (nodePath == null) {
			throw new RuntimeException("Node path cannot be null.");
		}

		if (nodePath.isPartial()) {
			throw new RuntimeException("The node path " + nodePath + " must not be partial.");
		}

		this.nodePath = nodePath;
		this.version = version;
	}

	/**
	 * Parses a ModuleVersion in literal form.
	 *
	 * @param stringModuleVersion Version in literal form.
	 * @return Version.
	 * @throws ParseException If parsing fails.
	 */
	public static ModuleVersion parse(String stringModuleVersion)
		throws ParseException {
		String[] arrayComponentsModuleVersion;
		ModuleVersion moduleVersion;

		arrayComponentsModuleVersion = stringModuleVersion.split(":");

		if ((arrayComponentsModuleVersion.length < 1) || (arrayComponentsModuleVersion.length > 2)) {
			throw new ParseException("Error parsing module version " + stringModuleVersion + ". Module version must be formatted as \"<node-path>[:<version>]\".", 0);
		}

		moduleVersion = new ModuleVersion();

		moduleVersion.nodePath = NodePath.parse(arrayComponentsModuleVersion[0]);

		if (arrayComponentsModuleVersion.length == 2) {
			moduleVersion.version = Version.parse(arrayComponentsModuleVersion[1]);
		}

		return moduleVersion;
	}

	public NodePath getNodePath() {
		return this.nodePath;
	}

	/**
	 * Returns the {@link Version}.
	 * <p>
	 * Can be null in some cases to represent the default Version of a Module as
	 * defined by the {@link ScmPlugin}.
	 *
	 * @return See description.
	 */
	public Version getVersion() {
		return this.version;
	}

	/**
	 * @return ModuleVersion in literal form.
	 */
	@Override
	public String toString() {
		if (this.version == null) {
			return this.nodePath.toString();
		} else {
			return this.nodePath.toString() +  ':' + this.version;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result;

		result = 1;
		result = (prime * result) + this.nodePath.hashCode();
		result = (prime * result) + (this.version == null ? 0 : this.version.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object other) {
		ModuleVersion moduleVersionOther;

		if (this == other) {
			return true;
		}

		if (!(other instanceof ModuleVersion)) {
			return false;
		}

		moduleVersionOther = (ModuleVersion)other;

		if (!this.nodePath.equals(moduleVersionOther.nodePath)) {
			return false;
		}

		if (this.version == null) {
			if (moduleVersionOther.version != null) {
				return false;
			}
		} else if (!this.version.equals(moduleVersionOther.version)) {
			return false;
		}

		return true;
	}
}
