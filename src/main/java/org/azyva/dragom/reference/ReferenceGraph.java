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

package org.azyva.dragom.reference;

import java.util.EnumSet;
import java.util.List;

import org.azyva.dragom.model.Module;
import org.azyva.dragom.model.ModuleVersion;
import org.azyva.dragom.model.NodePath;

/**
 * Represents a reference graph of {@link ModuleVersion}'s and operations that can
 * can be performed on it.
 * <p>
 * The concept of matched ModuleVersion is supported. A ReferenceGraph is generally
 * built by traversing the ModuleVersion's and their references using a
 * {@link ReferencePathMatcher} in which case it is possible to mark those
 * ModuleVersion's that are specifically matched by the ReferencePathMatcher. Note
 * that a ReferencePathMatcher matches a complete {@link ReferencePath}. But in a
 * ReferenceGraph the ModuleVersion's themselves are marked as matched. This means
 * that in a ReferenceGraph it is not possible to know which ReferencePath leading
 * to a given ModuleVersion was actually matched. For now it is assumed that once
 * the ReferenceGraph is built, that information is not really useful anymore.
 * <p>
 * Currently this is not very widely used within Dragom. It is used to gather and
 * expose ReferenceGraph data in order to make it easier to implement reporting
 * tools.
 * <p>
 * Currently, {@link ReferencePath}'s are used extensively, but the reference
 * graphs to which they relate are implicit during the traversal of the
 * {@link Reference}'s between {@link Module}'s.
 * <p>
 * It is possible that eventually the concept of ReferenceGraph becomes more
 * important. Specifically it is conceivable that ReferenceGraph data be cached
 * in some persistent storage in order to improve performance of some operations
 * that otherwise require the repeated loading and interpretation of Module build
 * scripts to extract the Reference's.
 *
 * @author David Raymond
 */
//TODO: Not sure if this interface should export only read operations, and if RG building should be left to implementation.
public interface ReferenceGraph {
  /**
   * Represents a {@link ModuleVersion} that references another ModuleVersion. It is
   * a tuple of a ModuleVersion and a {@link Reference} to a another ModuleVersion
   * that occurs within it.
   * <p>
   * This class is required when obtaining the List of referrers to a ModuleVersion
   * since that the referrer ModuleVersion is obviously required, but the actual
   * Reference is also useful.
   * <p>
   * It is not required when obtaining the List of referred-to ModuleVersion's since
   * in that case Reference's can be returned as then contain the referred-to
   * ModuleVersion.
   */
  public static class Referrer {
    /**
     * ModuleVersion.
     */
    private ModuleVersion moduleVersion;

    /**
     * Reference.
     */
    private Reference reference;

    /**
     * Constructor.
     *
     * @param moduleVersion ModuleVersion.
     * @param reference Reference. Can be a subclass which contains additional data
     *   specific to the context in which the Reference occurs, such as a Maven POM.
     */
    public Referrer(ModuleVersion moduleVersion, Reference reference) {
      this.moduleVersion = moduleVersion;
      this.reference = reference;
    }

    /**
     * @return ModuleVersion.
     */
    public ModuleVersion getModuleVersion() {
      return this.moduleVersion;
    }

    /**
     * @return Reference.
     */
    public Reference getReference() {
      return this.reference;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result;

      result = 1;
      result = prime * result + this.moduleVersion.hashCode();
      result = prime * result + this.reference.hashCode();
      return result;
    }

    @Override
    public boolean equals(Object other) {
      ReferenceGraph.Referrer referrerOther;

      if (this == other) {
        return true;
      }

      if (other == null) {
        return false;
      }

      if (!(other instanceof Referrer)) {
        return false;
      }

      referrerOther = (ReferenceGraph.Referrer)other;

      if (!this.moduleVersion.equals(referrerOther.moduleVersion)) {
        return false;
      }

      if (!this.reference.equals(referrerOther.reference)) {
        return false;
      }

      return true;
    }
  }

