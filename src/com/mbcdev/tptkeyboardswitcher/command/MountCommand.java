package com.mbcdev.tptkeyboardswitcher.command;


public class MountCommand implements Command {

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

  private String path;
  private MountOptions mountOptions;
  
  public MountCommand (final String path, final MountOptions mountOptions) {
    this.path = path;
    this.mountOptions = mountOptions;
  }

  @Override
  public String getCommand() {
    return String.format(MOUNT_FS_COMMAND_FORMAT,mountOptions.getOptions(), path);
  }
}
