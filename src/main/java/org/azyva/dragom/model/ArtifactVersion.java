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

import org.azyva.dragom.model.plugin.ArtifactVersionMapperPlugin;

/**
 * Version of an artifact produced by the build of a {@link Module}.
 * <p>
 * Implements value semantics. Instances are immutable.
 * <p>
 * Implements {@link hashCode} and {@link equals} so that instances can be used as
 * Map keys.
 * </p>
 * This class is essentially the artifact counterpart of the source-level
 * {@link Version}. One reason for having two separate classes is to help avoiding
 * type mismatch and ensuring type safety.
 * <p>
 * As for Version, two types of ArtifactVersion's are recognized:
 * <p>
 * <li>Dynamic: Refers to a state of something that can change. This type of
 *     ArtifactVersion is generally called a SNAPSHOT.</li>
 * <li>Static: Refers to a state of something that is fixed, immutable. This type
 *     of ArtifactVersion is generally called a release.</li>
 * <p>
 * An ArtifactVersion is essentially a String. This class offers additional
 * methods which handle the distinction between dynamic and static
 * ArtifactVersion's.
 * <p>
 * ArtifactVersion's are assumed to be of the form &lt;version&gt;[-SNAPSHOT]. If
 * the -SNAPSHOT suffix is present the ArtifactVersion is considered dynamic. If
 * the -SNAPSHOT suffix is not present, it is considered static.
 * <p>
 * No other assumption is made on the format of ArtifactVersion's (numeric,
 * semantic, date-based, etc.).
 * <p>
 * The -SNAPSHOT convention is borrowed from Maven so it could be said that this
 * implementation is somewhat Maven-specific. But we assume that this convention
 * is sufficiently generic to not be considered Maven-specific. Time and
 * experience will tell if this proves wrong and adjustments may be required in
 * the future to keep things clean.
 *
 * @author David Raymond
 */
public class ArtifactVersion {
	/**
	 * Suffix for dynamic ArtifactVersion.
	 */
	public static final String DYNAMIC_VERSION_SUFFIX = "-SNAPSHOT";

	private VersionType versionType;
	private String version;

	/**
	 * Constructor using the individual fields.
	 *
	 * @param versionType VersionType.
	 * @param version String part of the Version.
	 */
	public ArtifactVersion(VersionType versionType, String version) {
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
	 * Parses a Version in literal form.
	 *
	 * @param stringArtifactVersion ArtifactVersion in literal form.
	 * @return Version.
	 * @throws ParseException If parsing fails.
	 */
	public static ArtifactVersion parse(String stringArtifactVersion)
		throws ParseException {
		VersionType versionType;
		String version;

		if (stringArtifactVersion.endsWith(ArtifactVersion.DYNAMIC_VERSION_SUFFIX)) {
			versionType = VersionType.DYNAMIC;
			version = stringArtifactVersion.substring(0, stringArtifactVersion.length() - ArtifactVersion.DYNAMIC_VERSION_SUFFIX.length());
		} else {
			versionType = VersionType.STATIC;
			version = stringArtifactVersion;
		}

		if (version.length() == 0) {
			throw new ParseException("Version cannot be the empty string.", 0);
		}

		return new ArtifactVersion(versionType, version);
	}

	public VersionType getVersionType() {
		return this.versionType;
	}

	/**
	 * @return String part of the ArtifactVersion, without the {@link VersionType}
	 *   information.
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * Returns a {@link Version} corresponding to this ArtifactVersion assuming a
	 * direct equivalence between the two.
	 * <p>
	 * Note that the mapping between ArtifactVersion and Version during tool
	 * execution is generally handled by {@link ArtifactVersionMapperPlugin}. This
	 * method implements a very simple mapping which is only one possible mapping.
	 * ArtifactVersionMapperPlugin must generally be used so that module-specific
	 * mapping algorithms can be honored.
	 *
	 * @return Version.
	 */
	public Version getCorrespondingVersion() {
		return new Version(this.versionType, this.version);
	}

	/**
	 * @return ArtifactVersion in literal form.
	 */
	@Override
	public String toString() {
		switch (this.versionType) {
		case DYNAMIC:
			return this.version + ArtifactVersion.DYNAMIC_VERSION_SUFFIX;
		case STATIC:
			return this.version;
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
		ArtifactVersion versionOther;

		if (this == other) {
			return true;
		}

		if (!(other instanceof ArtifactVersion)) {
			return false;
		}

		versionOther = (ArtifactVersion)other;

		return (this.versionType == versionOther.versionType) && (this.version.equals(versionOther.version));
	}
}