  /**
   * Traversal order.
   */
  public enum TraversalOrder {
    /**
     * Parent {@link ModuleVersion}'s are visited before their children.
     */
    PARENT_FIRST,

    /**
     * Similar to {@link #PARENT_FIRST}, except that all parent
     * {@link ModuleVersion}'s of a child are visited before the child.
     *
     * <p>This is similar to a breadth-first traversal. This is useful when it must be
     * ensured that when a ModuleVersion is visited, all its parents have already been
     * visited. Recall we are in a graph and not a tree, so a ModuleVersion can have
     * multiple parents.
     */
    ALL_PARENTS_FIRST,

    /**
     * Children {@link ModuleVersion}'s are visited before their parents.
     */
    DEPTH_FIRST
  }

  /**
   * Reentry mode to determine what to do when a {@link ModuleVersion} has already
   * been visisted during the traversal of a {@link ReferenceGraph}.
   */
  public enum ReentryMode {
    /**
     * {@link ModuleVersion}'s in the ReferenceGraph are all visited.
     */
    REENTRY,

    /**
     * Reentry is avoided, but when a {@link ModuleVersion} that is encountered has
     * already been visited, it is visited again with {@link ReferenceGraph.VisitAction#REPEATED},
     * but its children are not.
     */
    ONLY_PARENT,

    /**
     * Reentry is avoided altogether. A given {@link ModuleVersion} will not be
     * visited twice.
     */
    NO_REENTRY
  }

  /**
   * Action during the traversal of a {@link ReferenceGraph}.
   * <p>
   * These are used as flags and provided as part of an EnumSet since many of them
   * can be pertinent for a single visit.
   */
  public enum VisitAction {
    /**
     * Indicates we are stepping into a {@link ModuleVersion} during the traversal. A
     * matching {@link #STEP_OUT} will occur when the traversal of the ModuleVersion
     * is complete.
     * <p>
     * This VisitAction is mutually exclusive with all others.
     * <p>
     * Not used by {@link ReferenceGraph#visitLeafModuleVersionReferencePaths}.
     */
    STEP_IN,

    /**
     * Indicates we are stepping out of a {@link ModuleVersion} during the traversal.
     * A matching {@link #STEP_IN} will have occurred previously.
     * <p>
     * This VisitAction is mutually exclusive with all others.
     * <p>
     * Not used by {@link ReferenceGraph#visitLeafModuleVersionReferencePaths}.
     */
    STEP_OUT,

    /**
     * Represents the actual visit of the leaf {@link ModuleVersion}. A
     * {@link #STEP_IN} will have occurred previously for the parent ModuleVersion.
     * But if traversal is depth-first STEP_IN for all the ModuleVersion in the
     * {@link ReferencePath} will have also occurred with no intervening visit for the
     * intermediate ModuleVersion's.
     * <p>
     * {@link #STEP_OUT} will occur thereafter for the parent ModuleVersion, after
     * having visited all of its child ModuleVersion.
     * <p>
     * This is the only VisitAction used by visitLeafModuleVersionReferencePaths.
     */
    VISIT,

    /**
     * Used in conjunction with {@link #VISIT} when the {@link ModuleVersion} has
     * already been visited.
     * <p>
     * If {@link ReentryMode#NO_REENTRY} is specified when calling
     * {@link ReferenceGraph#traverseReferenceGraph}, this will not occur. If
     * {@link ReentryMode#ONLY_PARENT} is specified, this will not occur for
     * children of an already visited parent. It will occur only for the top-most
     * revisited parent. If {@link ReentryMode#REENTRY} is specified, this will occur
     * for every ModuleVersion that is revisited.
     * <p>
     * Not used by visitLeafModuleVersionReferencePaths.
     */
    REPEATED,

    /**
     * Can be included with {@link #VISIT} to indicate that the
     * {@link ModuleVersion} is matched.
     */
    MATCHED;

