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

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	/**
	 * See description in ResourceBundle.
	 */
	private static final String MSG_PATTERN_KEY_ARTIFACT_GROUP_ID_PARSING_ERROR = "ARTIFACT_GROUP_ID_PARSING_ERROR";

	/**
	 * ResourceBundle specific to this class.
	 */
	private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(ArtifactGroupId.class.getName() + "ResourceBundle");

	/**
	 * Pattern for parsing an ArtifactGroupId literal.
	 */
	private static Pattern patternArtifactGroupIdLiteral = Pattern.compile("([a-zA-Z][a-zA-Z0-9\\.\\-_]*):([a-zA-Z][a-zA-Z0-9\\.\\-_]*)");

	/**
	 * GroupId.
	 */
	private String groupId;

	/**
	 * ArtifactId.
	 */
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
	 * Constructor using an ArtifactGroupId literal.
	 * <p>
	 * Throws RuntimeException if parsing fails.
	 *
	 * @param stringArtifactGroupId ArtifactGroupId literal.
	 */
	public ArtifactGroupId(String stringArtifactGroupId) {
		Matcher matcher;

		matcher = ArtifactGroupId.patternArtifactGroupIdLiteral.matcher(stringArtifactGroupId);

		try {
			if (!matcher.matches()) {
				throw new ParseException(MessageFormat.format(ArtifactGroupId.resourceBundle.getString(ArtifactGroupId.MSG_PATTERN_KEY_ARTIFACT_GROUP_ID_PARSING_ERROR), stringArtifactGroupId, ArtifactGroupId.patternArtifactGroupIdLiteral), 0);
			}

			this.groupId = matcher.group(1);
			this.artifactId = matcher.group(2);
		} catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}

	/**
	 * Parses an ArtifactGroupId literal.
	 *
	 * @param stringArtifactGroupId ArtifactVersion literal.
	 * @return Version.
	 * @throws ParseException If parsing fails.
	 */
	public static ArtifactGroupId parse(String stringArtifactGroupId)
	throws ParseException {
		try {
			return new ArtifactGroupId(stringArtifactGroupId);
		} catch (RuntimeException re) {
			if (re.getCause() instanceof ParseException) {
				throw (ParseException)re.getCause();
			} else {
				throw re;
			}
		}
	}

	public String getGroupId() {
		return this.groupId;
	}

	public String getArtifactId() {
		return this.artifactId;
	}

	/**
	 * @return ArtifactGroupId literal.
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
