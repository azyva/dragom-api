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

import java.io.Closeable;
import java.io.Writer;

/**
 * Allows interacting with the user during the execution of a job:
 *
 * - Providing information to the user
 * - Requesting information from the user
 *
 * General parameters for jobs that are pertinent no matter the modules are meant
 * to be provided as such, parameters.
 *
 * But depending on the situation, tools and plugins may require information that is
 * too specific to be provided up front to the job by the user. In such a case an
 * implementation of this interface can be used to request information from the
 * user during job execution.
 *
 * Calls to the getInfo method are generally meant to actually prompt the user for
 * information. But nothing prevents an implementation from gathering some
 * information from the user up front and providing this information appropriately
 * when getInfo is called.
 *
 * An implementation of this interface is meant to be set with
 * ExecContext.setUserInteractionCallback.
 *
 * @author David Raymond
 */

public interface UserInteractionCallbackPlugin extends ExecContextPlugin {
	public static interface BracketHandle extends Closeable {
	}

	BracketHandle startBracket(String idInfo, Object... arrayParam);

	void provideInfo(String idInfo, Object... arrayParam);

	/**
	 * Provides information to the user in a way similar to {@link #provideInfo}, but
	 * returns a Writer so that the caller can append additional information.
	 * <p>
	 * The caller is responsible for closing the Writer when done so that the plugin
	 * is able to associate all of the additional information with with the initial
	 * call to this method. Calling close in this case should not close some
	 * underlying stream in such a way that would render the system unstable. In
	 * particular if the Writer delegates to System.out, it must not delegate the
	 * close call. This use of close if specific to the contract with this class and
	 * does not necessarily respect the general contract of Writer.close.
	 * <p>
	 * No call to other methods of this plugin are allowed as long as the Writer is
	 * not closed.
	 *
	 * @param idInfo See similar parameter for provideInfo.
	 * @param arrayParam See similar parameter for provideInfo.
	 * @return Writer.
	 */
	Writer provideInfoWithWriter(String idInfo, Object... arrayParam);

	String getInfo(String idInfo, Object... arrayParam);

	String getInfoWithDefault(String idInfo, String defaultValue, Object... arrayParam);
}
