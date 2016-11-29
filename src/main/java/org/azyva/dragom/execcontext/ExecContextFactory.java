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

import java.util.Properties;

/**
 * Factory for getting an {@link ExecContext}.
 * <p>
 * The factory design pattern is used here to allow various ExecContext
 * implementations, although it is expected that in most cases
 * DefaultExecContextFactory from dragom-core will be adequate.
 * <p>
 * To provide a high degree of flexibility, initialization Properties are passed
 * to the getExecContext method. The caller (the initialization code of a tool) is
 * free to provide Properties as required, enabling the passing of initialization
 * data from outside to the ExecContext creation process.
 * <p>
 * A typical strategy is to provide a cascade of Properties initialized from
 * various sources, such as a Properties file in the classpath, system Properties
 * and user properties specified at tool invocation time. Such a strategy is
 * implemented by Util.setupExecContext from dragom-core which many tools use. But
 * ExecContextFactory implementations should not care about such a strategy. They
 * should simply read properties from the provided initialization Properties.
 * <p>
 * In most cases ExecContext support the workspace directory concept.
 * To indicate that the ExecContext's implemented by an ExecContextFactory
 * implementation support the workspace directory concept it should implement
 * {@link WorkspaceExecContextFactory}, on top of ExecContextFactory. Similarly,
 * the corresponding ExecContext's should implement {@link WorkspaceExecContext}.
 *
 * @author David Raymond
 */
public interface ExecContextFactory {
  /**
   * Returns an {@link ExecContext}.
   *
   * @param propertiesInit Initialization properties.
   * @return ExecContext.
   */
  ExecContext getExecContext(Properties propertiesInit);
}
