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

import org.azyva.dragom.model.ModuleVersion;

/**
 * Represents a reference path.
 *
 * For now this class simply extends ArrayList as a ReferencePath is essentially
 * an ordered sequence of Reference's. Eventually a more specific implementation
 * may be developed. But since the concept of ReferencePath is important in Dragom
 * and deserves being represented with a class.
 *
 * @author David Raymond
 */
public class ReferencePath extends ArrayList<Reference> {
	private static final long serialVersionUID = 0; // To keep the compiler from complaining.

	/**
	 * Default constructor;
	 */
	public ReferencePath() {
		super();
	}

	/**
	 * Copy constructor.
	 *
	 * @param referencePath ReferencePath.
	 */
	public ReferencePath(ReferencePath referencePath) {
		super(referencePath);
	}

	/**
	 * @return Leaf {@link ModuleVersion}.
	 */
	public ModuleVersion getLeafModuleVersion() {
		return this.get(this.size() - 1).getModuleVersion();
	}

	/**
	 * Removes the leaf reference.
	 */
	public void removeLeafReference() {
		this.remove(this.size() - 1);
	}
}
