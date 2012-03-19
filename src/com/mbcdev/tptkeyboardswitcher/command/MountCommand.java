package com.mbcdev.tptkeyboardswitcher.command;

import java.io.DataOutputStream;

import com.mbcdev.tptkeyboardswitcher.util.FileUtils.FilePaths;

public class MountCommand extends AbstractCommand {

  private static final String MOUNT_FS_COMMAND_FORMAT = "mount -o %s %s\n";
  
  public enum MountOptions {
    READ_ONLY("ro,remount"), READ_WRITE("rw,remount");

    private String options;

    private MountOptions(String options) {
      this.options = options;
    }

    public String getOptions() {
      return this.options;
    }
  }
  
  public boolean mount(final String path, final String mountOptions) throws Exception {
    int rc = execute(path, mountOptions);
    return rc == 0;
  }

  public boolean rwSystem() throws Exception {
    return mount(FilePaths.SYSTEM.getPath(), MountOptions.READ_WRITE.getOptions());
  }

  public boolean roSystem() throws Exception {
    return mount(FilePaths.SYSTEM.getPath(), MountOptions.READ_ONLY.getOptions());
  }
  
  @Override
  protected int execute(String... args) throws Exception {
    
    if (args.length != getNumberOfExpectedArgs()) {
      throw new IllegalArgumentException("wrong number of args");
    }
    
    String path = args[0];
    String mountOptions = args[1];
    
    if (mountOptions == null || path == null) {
      throw new IllegalArgumentException("one or more args null");
    }
    
    final String command = 
        String.format(MOUNT_FS_COMMAND_FORMAT,mountOptions, path);

    Process p = null;
    
    p = Runtime.getRuntime().exec("su");

    DataOutputStream os = new DataOutputStream(p.getOutputStream());

    os.writeBytes(command);
    os.writeBytes(EXIT_COMMAND);
    os.flush();

    p.waitFor();

    return p.exitValue();
  }

  @Override
  public int getNumberOfExpectedArgs() {
    return 2;
  }
}
