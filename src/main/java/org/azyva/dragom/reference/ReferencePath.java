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

import java.util.ArrayList;
import java.util.List;

import org.azyva.dragom.model.Module;
import org.azyva.dragom.model.ModuleVersion;

/**
 * Represents a reference path.
 * <p>
 * This class behaves in a way that is very similar to a List as a ReferencePath
 * is essentially an ordered sequence of Reference's. For that reason methods in
 * common with List are named the same.
 * <p>
 * Note that a ReferencePath is NOT a List. If it extended a List class it would
 * carry over methods that are not appropriate for a ReferencePath.
 *
 * @author David Raymond
 */
public class ReferencePath {
	/*
	 * List of {@link Reference}'s.
	 */
	private List<Reference> listReference;

	/**
	 * Default constructor;
	 */
	public ReferencePath() {
		this.listReference = new ArrayList<Reference>();
	}

	/**
	 * Copy constructor.
	 *
	 * @param referencePath ReferencePath.
	 */
	public ReferencePath(ReferencePath referencePath) {
		this.listReference = new ArrayList<Reference>(referencePath.listReference);
	}

	/**
	 * Adds a {@link Reference} to the end of the ReferencePath.
	 * <p>
	 * The new Reference must not create a cycle among the {@link Module}'s.
	 *
	 * @param reference Reference.
	 */
	public void add(Reference reference) {
		for (Reference reference2: this.listReference) {
			if (reference2.getModuleVersion().getNodePath().equals(reference.getModuleVersion().getNodePath())) {
				throw new RuntimeException("Cycle detected in ReferencePath " + this + " when adding Reference " + reference + '.');
			}
		}

		this.listReference.add(reference);
	}

	/**
	 * Adds a ReferencePath to the end of this ReferencePath.
	 * <p>
	 * The new Reference must not create a cycle among the {@link Module}'s.
	 *
	 * @param referencePath ReferencePath.
	 */
	public void add(ReferencePath referencePath) {
		// Adding the Reference's one at a time using the {@link #add} method ensure that
		// we properly check for cycles.
		for (int i = 0; i < referencePath.size() ; i++ ) {
			this.add(referencePath.get(i));
		}
	}

	/**
	 * @return Number of {@link Reference}'s in the ReferencePath.
	 */
	public int size() {
		return this.listReference.size();
	}

	/**
	 * Returns a {@link Reference} in the ReferencePath given an index.
	 *
	 * @param index Index.
	 * @return Reference.
	 */
	public Reference get(int index) {
		return this.listReference.get(index);
	}

	/**
	 * Removes the root {@link Reference} (from the head of the List).
	 */
	public void removeRootReferences() {
		this.listReference.remove(0);

	}

	/**
	 * Removes a given number of root {@link Reference}'s (from the head of the List).
	 * @param nbReferences
	 */
	public void removeRootReferences(int nbReferences) {
		this.listReference.subList(0, nbReferences).clear();
	}

	/**
	 * @return Leaf {@link ModuleVersion} (from the tail of the List).
	 */
	public ModuleVersion getLeafModuleVersion() {
		return this.listReference.get(this.listReference.size() - 1).getModuleVersion();
	}

	/**
	 * Removes the leaf {@Link Reference) (from the tail of the List).
	 */
	public void removeLeafReference() {
		this.listReference.remove(this.listReference.size() - 1);
	}

	/**
	 * Removes a given number of leaf {@Link Reference) (from the tail of the List).
	 */
	public void removeLeafReferences(int nbReferences) {
		this.listReference.subList(this.listReference.size() - nbReferences, nbReferences).clear();
	}
}
