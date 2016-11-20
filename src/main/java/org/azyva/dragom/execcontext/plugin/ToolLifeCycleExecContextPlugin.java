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

import org.azyva.dragom.execcontext.ExecContext;

/**
 * Implemented by {@link ExecContextPlugin} implementations that need to be
 * informed of tool life cycle.
 * <p>
 * This allows an ExecContextPlugin to explicitly bind itself to an
 * {@link ExecContext} information scope.
 * <p>
 * The ExecContext implementation having an ExecContextPlugin whose implementation
 * implements this ToolLifeCycleExecContextPlugin is expected to behave in the
 * following manner:
 * <p>
 * <li>If {@link #isTransient} returns true, it must create and release the
 *     ExecContextPlugin for each tool execution. This implies that the
 *     ExecContextPlugin is bound to tool scope;</li>
 * <li>If {@link #isTransient} returns false, it can keep the ExecContextPlugin
 *     instance alive in between tool execution. This implies that the
 *     ExecContextPlugin is bound to workspace scope;</li>
 * <li>It should call {@link #startTool} during tool initialization;</li>
 * <li>It should call {@link #endTool} during tool termination.</li>
 * <p>
 * An ExecContext implementation is not forced to handle
 * ToolLifeCycleExecContextPlugin. In such a case, it must treat the
 * ExecContextPlugin as if isTransient returns true, implying tool binding. If
 * isTransient actually returns false, the fact that the ExecContextPlugin is
 * anyways released after each tool execution should not do any harm apart from
 * reduced performance due to not reusing instances when a single JVM is used for
 * multiple tool executions. That is why it is not guaranteed that startTool and
 * endTool will be called. But if they are not, instances will necessarily not be
 * reused across tool executions.
 * <p>
 * If an ExecContextPlugin implementation does not implement that interface and
 * the ExecContext implementation does handle it, it must treat the
 * ExecContextPlugin as if isTransient returns false, implying workspace binding.
 * <p>
 * The two above paragraphs may seem incoherent (assume isTransient returns true
 * in one case and itTransient returns false in the other). But they are not. The
 * idea is that if an ExecContextPlugin does not implement that interface, it
 * implicitly "agrees" to be bound to workspace scope, the default. But if the
 * ExecContextPlugin does implement this interface and isTransient returns true,
 * it effectively does not support workspace scope, whereas all
 * ExecContextPlugin's implicitly support tool scope, even if isTransient returns
 * false.
 * <p>
 * If isTransient returns false and ExecContextPlugin's instances are reused
 * across tool executions, it also implies that two tools could execute
 * simultaneously on the same ExecContext (on different threads), and thus with
 * the same ExecContextPlugin instance. It is up to the ExecContextPlugin to
 * handle this situation, or prevent it from happening.
 * {@link DefaultWorkspacePluginFactory} prevents this from happening by locking
 * the workspace directory, and thus the ExecContext to which it is associated,
 * using a marker lock file.
 *
 * @author David Raymond
 */

public interface ToolLifeCycleExecContextPlugin {
	/**
	 * @return Indicates that the {@link ExecContextPlugin} must not be reused across
	 *   tool executions, implying tool binding. If false, the ExecContextPlugin may
	 *   be reused across tool executions, implying workspace binding.
	 */
	boolean isTransient();

	/**
	 * Informs the {@link ExecContextPlugin} that a tool is started with the
	 * {@link ExecContext} holding it.
	 * <p>
	 * This gives an opportunity to the ExecContextPlugin to perform per-tool
	 * initialization.
	 */
	void startTool();

	/**
	 * Informs the {@link ExecContextPlugin} that a tool is terminated with the
	 * {@link ExecContext} holding it.
	 * <p>
	 * This gives an opportunity to the ExecContextPlugin to perform per-tool
	 * cleanup.
	 */
	void endTool();
}
