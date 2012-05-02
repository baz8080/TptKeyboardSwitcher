package com.mbcdev.tptkeyboardswitcher.command;

public class SymlinkCommand implements Command {

  private static final String SYMLINK_COMMAND_FORMAT = "ln -s %s %s\n";
  
  private String linkPath;
  private String targetPath;
  
  public SymlinkCommand(String link, String target) {
    this.linkPath = link;
    this.targetPath = target;
  }
  
  
  @Override
  public String getCommand() {
    return String.format(SYMLINK_COMMAND_FORMAT, targetPath, linkPath); 
  }

}
