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

package org.azyva.dragom.model;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bundles the groupId, artifactId and version of an artifact.
 *
 * <p>This is essentially the same as what is commonly know as a GAV.
 *
 * <p>Implements value semantics. Instances are immutable.
 * <p>
 * Implements {@link #hashCode} and {@link #equals} so that instances can be used as
 * Map keys.
 * </p>
 * ArtifactGroupIdVersion supports the literal form
 * &lt;groupId&gt;:&lt;artifactId&gt;:&lt;version&gt;.
 *
 * @author David Raymond
 */
public final class ArtifactGroupIdVersion {
  /**
   * See description in ResourceBundle.
   */
  private static final String MSG_PATTERN_KEY_ARTIFACT_GROUP_ID_VERSION_PARSING_ERROR = "ARTIFACT_GROUP_ID_VERSION_PARSING_ERROR";

  /**
   * ResourceBundle specific to this class.
   */
  private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(ArtifactGroupIdVersion.class.getName() + "ResourceBundle");

  /**
   * Pattern for parsing an ArtifactGroupId literal.
   */
  private static final Pattern patternArtifactGroupIdLiteral = Pattern.compile("([a-zA-Z][a-zA-Z0-9\\.\\-_]*):([a-zA-Z][a-zA-Z0-9\\.\\-_]*):([a-zA-Z0-9\\.\\-_]+)(-SNAPSHOT)?");

  /**
   * ArtifactGroupId.
   */
  private ArtifactGroupId artifactGroupId;

  /**
   * ArtifactVersion.
   */
  private ArtifactVersion artifactVersion;

  /**
   * Constructor using the individual fields.
   *
   * @param groupId GroupId.
   * @param artifactId ArtifactId.
   */
  public ArtifactGroupIdVersion(ArtifactGroupId artifactGroupId, ArtifactVersion artifactVersion) {
    if ((artifactGroupId == null) || (artifactVersion == null)) {
      throw new RuntimeException("The artifactGroupId and artifactVersion cannot be null.");
    }

    this.artifactGroupId = artifactGroupId;
    this.artifactVersion = artifactVersion;
  }

  /**
   * Constructor using an ArtifactGroupIdVersion literal.
   * <p>
   * Throws RuntimeException if parsing fails.
   *
   * @param stringArtifactGroupIdVersion ArtifactGroupIdVersion literal.
   */
  public ArtifactGroupIdVersion(String stringArtifactGroupIdVersion) {
    Matcher matcher;

    matcher = ArtifactGroupIdVersion.patternArtifactGroupIdLiteral.matcher(stringArtifactGroupIdVersion);

    try {
      if (!matcher.matches()) {
        throw new ParseException(MessageFormat.format(ArtifactGroupIdVersion.resourceBundle.getString(ArtifactGroupIdVersion.MSG_PATTERN_KEY_ARTIFACT_GROUP_ID_VERSION_PARSING_ERROR), stringArtifactGroupIdVersion, ArtifactGroupIdVersion.patternArtifactGroupIdLiteral), 0);
      }

      this.artifactGroupId = new ArtifactGroupId(matcher.group(1), matcher.group(2));
      this.artifactVersion = new ArtifactVersion(matcher.group(3));
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
  public static ArtifactGroupIdVersion parse(String stringArtifactGroupId)
  throws ParseException {
    try {
      return new ArtifactGroupIdVersion(stringArtifactGroupId);
    } catch (RuntimeException re) {
      if (re.getCause() instanceof ParseException) {
        throw (ParseException)re.getCause();
      } else {
        throw re;
      }
    }
  }

  /**
   * @return GroupId.
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
   * @return ArtifactGroupId literal.
   */
  @Override
  public String toString() {
    return this.artifactGroupId.toString() + ':' + this.artifactVersion;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result;

    result = 1;
    result = (prime * result) + this.artifactGroupId.hashCode();
    result = (prime * result) + this.artifactVersion.hashCode();

    return result;
  }

  @Override
  public boolean equals(Object other) {
    ArtifactGroupIdVersion artifactGroupIdVersionOther;

    if (this == other) {
      return true;
    }

    if (!(other instanceof ArtifactGroupIdVersion)) {
      return false;
    }

    artifactGroupIdVersionOther = (ArtifactGroupIdVersion)other;

    return this.artifactGroupId.equals(artifactGroupIdVersionOther.artifactGroupId) && this.artifactVersion.equals(artifactGroupIdVersionOther.artifactVersion);
  }
}
