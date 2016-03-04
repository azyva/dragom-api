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

package org.azyva.dragom.model.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.azyva.dragom.execcontext.ExecContext;
import org.azyva.dragom.model.Version;
import org.azyva.dragom.reference.Reference;
import org.azyva.dragom.reference.ReferencePath;

/**
 * Represents an arbitrary task that can be performed on a Module.
 *
 * @author David Raymond
 */

public interface TaskPlugin extends ModulePlugin {
	/**
	 * Specifies the effects a task had on a Module.
	 *
	 * This class implements a simple builder pattern allowing the following idiom for
	 * concisely returning instances:
	 *
	 * return (new TaskEffect()).changed().referenceChanged();
	 */
	public class TaskEffects {
		/**
		 * Indicates to abort the whole process.
		 */
		private boolean indAbort;

		/**
		 * Indicates the module was changed.
		 */
		private boolean indChanged;

		/**
		 * Indicates at least a reference was changed.
		 */
		private boolean indReferenceChanged;

		/**
		 * Indicates to skip the traversal of all the children (if the traversal was
		 * parent first).
		 */
		private boolean indSkipChildren;

		/**
		 * List of Reference to skip the traversal (if the traversal was parent first).
		 */
		private List<Reference> listReferenceSkip;

		/**
		 * List of actions performed.
		 */
		private List<String> listActionPerformed;


		/**
		 * Indicates to abort the whole process.
		 * <p>
		 * It is OK for the task to also set the IND_ABORT runtime property within the
		 * {@link ExecContext}.
		 *
		 * @return
		 */
		public TaskEffects abort() {
			this.indAbort = true;
			return this;
		}

		public boolean isAbort() {
			return this.indAbort;
		}

		public TaskEffects changed() {
			this.indChanged = true;
			return this;
		}

		public boolean isChanged() {
			return this.indChanged;
		}

		public TaskEffects referenceChanged() {
			this.indReferenceChanged = true;
			this.indChanged = true;
			return this;
		}

		public boolean isReferenceChanged() {
			return this.indReferenceChanged;
		}

		public TaskEffects skipChildren() {
			this.indSkipChildren = true;
			return this;
		}

		public boolean isSkipChildren() {
			return this.indSkipChildren;
		}

		public TaskEffects skipReference(Reference reference) {
			if (this.listReferenceSkip == null) {
				this.listReferenceSkip = new ArrayList<Reference>();
			}

			this.listReferenceSkip.add(reference);

			return this;
		}

		public List<Reference> getListReferenceSkip() {
			if (this.listReferenceSkip == null) {
				return Collections.emptyList();
			} else {
				return this.listReferenceSkip;
			}
		}

		public TaskEffects actionPerformed(String actionPerformed) {
			if (this.listActionPerformed == null) {
				this.listActionPerformed = new ArrayList<String>();
			}

			this.listActionPerformed.add(actionPerformed);

			return this;
		}

		public List<String> getListActionPerformed() {
			if (this.listActionPerformed == null) {
				return Collections.emptyList();
			} else {
				return this.listActionPerformed;
			}
		}
	}

	/**
	 * @return Indicates if the traversal is depth first, as opposed to parent first.
	 */
	boolean isDepthFirst();

	/**
	 * @return Indicates that reentry in same ModuleVersion must be avoided.
	 */
	boolean isAvoidReentry();

	/**
	 * Performs the task.
	 *
	 * @param taskId Task ID. Can be null to indicate to perform the default task, if
	 *   any.
	 * @param version Version of the Module on which to perform the task.
	 * @param referencePath ReferencePath of the Module. The last Reference is
	 * necessarily Version version of the current Module.
	 * @return TaskEffects.
	 */
	TaskEffects performTask(String taskId, Version version, ReferencePath referencePath);
}