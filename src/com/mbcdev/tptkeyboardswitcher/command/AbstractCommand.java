package com.mbcdev.tptkeyboardswitcher.command;

import java.io.IOException;

public abstract class AbstractCommand {

  public static final String EXIT_COMMAND = "exit\n";
  
  protected Runtime runtime;
  
  public AbstractCommand() {
    runtime = Runtime.getRuntime();
  }
  
  protected abstract int execute(String... args) throws Exception;
  
  public abstract int getNumberOfExpectedArgs();
  
  public Process getNormalProcess() throws IOException {
    return runtime.exec("sh");
  }
}
