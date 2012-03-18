package com.mbcdev.tptkeyboardswitcher.command;

public abstract class AbstractCommand {

  public static final String EXIT_COMMAND = "exit\n";
  
  protected Runtime runtime;
  
  public AbstractCommand() {
    runtime = Runtime.getRuntime();
  }
  
  protected abstract int execute(String... args) throws Exception;
  
  public abstract int getNumberOfExpectedArgs(); 
}
