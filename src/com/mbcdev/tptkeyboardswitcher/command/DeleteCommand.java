package com.mbcdev.tptkeyboardswitcher.command;

public class DeleteCommand implements Command {

  private String filePath;
  private final static String COMMAND_FORMAT = "rm %s\n";

  public DeleteCommand(String filePath) {
    this.filePath = filePath;
  }
  
  @Override
  public String getCommand() {
    return String.format(COMMAND_FORMAT, filePath);
  }

}
