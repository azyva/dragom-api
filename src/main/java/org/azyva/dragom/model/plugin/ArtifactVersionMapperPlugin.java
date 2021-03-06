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

import org.azyva.dragom.model.ArtifactVersion;
import org.azyva.dragom.model.Version;

/**
 * Maps artifact versions to and from global versions (Version) known to module
 * manager.
 *
 * @author David Raymond
 */
public interface ArtifactVersionMapperPlugin extends ModulePlugin {
  /**
   * Maps an ArtifactVersion to a Version.
   *
   * @param artifactVersion ArtifactVersion.
   * @return Version.
   */
  Version mapArtifactVersionToVersion(ArtifactVersion artifactVersion);

  /**
   * Maps a Version to an ArtifactVersion.
   *
   * @param version Version.
   * @return ArtifactVersion.
   */
  ArtifactVersion mapVersionToArtifactVersion(Version version);
}
