package com.mbcdev.tptkeyboardswitcher.command;

public class CopyCommand implements Command {

  private static final String COMMAND_FORMAT = "cp -f %s %s\n";
  private String sourcePath;
  private String destinationPath;
  
  public CopyCommand(String sourcePath, String destinationPath) {
    if (sourcePath == null || destinationPath == null) {
      throw new IllegalArgumentException("one or more params null");
    }
    
    this.sourcePath = sourcePath;
    this.destinationPath = destinationPath;
  }
  
  @Override
  public String getCommand() {
    return String.format(COMMAND_FORMAT, sourcePath, destinationPath);
  }

}
