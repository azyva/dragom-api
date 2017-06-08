package org.azyva.dragom.modelcommand;

public abstract class CommandResult {
  /**
   * Error ID.
   */
  private String errorId;

  /**
   * Error message.
   */
  private String errorMsg;

  /**
   * Sets the error ID.
   *
   * @param errorId See description.
   */
  public void setErrorId(String errorId) {
    this.errorId = errorId;
  }

  /**
   * @return Error ID.
   */
  public String getErrorId() {
    return this.errorId;
  }

  /**
   * Sets the error message.
   *
   * @param errorMsg See description.
   */
  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  /**
   * @return Error message.
   */
  public String getErrorMsg() {
    return this.errorMsg;
  }
}