    /**
     * EnumSet of VisitAction's representing the single {@link VisitAction#STEP_IN}.
     */
    public static final EnumSet<VisitAction> ENUM_SET_STEP_IN = EnumSet.of(STEP_IN);

    /**
     * EnumSet of VisitAction's representing the single {@link VisitAction#STEP_OUT}.
     */
    public static final EnumSet<VisitAction> ENUM_SET_STEP_OUT = EnumSet.of(STEP_OUT);

    /**
     * EnumSet of VisitAction's representing the single {@link VisitAction#VISIT}.
     */
    public static final EnumSet<VisitAction> ENUM_SET_VISIT = EnumSet.of(VISIT);

    /**
     * EnumSet of VisitAction's representing {@link VisitAction#VISIT} and
     * {@link VisitAction#REPEATED}.
     */
    public static final EnumSet<VisitAction> ENUM_SET_REPEATED_VISIT = EnumSet.of(VISIT, REPEATED);

    /**
     * EnumSet of VisitAction's representing {@link VisitAction#VISIT} and
     * {@link VisitAction#MATCHED}.
     */
    public static final EnumSet<VisitAction> ENUM_SET_VISIT_MATCHED = EnumSet.of(VISIT, MATCHED);

    /**
     * EnumSet of VisitAction's representing {@link VisitAction#VISIT},
     * {@link VisitAction#REPEATED} and {@link VisitAction#MATCHED}.
     */
    public static final EnumSet<VisitAction> ENUM_SET_REPEATED_VISIT_MATCHED = EnumSet.of(VISIT, REPEATED, MATCHED);
  }

  /**
   * Controls the subsequent visit actions during a traversal of the
   * ReferenceGraph. This is the type returned by {@link Visitor#visit}.
   */
  public enum VisitControl {
    /**
     * Continue.
     */
    CONTINUE,

    /**
     * Aborts the traversal.
     */
    ABORT,

    /**
     * Skip the children and continue.
     * <p>
     * Not valid when {@link ReferenceGraph.VisitAction#STEP_OUT}.
     * <p>
     * Not valid when {@link ReferenceGraph#traverseReferenceGraph} is called with
     * traversalOrder set to {@link TraversalOrder#DEPTH_FIRST} and
     * {@link VisitAction#VISIT}.
     * <p>
     * Not valid when {@link ReferenceGraph#visitLeafModuleVersionReferencePaths}.
     */
    SKIP_CHILDREN,

    /**
     * Skip the current root {@link ModuleVersion} and continue with the next one, if
     * any.
     * <p>
     * Not valid when {@link ReferenceGraph#visitLeafModuleVersionReferencePaths}.
     */
    SKIP_CURRENT_ROOT
  }

  /**
   * Visitor interface.
   */
  public static interface Visitor {
    /**
     * Called when visiting a {@link ModuleVersion} in the ReferenceGraph.
     *
     * @param referenceGraph ReferenceGraph in the context of which the ModuleVersion
     *   is being visited.
     * @param referencePath ReferencePath of the ModuleVersion being visited.
     * @param enumSetVisitAction EnumSet of VisitAction's.
     * @return VisitControl.
     */
    VisitControl visit(ReferenceGraph referenceGraph, ReferencePath referencePath, EnumSet<ReferenceGraph.VisitAction> enumSetVisitAction);
  }

  /**
   * Indicates if a {@link ModuleVersion} is part of the ReferenceGraph.
   *
   * @param moduleVersion ModuleVersion.
   * @return Indicates if the ModuleVersion is part of the ReferenceGraph.
   */
  boolean moduleVersionExists(ModuleVersion moduleVersion);

  /**
   * @return List of {@link ModuleVersion} that are the roots of the ReferenceGraph.
   */
  List<ModuleVersion> getListModuleVersionRoot();

  /**
   * Indicates if a {@link ModuleVersion} is a root of the ReferenceGraph.
   *
   * @param moduleVersion ModuleVersion.
   * @return See description.
   */
  boolean isRootModuleVersion(ModuleVersion moduleVersion);

