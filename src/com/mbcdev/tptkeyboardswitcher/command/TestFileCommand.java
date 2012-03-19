package com.mbcdev.tptkeyboardswitcher.command;

import java.io.DataOutputStream;

public class TestFileCommand extends AbstractCommand {

  private static final String TEST_COMMAND_FORMAT = "test %s %s" + "\n";
  
  public enum FileTest {
    IS_SYMBOLIC_LINK("-L"),
    IS_REGULAR_FILE("-e");
    
    private String testOption;
    
    private FileTest(String testOption) {
     this.testOption = testOption;
    }
    
    public String getTestOption() {
      return this.testOption;
    }
  }
  
  public boolean isSymbolic(String filePath) throws Exception {
    return execute(FileTest.IS_SYMBOLIC_LINK.testOption, filePath) == 0 ? true : false;
  }
  
  public boolean isRegular(String filePath) throws Exception {
    return execute(FileTest.IS_REGULAR_FILE.testOption, filePath) == 0 ? true : false;
  }
  
  @Override
  protected int execute(String... args) throws Exception {
    if (args.length != getNumberOfExpectedArgs()) {
      throw new IllegalArgumentException("Wrong number of args, expected: " + getNumberOfExpectedArgs());
    }
    
    String test = args[0];
    String path = args[1];
    
    if (test == null || path == null) {
      throw new IllegalArgumentException("All args required");
    }
    
    final String command = 
        String.format(TEST_COMMAND_FORMAT, test, path);
    
    Process p = getNormalProcess();
    
    DataOutputStream os = new DataOutputStream(p.getOutputStream());
    
    os.writeBytes(command);
    os.writeBytes(EXIT_COMMAND);
    os.flush();
    
    p.waitFor();
    
    int rc = p.exitValue();
    
    return rc;
  }

  @Override
  public int getNumberOfExpectedArgs() {
    return 2;
  }

  

}
