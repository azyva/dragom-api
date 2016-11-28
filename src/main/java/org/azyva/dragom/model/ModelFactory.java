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

package org.azyva.dragom.model;

import java.util.Properties;

import org.azyva.dragom.execcontext.ExecContextFactory;

/**
 * Factory for getting a {@link Model}.
 * <p>
 * The factory design pattern is used here to allow various Model implementations,
 * although it is expected that in most cases DefaultModelFactory in dragom-core
 * will be adequate.
 * <p>
 * To provide a high degree of flexibility, initialization Properties are passed
 * to the getModel method. The caller (generally an {@link ExecContextFactory}
 * that is itself invoked by the initialization code of a tool) is free to provide
 * Properties as required, enabling the passing of initialization data from the
 * outside to the Model creation process.
 * <p>
 * A typical strategy is to provide a cascade of Properties initialized from
 * various sources, such as a Properties file in the classpath, system Properties
 * and user properties specified at tool invocation time. Such a strategy is
 * implemented by Util.setupExecContext in dragom-core which many tools use. But
 * ModelFactory implementations should not care about such a strategy. They should
 * simply read properties from the provided initialization Properties.
 *
 * @author David Raymond
 */
public interface ModelFactory {
	/**
	 * Returns a {@link Model}.
	 *
	 * @param propertiesInit Initialization properties.
	 * @return Model.
	 */
	Model getModel(Properties propertiesInit);
}
