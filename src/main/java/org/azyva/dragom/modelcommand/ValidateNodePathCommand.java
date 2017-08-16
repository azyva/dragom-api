package org.azyva.dragom.modelcommand;

import org.azyva.dragom.model.NodePath;

public class ValidateNodePathCommand extends ModelCommand {
  /**
   * Constructor.
   *
   * @param nodePath NodePath.
   */
  public ValidateNodePathCommand(NodePath nodePath) {
    super(nodePath);
  }
}
