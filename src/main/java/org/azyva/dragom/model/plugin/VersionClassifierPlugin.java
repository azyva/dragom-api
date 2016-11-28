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

package org.azyva.dragom.model.plugin;

import java.util.Comparator;

import org.azyva.dragom.model.Module;
import org.azyva.dragom.model.Version;

/**
 * Allows classifying the {@link Version}'s of the {@link Module}.
 * <p>
 * Note that Version's generally represent source-level versions, or tags, as
 * opposed to artifact-level versions. That is so since Dragom is designed to work
 * more at the source level than at the artifact level and provides mapping
 * mechanisms between the two versioning contexts
 * ({@link ArtifactVersionMapperPlugin}).
 * <p>
 * Among others, Version classification is used in reference graph reports.
 * <p>
 * One way Version's can be classified is by establishing an order among them so
 * that we can know if a given Version is greater or less than another Version,
 * the meaning of this order being generally related to the set of changes
 * included, and to some extent to the date at which the Version's were created,
 * although a more recently created Version does not necessarily contain more
 * changes than others (e.g., hotfix changes).
 * <p>
 * For example S/v-1.3 &gt; S/v-1.2.3 &gt; S/v-1.2.2 generally holds true.
 * <p>
 * Different versioning strategies can be used for different {@link Module}'s so
 * that different Version classifications strategies can be used.
 * <p>
 * The distinction that Dragom makes between static and dynamic Version's is a
 * form of classification that is global and embedded in the Version itself, not
 * specific to Module's. But this plugin needs to consider these two general types
 * of Version's. It is up to the plugin implementation to use a strategy that is
 * meaningful to users, even if mixed comparisons between static and dynamic
 * Versions does not always make complete sense.
 * <p>
 * For example, saying that all dynamic Version's are greater than static
 * Version's generally leads to meaningful results, since we understand that new
 * changes can always be added to dynamic Version's. But then again, if different
 * evolution paths exist for a given Module, as is often the case for Web services
 * for which multiple major Version's are often concurrently maintained, a
 * static Version of a more recent evolution path could be considered greater than
 * a dynamic Version of an older evolution path.
 * <p>
 * This interface extends {@code Comparator<Version>} to make it easy to use it in
 * conjunction with the Java Collections framework, such as to perform sort
 * operations.
 *
 * @author David Raymond
 */

public interface VersionClassifierPlugin extends ModulePlugin, Comparator<Version> {
	/**
	 * Compares two {@link Version}'s.
	 *
	 * 0 should be returned if and only if version1.equals(version2). That is,
	 * Version equality should not be dependent on the classification strategy.
	 *
	 * @param version1 First Version.
	 * @param version2 Second Version.
	 * @return -1 if version1 &lt; version2, 1 if version1 &gt; versions2 and 0 if
	 *   version1 = version2.
	 */
	@Override
	int compare(Version version1, Version version2);

	/**
	 * Returns the evolution path associated with a Version.
	 * <p>
	 * Currently Dragom does not manage evolution paths. But it is expected that
	 * this information be eventually exploited by some new functionality.
	 *
	 * @param version Version.
	 * @return Evolution path. null is an acceptable value if, for exemple,
	 *   evolution paths are not supported.
	 */
	String getEvolutionPath(Version version);

	/**
	 * Compares two evolution paths.
	 * <p>
	 * Currently Dragom does not manage evolution paths. But it is expected that
	 * this information be eventually exploited by some new functionnality.
	 *
	 * @param evolutionPath1 First evolution path.
	 * @param evolutionPath2 Second evolution path.
	 * @return -1 if evolutionPath1 &lt; evolutionPath2,
	 *   1 if evolutionPath1 &gt; evolution Path2 and 0 if
	 *   evolutionPath1 = evolutionPath2. If evolution paths are not supported,
	 *   java.lang.UnsupportedOperationException can be raised.
	 */
	int compareEvolutionPaths(String evolutionPath1, String evolutionPath2);
}
