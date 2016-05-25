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
import org.azyva.dragom.model.plugin.NodePlugin;
import org.azyva.dragom.model.plugin.ReferenceManagerPlugin;

/**
 * Class representing a reference.
 * <p>
 * Although this class is final so that it cannot be derived, extra implementation
 * data can be attached using the implData property. This is useful since
 * References are generally specific to some build tool and build-tool-specific
 * {@link NodePlugin}'s such as {@link ReferenceManagerPlugin} may need to include
 * extra implementation data.
 * <p>
 * When implData is specified, it is used in the following way by this class:
 * <p>
 * <li>It is included in equality tests (equals and equalsNoVersion method). The
 *     equals method should not consider any version information so that it can be
 *     used by equals and equalsNoVersion. If ever extra implementation data
 *     contains version information, it should be the same as that maintained by
 *     this class, which considers it for equals;</li>
 * <li>Its hashCode is used to compute this class' hashCode;</li>
 * <li>Its string representation (toString method) is included in the string
 * representation of this class, unless it is null or the empty string.</li>
 * <p>
 * This class implements value semantics and is immutable.
 */
public final class Reference {
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
	 * Extra implementation data attached to the Reference.
	 */
	private Object implData;

	/**
	 * Constructor.
	 */
	public Reference(ModuleVersion moduleVersion, ArtifactGroupId artifactGroupId, ArtifactVersion artifactVersion, Object implData) {
		this.moduleVersion = moduleVersion;
		this.artifactGroupId = artifactGroupId;
		this.artifactVersion = artifactVersion;
		this.implData = implData;
	}

	/**
	 * Constructor with no extra implementation data.
	 */
	public Reference(ModuleVersion moduleVersion, ArtifactGroupId artifactGroupId, ArtifactVersion artifactVersion) {
		this.moduleVersion = moduleVersion;
		this.artifactGroupId = artifactGroupId;
		this.artifactVersion = artifactVersion;
	}

	/**
	 * Constructor for a source-level reference, or when artifact-level
	 * reference is not important.
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
	 * @return Extra implementation data.
	 */
	public Object getImplData() {
		return this.implData;
	}

	/**
	 * Reference's are often displayed to the user and need to be shown in a human-
	 * friendly and not too cryptic way.
	 * <p>
	 * When Reference's are shown to the user they are generally as part of a
	 * {@link ReferencePath} which puts the Reference's in context.
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder;

		stringBuilder = new StringBuilder();

		if (this.implData != null) {
			String stringImplData;

			stringImplData = this.implData.toString();

			if ((stringImplData != null) && (stringImplData.length() != 0)) {
				stringBuilder.append("[").append(stringImplData).append("] ");
			}
		}

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
		result = (prime * result) + ((this.implData == null) ? 0 : this.implData.hashCode());

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

		if (this.implData == null) {
			if (referenceOther.implData != null) {
				return false;
			}
		} else if (!this.implData.equals(referenceOther.implData)) {
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

		if (this.implData == null) {
			if (referenceOther.implData != null) {
				return false;
			}
		} else if (!this.implData.equals(referenceOther.implData)) {
			// The equals method of the extra implementation data should never consider the
			// version.
			return false;
		}

		return true;
	}
};
