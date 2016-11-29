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

package org.azyva.dragom.execcontext;

import java.nio.file.Path;

import org.azyva.dragom.execcontext.plugin.WorkspacePlugin;

/**
 * Interface implemented by ExecContext implementations that support the workspace
 * directory concept.
 * <p>
 * It is expected that a {@link WorkspacePlugin} implementation manage workspace
 * data and that such plugin implementations require a WorkspaceExecContext. In
 * order to support long-lived workspaces, WorkspaceExecContext supports the
 * of workspace format and version that can be used by WorkspacePlugin
 * implementations to validate that existing workspaces are usable.
 * <p>
 * It is up to WorkspacePlugin implementations to recognize various workspace
 * formats and versions and permit workspace format and version migration.
 *
 * @author David Raymond
 */
public interface WorkspaceExecContext {
  /**
   * Workspace format and version.
   */
  public static class WorkspaceFormatVersion {
    /**
     * Workspace format.
     */
    public String format;

    /**
     * Workspace version.
     */
    public String version;

    /**
     * Constructor.
     *
     * @param format Format.
     * @param version Version.
     */
    public WorkspaceFormatVersion(String format, String version) {
      this.format = format;
      this.version = version;
    }

    @Override
    public String toString() {
      return this.format + ':' + this.version;
    }
  }

  /**
   * @return Path to the workspace directory associated with the ExecContext.
   */
  Path getPathWorkspaceDir();

  /**
   * @return Path to a directory within the workspace directory that contains
   *   metadata about the workspace. Plugins and tools can use this directory to
   *   store metadata that should remain persisted with the workspace.
   */
  Path getPathMetadataDir();

  /**
   * @return WorkspaceFormatVersion. null if workspace has not been initialized.
   */
  WorkspaceFormatVersion getWorkspaceFormatVersion();

  /**
   * @param workspaceFormatVersion WorkspaceFormatVersion.
   */
  void setWorkspaceFormatVersion(WorkspaceFormatVersion workspaceFormatVersion);
}
