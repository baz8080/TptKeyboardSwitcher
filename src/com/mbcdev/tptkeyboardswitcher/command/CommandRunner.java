package com.mbcdev.tptkeyboardswitcher.command;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import roboguice.util.Ln;

public class CommandRunner {

  private boolean runAsRoot;
  private List<Command> commands;

  private CommandRunner(Builder builder) {
    runAsRoot = builder.runAsRoot;
    commands = builder.commands;
  }
  
  public int execute() throws Exception {
    
    int exitCode = -1;
    
    Process p = getProcess();
    
    DataOutputStream dos = new DataOutputStream(p.getOutputStream());
    
    for (Command command: commands) {
      dos.writeBytes(command.getCommand());
    }
    
    dos.flush();
    
    p.waitFor();
    exitCode = p.exitValue();
    
    dos.close();
    
    return exitCode;
  }
  
  private Process getProcess() throws IOException {
    Runtime runtime = Runtime.getRuntime();
    Process p = runAsRoot ? runtime.exec("su") : runtime.exec("sh");
    return p;
  }
  
  public static class Builder {
    
    private boolean runAsRoot;
    
    private List<Command> commands;

    private boolean exitOnClose;
    
    public Builder() {
      runAsRoot = false;
      commands = new ArrayList<Command>();
      exitOnClose = true;
    }
    
    public CommandRunner build() {
      
      if (exitOnClose) {
        commands.add(new ExitCommand());
      } else {
        Ln.w("Will NOT exit on close!");
      }
      
      return new CommandRunner(this);
    }
    
    public Builder command(Command command) {
      commands.add(command);
      return this;
    }
    
    /**
     * The set of commands should be run as root
     */
    public Builder runAsRoot() {
      this.runAsRoot = true;
      return this;
    }
    
    /**
     * The set of commands should not be run as root
     */
    public Builder runAsNormal() {
      this.runAsRoot = false;
      return this;
    }
    
    public Builder exitOnClose(boolean exitOnClose) {
      this.exitOnClose = exitOnClose;
      return this;
    }
  }
}
