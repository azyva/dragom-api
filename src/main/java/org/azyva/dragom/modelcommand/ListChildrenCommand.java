package org.azyva.dragom.modelcommand;

import org.azyva.dragom.model.NodePath;

public class ListChildrenCommand extends ModelCommand {
  /**
   * Constructor.
   *
   * @param nodePath NodePath.
   */
  public ListChildrenCommand(NodePath nodePath) {
    super(nodePath);
  }
}
