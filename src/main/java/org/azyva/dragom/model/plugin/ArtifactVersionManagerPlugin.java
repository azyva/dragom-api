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

import java.nio.file.Path;

import org.azyva.dragom.model.ArtifactVersion;
import org.azyva.dragom.model.Module;

/**
 * Allows managing {@link ArtifactVersion} in the source code of a {@link Module}.
 *
 * <p>A Module implementing this plugin implies that its ArtifactVersion is stored
 * in the sourde code.
 *
 * @author David Raymond
 */
public interface ArtifactVersionManagerPlugin extends ModulePlugin {
  /**
   * Returns the ArtifactVersion of a {@link Module} whose source code is in a given
   * path.
   *
   * @param pathModuleWorkspace Path to the Module.
   * @return See description.
   */
  ArtifactVersion getArtifactVersion(Path pathModuleWorkspace);

  /**
   * Sets the ArtifactVersion in the source code of a {@link Module}.
   *
   * @param pathModuleWorkspace Path to the Module.
   * @param artifactVersion ArtifactVersion.
   * @return Indicates if the source code was actually changed. false is returned
   *   if the Module already had the specified ArtifactVersion.
   */
  boolean setArtifactVersion(Path pathModuleWorkspace, ArtifactVersion artifactVersion);
}
