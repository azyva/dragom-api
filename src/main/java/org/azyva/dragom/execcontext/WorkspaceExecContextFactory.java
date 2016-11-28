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

/**
 * Interface implemented by ExecContextFactory implementations that support the
 * workspace directory concept.
 *
 * @author David Raymond
 */
public interface WorkspaceExecContextFactory {
	/**
	 * Initialization property that specifies the path of the root workspace
	 * directory.
	 * <p>
	 * Such an initialization property is expected to be provided to
	 * {@link ExecContextFactory#getExecContext} in order to specify where the
	 * workspace directory is. If not, the {@link ExecContextFactory} is expected to
	 * use the current working directory as the workspace directory.
	 *
	 * @return See description.
	 */
	String getWorkspaceDirInitProperty();
}
