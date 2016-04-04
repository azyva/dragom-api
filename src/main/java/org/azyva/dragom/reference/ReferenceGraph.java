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

package org.azyva.dragom.reference;

import java.util.List;

import org.azyva.dragom.model.Module;
import org.azyva.dragom.model.ModuleVersion;

/**
 * Represents a reference graph of {@link ModuleVersion}'s and operations that can
 * can be performed on it.
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
 * important. In particular it is conceivable that ReferenceGraph data be cached
 * in some persistent storage in order to improve performance of some operations
 * that otherwise require the repeated loading and interpretation of Module build
 * scripts to extract the Reference's.
 *
 * @author David Raymond
 */
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
	}

	/**
	 * Visitor interface.
	 */
	public static interface Visitor {
		/**
		 * Called when visiting a {@link ModuleVersion} in the ReferenceGraph.
		 *
		 * @param referencePath ReferencePath of the ModuleVersion being visited.
		 */
		void visit(ReferencePath referencePath);
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
	 * @return Indicates if a {@link ModuleVersion} is a root of the ReferenceGraph.
	 */
	boolean isRootModuleVersion(ModuleVersion moduleVersion);

	/**
	 * Returns the List of {@link ModuleVersion}'s that are matched by a specified
	 * ModuleVersion that can potentially have a null Version.
	 *
	 * @param moduleVersion ModuleVersion. Can be null in which case all
	 *   ModuleVersions's in the ReferenceGraph are returned.
	 * @return List of matched ModuleVersion's.
	 */
	List<ModuleVersion> getListModuleVersionMatched(ModuleVersion moduleVersion);

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
	 * @param isDepthFirst Indicates that the traversal is depth-first, as opposed to
	 *   parent-first.
	 * @param isAvoidReentry Indicates to avoid revisiting same ModuleVersion's.
	 * @param visitor Visitor.
	 */
	void traverseReferenceGraph(ModuleVersion moduleVersion, boolean isDepthFirst, boolean isAvoidReentry, Visitor visitor);

	/**
	 * Visits all {@link ReferencePath}'s ending with a leaf ModuleVersion.
	 *
	 * @param moduleVersion Leaf ModuleVersion.
	 * @param visitor Visitor.
	 */
	void visitLeafModuleVersionReferencePaths(ModuleVersion moduleVersion, Visitor visitor);

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
	 * The newly added Reference must not create a cycle in the ReferenceGraph.
	 *
	 * @param moduleVersionReferrer Referrer ModuleVersion.
	 * @param reference Reference.
	 */
	void addReference(ModuleVersion moduleVersionReferrer, Reference reference);
}
