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

import java.util.Set;

import org.azyva.dragom.model.ArtifactGroupId;

/**
 * Provides artifact-related information about the module.
 *
 * A module providing this plugin implies that building this module produces
 * artifacts identified by a groupId and artifactId (and version) that can
 * be deployed to an artifact repository.
 *
 * The build of any module produces artifacts, but such artifacts are not
 * necessarily meant to be deployed to an artifact repository and are not identified
 * by a groupId and artifactId. Such modules will not provide this plugin.
 *
 * A module providing this plugin does not necessarily imply that the version of
 * the artifacts produced is specified within the module source code, although that
 * is the most common case (when Maven is used as the build tool). This capability
 * is related to the module providing the ArtifactVersionManagerPlugin plugin.
 *
 * @author David Raymond
 */

public interface ArtifactInfoPlugin extends ModulePlugin {
  /**
   * Indicates if builds of this module produce artifacts with a given
   * ArtifactGroupId.
   *
   * @param artifactGroupId See description.
   * @return See description.
   */
  boolean isArtifactGroupIdProduced(ArtifactGroupId artifactGroupId);

  /**
   * Indicates if builds of this module possibly produce artifacts with a given
   * ArtifactGroupId.
   *
   * This method returns true when isArtifactGroupIdProduced returns true for
   * the same ArtifactGroupId.
   *
   * @param artifactGroupId See description.
   * @return See description.
   */
  boolean isArtifactGroupIdPossiblyProduced(ArtifactGroupId artifactGroupId);

  /**
   * Gets the set of all ArtifactGroupId produced by builds of this module.
   *
   * Only ArtifactGroupId that are definitively produced are returned.
   * isArtifactGroupIdProduced returned true for these ArtifactGroupId and all
   * ArtifactGroupId for which isArtifactGroupIdProduced returned true are returned
   * by this method.
   *
   * Builds of the module can produce artifacts as defined within its build script
   * and that cannot be known statically. Such ArtifactGroupId are not returned.
   * However the method isArtifactGroupIdPossiblyProduced returned true for such
   * potentially produced ArtifactGroupId.
   *
   * @return See description.
   */
  Set<ArtifactGroupId> getSetDefiniteArtifactGroupIdProduced();
}