  /**
   * @return List of {@link ModuleVersion} that are matched.
   */
  List<ModuleVersion> getListModuleVersionMatched();

  /**
   * Indicates if a {@link ModuleVersion} is matched.
   *
   * @param moduleVersion ModuleVersion.
   * @return See description.
   */
  boolean isMatchedModuleVersion(ModuleVersion moduleVersion);

  /**
   * Returns the List of {@link ModuleVersion}'s corresponding to a specified
   * {@link Module} identified by its {@link NodePath}.
   *
   * @param nodePath NodePath. Can be null in which case all ModuleVersions's in the
   * ReferenceGraph are returned.
   * @return List of ModuleVersion's.
   */
  List<ModuleVersion> getListModuleVersion(NodePath nodePath);

  /**
   * Returns the List of {@link Referrer}'s to a specified {@link ModuleVersion}.
   *
   * @param moduleVersionReferred Referred ModuleVersion.
   * @return List of Referrer's.
   */
  List<Referrer> getListReferrer(ModuleVersion moduleVersionReferred);

  /**
   * Returns the List of {@link Reference}'s of a specified {@link ModuleVersion}.
   *
   * @param moduleVersionReferrer Referrer ModuleVersion.
   * @return List of Reference's.
   */
  List<Reference> getListReference(ModuleVersion moduleVersionReferrer);

  /**
   * Traverses the ReferenceGraph starting at a given {@link ModuleVersion} or for
   * each root ModuleVersion.
   *
   * @param moduleVersion ModuleVersion at which to start the traversal. Can be null
   *   to indicate to perform the traversal for each root ModuleVersion.
   * @param traversalOrder TraversalOrder.
   * @param reentryMode ReentryMode.
   * @param visitor Visitor.
   * @return Indicates if the traversal has been aborted (if {@link Visitor#visit}
   *   returned {@link VisitControl#ABORT}).
   */
  boolean traverseReferenceGraph(ModuleVersion moduleVersion, TraversalOrder traversalOrder, ReentryMode reentryMode, Visitor visitor);

  /**
   * Visits all {@link ReferencePath}'s ending with a leaf ModuleVersion.
   *
   * @param moduleVersion Leaf ModuleVersion.
   * @param visitor Visitor.
   * @return Indicates if the traversal has been aborted (if {@link Visitor#visit}
   *   returned {@link VisitControl#ABORT}).
   */
  boolean visitLeafModuleVersionReferencePaths(ModuleVersion moduleVersion, Visitor visitor);

  /**
   * Adds a root {@link ModuleVersion}.
   * <p>
   * If the ModuleVersion already exists, it is made a root (if not already).
   *
   * @param moduleVersionRoot Root ModuleVersion.
   */
  void addRootModuleVersion(ModuleVersion moduleVersionRoot);

  /**
   * Adds a {@link Reference} to the ReferenceGraph.
   * <p>
   * The referrer ModuleVersion must already exist in the ReferenceGraph. It may be
   * a root.
   * <p>
   * The referred-to ModuleVersion within the Reference is created if it does not
   * exist.
   * <p>
   * The Reference may already exist in which case it is not added.
   * <p>
   * The newly added Reference must not create a cycle in the ReferenceGraph.
   *
   * @param moduleVersionReferrer Referrer ModuleVersion.
   * @param reference Reference.
   */
  void addReference(ModuleVersion moduleVersionReferrer, Reference reference);

  /**
   * Adds a complete {@link ReferencePath} to the ReferenceGraph and makes the leaf
   * {@link ModuleVersion} matched.
   * <p>
   * {@link Reference}'s that already exist are not added. But the leaf
   * ModuleVersion is made matched (if not already).
   * <p>
   * The newly added Reference's must not create cycles in the ReferenceGraph.
   *
   * @param referencePath ReferencePath.
   */
  void addMatchedReferencePath(ReferencePath referencePath);
}
