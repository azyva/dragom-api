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

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.azyva.dragom.execcontext.plugin.WorkspaceDirUserModuleVersion;
import org.azyva.dragom.execcontext.plugin.WorkspacePlugin;
import org.azyva.dragom.model.plugin.ScmPlugin;

/**
 * Represents a specific {@link Version} of a {@link Module}. Includes a
 * {@link NodePath} (which cannot not be partial) and a Version.
 * <p>
 * Implements value semantics. Instances are immutable.
 * <p>
 * Implements {@link #hashCode} and {@link #equals} so that instances can be used as
 * Map keys.
 * <p>
 * ModuleVersion support the literal form
 * &lt;NodePath&gt;:&lt;Version&gt; where
 * &lt;NodePath&gt; is the NodePath in String
 * format and &lt;Version&gt; is the Version in literal form.
 * <p>
 * Example: Acme/module:D/master

 * @author David Raymond
 */
public final class ModuleVersion {
  /**
   * See description in ResourceBundle.
   */
  private static final String MSG_PATTERN_KEY_MODULE_VERSION_PARSING_ERROR = "MODULE_VERSION_PARSING_ERROR";

  /**
   * ResourceBundle specific to this class.
   */
  private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(ModuleVersion.class.getName() + "ResourceBundle");

  /**
   * Pattern for parsing an ModuleVersion literal.
   */
  private static final Pattern patternModuleVersionLiteral = Pattern.compile("([^:]+):([^:]+)");

  /**
   * NodePath.
   */
  private NodePath nodePath;

  /**
   * Verseion.
   */
  private Version version;

  /**
   * Constructor using the individual fields.
   *
   * @param nodePath NodePath. Cannot be partial.
   * @param version Version. Can be null to represent the default Version of a
   *   {@link Module}, or simply not include a Version.
   */
  public ModuleVersion(NodePath nodePath, Version version) {
    if ((nodePath == null) || (version == null)) {
      throw new RuntimeException("NodePath and Version cannot be null.");
    }

    if (nodePath.isPartial()) {
      throw new RuntimeException("The NodePath " + nodePath + " must not be partial.");
    }

    this.nodePath = nodePath;
    this.version = version;
  }

  /**
   * Constructor using only a {@link NodePath}, with a null {@link Version}.
   *
   * <p>Such a ModuleVersion is incomplete. It can be used in
   * {@link WorkspaceDirUserModuleVersion} and
   * {@link WorkspacePlugin#getSetWorkspaceDir} to get all workspace directories for
   * a given {@link Module}.
   *
   * <p>Note that such a ModuleVersion is a special case and cannot be parsed from
   * a literal.
   *
   * @param nodePath NodePath. Cannot be partial.
   */
  // TODO: It is not sure this concept of an incomplete ModuleVersion should be
  // kept. It does not sound clean.
  public ModuleVersion(NodePath nodePath) {
    if (nodePath == null) {
      throw new RuntimeException("NodePath cannot be null.");
    }

    if (nodePath.isPartial()) {
      throw new RuntimeException("The NodsePath " + nodePath + " must not be partial.");
    }

    this.nodePath = nodePath;
  }

  /**
   * Constructor using a ModuleVersion literal.
   * <p>
   * Throws RuntimeException if parsing fails.
   *
   * @param stringModuleVersion ModuleVersion literal.
   */
  public ModuleVersion(String stringModuleVersion) {
    Matcher matcher;

    matcher = ModuleVersion.patternModuleVersionLiteral.matcher(stringModuleVersion);

    try {
      if (!matcher.matches()) {
        throw new ParseException(MessageFormat.format(ModuleVersion.resourceBundle.getString(ModuleVersion.MSG_PATTERN_KEY_MODULE_VERSION_PARSING_ERROR), stringModuleVersion, ModuleVersion.patternModuleVersionLiteral), 0);
      }

      this.nodePath = NodePath.parse(matcher.group(1));

      if (matcher.group(2) != null) {
        try {
          this.version = Version.parse(matcher.group(2));
        } catch (ParseException pe) {
          throw new ParseException(pe.getMessage(), pe.getErrorOffset() + matcher.start(2));
        }
      }
    } catch (ParseException pe) {
      throw new RuntimeException(pe);
    }
  }

  /**
   * Parses a ModuleVersion literal.
   *
   * @param stringModuleVersion Version literal.
   * @return Version.
   * @throws ParseException If parsing fails.
   */
  public static ModuleVersion parse(String stringModuleVersion)
  throws ParseException {
    try {
      return new ModuleVersion(stringModuleVersion);
    } catch (RuntimeException re) {
      if (re.getCause() instanceof ParseException) {
        throw (ParseException)re.getCause();
      } else {
        throw re;
      }
    }
  }

  /**
   * @return NodePath.
   */
  public NodePath getNodePath() {
    return this.nodePath;
  }

  /**
   * Returns the {@link Version}.
   * <p>
   * Can be null in some cases to represent the default Version of a Module as
   * defined by the {@link ScmPlugin}.
   *
   * @return See description.
   */
  public Version getVersion() {
    return this.version;
  }

  /**
   * @return ModuleVersion literal.
   */
  @Override
  public String toString() {
    if (this.version == null) {
      return this.nodePath.toString();
    } else {
      return this.nodePath.toString() +  ':' + this.version;
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result;

    result = 1;
    result = (prime * result) + this.nodePath.hashCode();
    result = (prime * result) + (this.version == null ? 0 : this.version.hashCode());

    return result;
  }

  @Override
  public boolean equals(Object other) {
    ModuleVersion moduleVersionOther;

    if (this == other) {
      return true;
    }

    if (!(other instanceof ModuleVersion)) {
      return false;
    }

    moduleVersionOther = (ModuleVersion)other;

    if (!this.nodePath.equals(moduleVersionOther.nodePath)) {
      return false;
    }

    if (this.version == null) {
      if (moduleVersionOther.version != null) {
        return false;
      }
    } else if (!this.version.equals(moduleVersionOther.version)) {
      return false;
    }

    return true;
  }
}
