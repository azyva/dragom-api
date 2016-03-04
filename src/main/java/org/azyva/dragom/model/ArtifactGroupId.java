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


/**
 * Bundles the groupId and artifactId of an artifact.
 * <p>
 * The version is not included.
 * <p>
 * Implements value semantics. Instances are immutable.
 * <p>
 * Implements {@link hashCode} and {@link equals} so that instances can be used as
 * Map keys.
 * </p>
 * ArtifactGroupId supports the literal form &lt;groupId&gt;:&lt;artifactId&gt;.
 *
 * @author David Raymond
 */
public final class ArtifactGroupId {
	private String groupId;
	private String artifactId;

	/**
	 * Constructor using the individual fields.
	 *
	 * @param groupId GroupId.
	 * @param artifactId ArtifactId.
	 */
	public ArtifactGroupId(String groupId, String artifactId) {
		if ((groupId == null) || (artifactId == null)) {
			throw new RuntimeException("The groupId and artifactId cannot be null.");
		}

		this.groupId = groupId;
		this.artifactId = artifactId;
	}

	/**
	 * Parses an ArtifactGroupId in literal form.
	 *
	 * @param stringArtifactGroupId ArtifactVersion in literal form.
	 * @return Version.
	 * @throws ParseException If parsing fails.
	 */
	public static ArtifactGroupId parse(String stringArtifactGroupId)
		throws ParseException {
		int colonPos;

		colonPos = stringArtifactGroupId.indexOf(':');

		if (colonPos == -1) {
			throw new ParseException("The string representation of an ArtifactGroupId " + stringArtifactGroupId + " is not valid as it does not contain \":\".", 0);
		}

		return new ArtifactGroupId(stringArtifactGroupId.substring(0, colonPos), stringArtifactGroupId.substring(colonPos + 1));
	}

	public String getGroupId() {
		return this.groupId;
	}

	public String getArtifactId() {
		return this.artifactId;
	}

	/**
	 * @return ArtifactGroupId in literal form.
	 */
	@Override
	public String toString() {
		return this.groupId + ":" + this.artifactId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result;

		result = 1;
		result = (prime * result) + this.artifactId.hashCode();
		result = (prime * result) + this.groupId.hashCode();

		return result;
	}

	@Override
	public boolean equals(Object other) {
		ArtifactGroupId artifactGroupIdOther;

		if (this == other) {
			return true;
		}

		if (!(other instanceof ArtifactGroupId)) {
			return false;
		}

		artifactGroupIdOther = (ArtifactGroupId)other;

		return this.groupId.equals(artifactGroupIdOther.groupId) && this.artifactId.equals(artifactGroupIdOther.artifactId);
	}
}
