/*
 * Copyright 2015 - 2017 AZYVA INC.
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

import java.util.EnumSet;

import org.azyva.dragom.apiutil.ByReference;
import org.azyva.dragom.model.ModuleVersion;
import org.azyva.dragom.reference.ReferencePath;
import org.azyva.dragom.reference.ReferencePathMatcher;

/**
 * Filters the matched {@link ModuleVersion}. Used by many jobs when traversing a
 * reference graph.
 *
 * <p>Allows to implement ModuleVersion matching strategies which cannot be
 * expressed with {@link ReferencePathMatcher}. Note that this matching is
 * performed in addition to the matching performed by other means, such as with
 * the ReferencePathMatcher, and all matchers must match for a ModuleVersion to be
 * deemed matched.
 *
 * <p>Jobs which are aware of ModuleVersionFilter (essentially all jobs based on
 * RootModuleVersionJobAbstractImpl from dragom-core) generally obtain this plugin
 * with a null plugin ID. It is up to the configuration to provide or not an
 * implementation of the plugin, or to use RuntimeSelectionPluginFactory from
 * dragom-core to allow selecting the filtering strategy at runtime.
 *
 * @author David Raymond
 */
public interface ModuleVersionMatcherPlugin extends ModulePlugin {
  /**
   * Defines the match flags.
   */
  public enum MatchFlag {
    /**
     * The {@link ModuleVersion} matches.
     *
     * <p>The absence of this flag implies the ModuleVersion does not match.
     */
    MATCH,

    /**
     * Assume children do not match and must be skipped.
     *
     * <p>This flag applies only if the tranversal of the reference graph is parent
     * first.
     */
    SKIP_CHILDREN;

    /**
     * EnumSet MatchFlag indicating the {@link ModuleVersion} matches and the children
     * may also match.
     */
    public static final EnumSet<MatchFlag> MATCH_CONTINUE = EnumSet.of(MatchFlag.MATCH);

    /**
     * EnumSet MatchFlag indicating the {@link ModuleVersion} matches but the children
     * do not match and must be skipped.
     */
    public static final EnumSet<MatchFlag> MATCH_SKIP_CHILDREN = EnumSet.of(MatchFlag.MATCH, MatchFlag.SKIP_CHILDREN);

    /**
     * EnumSet MatchFlag indicating the {@link ModuleVersion} does not match but the
     * children may match.
     */
    public static final EnumSet<MatchFlag> NO_MATCH_CONTINUE = EnumSet.noneOf(MatchFlag.class);

    /**
     * EnumSet MatchFlag indicating the {@link ModuleVersion} does not match and the
     * children do not match either and must be skipped.
     */
    public static final EnumSet<MatchFlag> NO_MATCH_SKIP_CHILDREN = EnumSet.of(MatchFlag.SKIP_CHILDREN);
  }

  /**
   * Called to verify if a ModuleVersion matches.
   *
   * <p>Generally, jobs which use this plugin are expected to accumulate the
   * messages returned through byReferenceMessage and provide them to the user at
   * the end of the job execution.
   *
   * @param referencePath ReferencePath of the ModuleVersion.
   * @param moduleVersion ModuleVersion (corresponds necessarily to the leaf of the
   *   ReferencePath).
   * @param byReferenceMessage Allows the method to return a message justifying the
   *   decision to include or exclude a ModuleVersion and/or its children. Generally
   *   it is expected that such a message be provided for negative responses only.
   * @return EnumSet of MatchFlag.
   */
  EnumSet<MatchFlag> matches(ReferencePath referencePath, ModuleVersion moduleVersion, ByReference<String> byReferenceMessage);
}
