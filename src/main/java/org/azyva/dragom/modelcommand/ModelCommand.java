package org.azyva.dragom.modelcommand;

import org.azyva.dragom.model.Node;
import org.azyva.dragom.model.NodePath;

public abstract class ModelCommand {
  /**
   * NodePath of the {@link Node} on which the command is to be applied.
   */
  private NodePath nodePath;

  /**
   * Constructor.
   *
   * @param nodePath NodePath.
   */
  public ModelCommand(NodePath nodePath) {
    this.nodePath = nodePath;
  }

  /**
   * @return NodePath.
   */
  public NodePath getNodePath() {
    return this.nodePath;
  }
}
