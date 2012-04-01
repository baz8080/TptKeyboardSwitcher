package com.mbcdev.tptkeyboardswitcher.command;

public class FileTestCommand implements Command {

  public enum FileTestType {
    SYMLINK("-L"),
    REGULAR("-e");
    
    private String testOption;
    
    private FileTestType(String testOption) {
     this.testOption = testOption;
    }
    
    public String getTestOption() {
      return this.testOption;
    }
  }
  
  String TEST_COMMAND_FORMAT = "test %s %s" + "\n";
  
  private FileTestType type;
  private String path;

  public FileTestCommand(FileTestType type, String path) {
    
    if (type == null || path == null) {
      throw new IllegalStateException("type and path must be specified");
    }
    
    this.type = type;
    this.path = path;
  }
  
  @Override
  public String getCommand() {
    return String.format(TEST_COMMAND_FORMAT, type.getTestOption(), path);
  }

}
