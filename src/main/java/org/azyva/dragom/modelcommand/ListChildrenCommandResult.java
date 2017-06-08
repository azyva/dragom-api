package org.azyva.dragom.modelcommand;

import java.util.List;

public class ListChildrenCommandResult extends CommandResult {
  private List<String> listChildren;

  public void setListChildren(List<String> listChildren) {
    this.listChildren = listChildren;
  }

  public List<String> getListChildren() {
    return this.listChildren;
  }
}
