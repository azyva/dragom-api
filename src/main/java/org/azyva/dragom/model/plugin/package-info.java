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

/*
 * Contains the interfaces for the various {@link NodePlugin}'s. Other
 * NodePlugin's can be introduced by clients.
 * <p>
 * Tools and NodePlugin's collaborate in providing required functionality.
 * Generally, tools are responsible for the high-level algorithms expressed using
 * lower-level abstractions provided by NodePlugin's. For example,
 * {@link org.azyva.dragom.tool.SwithToDynamicVersionTool} manages the navigation
 * within a {@link org.azyva.dragom.model.Module}
 * {@link org.azyva.dragom.reference.Reference} graph and the calls for creating
 * the dynamic {@link org.azyva.dragom.model.Version}'s and for updating the
 * Reference's, while NodePlugin's are used to get the source code (@link
 * org.azyva.dragom.model.plugin.ScmPlugin}, established the new Version's
 * ({@link org.azyva.dragom.model.plugin.NewDynamicVersionPlugin}, etc.
 */

package org.azyva.dragom.model.plugin;
