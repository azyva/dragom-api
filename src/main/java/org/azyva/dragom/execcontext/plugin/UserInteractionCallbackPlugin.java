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
  /**
   * Represents a handle to an indent in the output.
   */
  public static interface IndentHandle extends Closeable {
    /**
     * Closeable.close throws checked IOException which is useless and bothersome
     * in this context. We therefore redefine it without any exceptions.
     */
    @Override
    void close();
  }

  /**
   * Indents the next outputs.
   *
   * <p>Every call to startIndent must be matched by a call to
   * {@link IndentHandle#close}.
   *
   * @return IndentHandle.
   */
  IndentHandle startIndent();

  /**
   * Provides information information to the user at a new indentation level.
   *
   * @param info Information.
   */
  void provideInfo(String info);

  /**
   * Provides information to the user in a way similar to {@link #provideInfo}, but
   * returns a Writer so that the caller can append additional information.
   * <p>
   * The caller is responsible for closing the Writer when done so that the plugin
   * is able to associate all of the additional information with the initial call to
   * this method. Calling close in this case should not close some underlying stream
   * in such a way that would render the system unstable. Specifically if the
   * Writer delegates to System.out, it must not delegate the close call. This use
   * of close is specific to the contract with this class and does not necessarily
   * respect the general contract of Writer.close.
   * <p>
   * No call to other methods of this plugin are allowed as long as the Writer is
   * not closed.
   *
   * @param info See similar parameter for provideInfo.
   * @return Writer.
   */
  Writer provideInfoWithWriter(String info);

  /**
   * Obtains information from the user.
   *
   * <p>Only allowed if not {@link #isBatchMode}.
   *
   * @param prompt Prompt.
   * @return Information.
   */
  String getInfo(String prompt);

  /**
   * Similar to {@link #getInfo}, but if the user enters nothing, the provided
   * default value is returned.
   *
   * @param prompt Prompt.
   * @param defaultValue Default value.
   * @return Information.
   */
  String getInfoWithDefault(String prompt, String defaultValue);

  /**
   * Obtains sensitive information from the user which should not be echoed back.
   * Generally used for passwords.
   *
   * @param prompt Prompt.
   * @return Information.
   */
  String getInfoPassword(String prompt);

  /**
   * @return Indicates if the plugin, which essentially is the interaction point with
   *   the user, operates in batch mode, meaning that calls to {@link #getInfo} and
   *   {@link #getInfoWithDefault} are not allowed. Generally, caller should not
   *   worry about this and let these method fail if in batch mode. But some
   *   classes, such as DefaultCredetialStorePluginImpl, may need to know whether
   *   batch mode is enabled.
   */
  boolean isBatchMode();
}
