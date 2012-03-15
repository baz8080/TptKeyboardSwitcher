package com.mbcdev.tptkeyboardswitcher.util;

import java.io.DataOutputStream;
import java.io.IOException;

import roboguice.util.Ln;

public class FsUtil {

  private static final String SYSTEM_PARTITION_PATH = "/system";
  private static final String MOUNT_FS_COMMAND_FORMAT = "mount -o %s %s\n";
  private static final String EXIT_COMMAND = "exit\n";

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

  public static boolean mount(final String path, final MountOptions mountOptions) {

    final String command = String.format(MOUNT_FS_COMMAND_FORMAT,mountOptions.getOptions(), path);

    Process p = null;
      
    try {
      p = Runtime.getRuntime().exec("su");

      DataOutputStream os = new DataOutputStream(p.getOutputStream());

      os.writeBytes(command);
      os.writeBytes(EXIT_COMMAND);
      os.flush();

      p.waitFor();

      int rc = p.exitValue();
      Ln.d("rc " + rc);

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return false;

  }

  public static boolean rwSystem() {
    return mount(SYSTEM_PARTITION_PATH, MountOptions.READ_WRITE);
  }

  public static boolean roSystem() {
    return mount(SYSTEM_PARTITION_PATH, MountOptions.READ_ONLY);
  }
}
