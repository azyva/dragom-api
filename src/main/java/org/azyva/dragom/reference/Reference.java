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

package org.azyva.dragom.reference;

import org.azyva.dragom.model.ArtifactGroupId;
import org.azyva.dragom.model.ArtifactVersion;
import org.azyva.dragom.model.ModuleVersion;

/**
 * Class representing a reference.
 *
 * This class implements value semantics and is immutable.
 */
public class Reference {
	/**
	 * ModuleVersion. null if artifact-level reference and module unknown to Dragom.
	 */
	private ModuleVersion moduleVersion;

	/**
	 * ArtifactGroupId. null if source-level reference.
	 */
	private ArtifactGroupId artifactGroupId;

	/**
	 * Artifact version. null if source-level reference.
	 */
	private ArtifactVersion artifactVersion;

	/**
	 * Constructor.
	 */
	public Reference(ModuleVersion moduleVersion, ArtifactGroupId artifactGroupId, ArtifactVersion artifactVersion) {
		this.moduleVersion = moduleVersion;
		this.artifactGroupId = artifactGroupId;
		this.artifactVersion = artifactVersion;
	}

	/**
	 * Constructor for a source-level reference.
	 *
	 * @param moduleVersion ModuleVersion.
	 */
	public Reference(ModuleVersion moduleVersion) {
		this.moduleVersion = moduleVersion;
	}

	/**
	 * @return ModuleVersion.
	 */
	public ModuleVersion getModuleVersion() {
		return this.moduleVersion;
	}

	/**
	 * @return ArtifactGroupId.
	 */
	public ArtifactGroupId getArtifactGroupId() {
		return this.artifactGroupId;
	}

	/**
	 * @return ArtifactVersion.
	 */
	public ArtifactVersion getArtifactVersion() {
		return this.artifactVersion;
	}

	/**
	 * References are often displayed to the user and need to be shown in a human-
	 * friendly and not too cryptic way.
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder;

		stringBuilder = new StringBuilder();

		if (this.moduleVersion != null) {
			stringBuilder.append(this.moduleVersion);

			if (this.artifactGroupId != null) {
				stringBuilder.append(" (").append(this.artifactGroupId).append(":").append(this.artifactVersion).append(")");
			}
		} else {
			stringBuilder.append(this.artifactGroupId).append(":").append(this.artifactVersion);
		}

		return stringBuilder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result ;

		result = 1;
		result = (prime * result) + ((this.moduleVersion == null) ? 0 : this.moduleVersion.hashCode());
		result = (prime * result) + ((this.artifactGroupId == null) ? 0 : this.artifactGroupId.hashCode());
		result = (prime * result) + ((this.artifactVersion == null) ? 0 : this.artifactVersion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object other) {
		Reference referenceOther;

		if (this == other) {
			return true;
		}

		if (!(other instanceof Reference)) {
			return false;
		}

		referenceOther = (Reference)other;


		if (this.artifactGroupId == null) {
			if (referenceOther.artifactGroupId != null) {
				return false;
			}
		} else if (!this.artifactGroupId.equals(referenceOther.artifactGroupId)) {
			return false;
		}

		if (this.artifactVersion == null) {
			if (referenceOther.artifactVersion != null) {
				return false;
			}
		} else if (!this.artifactVersion.equals(referenceOther.artifactVersion)) {
			return false;
		}

		if (this.moduleVersion == null) {
			if (referenceOther.moduleVersion != null) {
				return false;
			}
		} else if (!this.moduleVersion.equals(referenceOther.moduleVersion)) {
			return false;
		}

		return true;
	}

	/**
	 * Tests equality between two Reference without considering the Version and
	 * ArtifactVersion.
	 *
	 * This is useful when the Version of a Reference can change within a collection
	 * and we want to find the original Reference.
	 *
	 * @param other
	 * @return If the Reference is equal to referenceOther.
	 */
	public boolean equalsNoVersion(Reference referenceOther) {

		if (this == referenceOther) {
			return true;
		}

		if (this.artifactGroupId == null) {
			if (referenceOther.artifactGroupId != null) {
				return false;
			}
		} else if (!this.artifactGroupId.equals(referenceOther.artifactGroupId)) {
			return false;
		}

		if (this.moduleVersion == null) {
			if (referenceOther.moduleVersion != null) {
				return false;
			}
		} else if (!this.moduleVersion.getNodePath().equals(referenceOther.moduleVersion.getNodePath())) {
			return false;
		}

		return true;
	}
};
