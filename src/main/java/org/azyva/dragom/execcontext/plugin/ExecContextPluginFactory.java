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

package org.azyva.dragom.execcontext.plugin;

import org.azyva.dragom.execcontext.ExecContext;

/**
 * Factory for getting an {@link ExecContextPlugin}.
 * <p>
 * The factory design pattern is used here to allow various ExecContextPlugin
 * implementations, although it is expected that in most cases the
 * default ExecContextPlugin implementations provided by
 * {@link ExecContextPluginHolder} will be adequate.
 *
 * @author David Raymond
 * @param <ExecContextPluginInterface> ExecContextPlugin sub-interface to be
 *   returned by the ExecContextPluginFactory.
 */
public interface ExecContextPluginFactory<ExecContextPluginInterface extends ExecContextPlugin> {
	/**
	 * Returns an instance of the {@link ExecContextPlugin}.
	 * <p>
	 * ExecContextPlugin's can hold on to the {@link ExecContext} the is passed as a
	 * parameter as it is garanteed that the same ExecContext will remain used
	 * throughout the life of the ExecContextPlugin. But in general,
	 * {@link ExecContextHolder#get} should be used to obtain the ExecContext at
	 * runtime.
	 *
	 * @param execContext ExecContext in the context of which the ExecContextPlugin is
	 *   created.
	 * @return ExecContextPlugin.
	 */
	ExecContextPluginInterface getExecContextPlugin(ExecContext execContext);
}
