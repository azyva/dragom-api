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

import org.azyva.dragom.model.plugin.ArtifactVersionMapperPlugin;
import org.azyva.dragom.model.plugin.ScmPlugin;

/**
 * Version of a {@link Module} at the source level.
 * <p>
 * Implements value semantics. Instances are immutable.
 * <p>
 * Implements {@link hashCode} and {@link equals} so that instances can be used as
 * Map keys.
 * </p>
 * Two types of Version's are recognized:
 * <p>
 * <li>Dynamic: Refers to a state of something that can change. This type of
 *     Version is generally called a branch.</li>
 * <li>Static: Refers to a state of something that is fixed, immutable. This type
 *     of Version is generally called a tag.</li>
 * <p>
 * Version support the literal form [D|S]/&lt;version&gt;, where the D or S
 * prefix differentiates between a dynamic and static Version. This format is
 * Dragom-specific and SCM independent. It is up to {@link ScmPlugin} to interpret
 * Version's in a way that can be handled by the SCM. For example a Version such
 * as D/develop-my-project could be mapped to refs/heads/develop-my-project for
 * Git and branches/develop-my-project for Subversion.
 *
 * @author David Raymond
 */
public final class Version {
	/**
	 * See description in ResourceBundle.
	 */
	public static final String MSG_PATTERN_KEY_VERSION_PARSING_ERROR = "MODULE_VERSION_PARSING_ERROR";

	/**
	 * ResourceBundle specific to this class.
	 */
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle(Version.class.getName());

	/**
	 * Pattern for parsing a Version literal.
	 */
	private static Pattern patternVersionLiteral = Pattern.compile("(D|S)/([a-zA-Z0-9\\.\\-_]+)");

	/**
	 * VersionType.
	 */
	private VersionType versionType;

	/**
	 * Version string.
	 */
	private String version;

	/**
	 * Constructor using the individual fields.
	 *
	 * @param versionType VersionType.
	 * @param version String part of the Version.
	 */
	public Version(VersionType versionType, String version) {
		if ((versionType == null) || (version == null)) {
			throw new RuntimeException("Version type or version cannot be null.");
		}

		if (version.length() == 0) {
			throw new RuntimeException("Version cannot be the empty string.");
		}

		this.versionType = versionType;
		this.version = version;
	}

	/**
	 * Constructor using a Version literal.
	 * <p>
	 * Throws RuntimeException if parsing fails.
	 *
	 * @param stringVersion Version literal.
	 */
	public Version(String stringVersion) {
		Matcher matcher;

		matcher = Version.patternVersionLiteral.matcher(stringVersion);

		try {
			if (!matcher.matches()) {
				throw new ParseException(MessageFormat.format(Version.resourceBundle.getString(Version.MSG_PATTERN_KEY_VERSION_PARSING_ERROR), stringVersion, Version.patternVersionLiteral), 0);
			}

			if (matcher.group(1).charAt(0) == 'D') {
				this.versionType = VersionType.DYNAMIC;
			} else {
				this.versionType = VersionType.STATIC;
			}

			this.version = matcher.group(2);
		} catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}

	/**
	 * Parses a Version literal.
	 *
	 * @param stringVersion Version literal.
	 * @return Version.
	 * @throws ParseException If parsing fails.
	 */
	public static Version parse(String stringVersion)
	throws ParseException {
		try {
			return new Version(stringVersion);
		} catch (RuntimeException re) {
			if (re.getCause() instanceof ParseException) {
				throw (ParseException)re.getCause();
			} else {
				throw re;
			}
		}
	}

	public VersionType getVersionType() {
		return this.versionType;
	}

	/**
	 * @return String part of the Version, without the {@link VersionType}
	 *   information.
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * Returns an {@link ArtifactVersion} corresponding to this Version assuming a
	 * direct equivalence between the two.
	 * <p>
	 * Note that the mapping between Version and ArtifactVersion during tool
	 * execution is generally handled by {@link ArtifactVersionMapperPlugin}. This
	 * method implements a very simple mapping which is only one possible mapping.
	 * ArtifactVersionMapperPlugin must generally be used so that module-specific
	 * mapping algorithms can be honored.
	 *
	 * @return ArtifactVersion.
	 */
	public ArtifactVersion getCorrespondingArtifactVersion() {
		return new ArtifactVersion(this.versionType, this.version);
	}

	/**
	 * @return Version literal.
	 */
	@Override
	public String toString() {
		switch (this.versionType) {
		case DYNAMIC:
			return "D/" + this.version;
		case STATIC:
			return "S/" + this.version;
		default:
			throw new RuntimeException("Invalid version type " + this.versionType + '.');
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result;

		result = 1;
		result = (prime * result) + this.versionType.hashCode();
		result = (prime * result) + this.version.hashCode();

		return result;
	}

	@Override
	public boolean equals(Object other) {
		Version versionOther;

		if (this == other) {
			return true;
		}

		if (!(other instanceof Version)) {
			return false;
		}

		versionOther = (Version)other;

		return (this.versionType == versionOther.versionType) && (this.version.equals(versionOther.version));
	}
}
