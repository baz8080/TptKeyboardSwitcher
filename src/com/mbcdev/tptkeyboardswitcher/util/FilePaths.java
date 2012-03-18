package com.mbcdev.tptkeyboardswitcher.util;

public enum FilePaths {
  SYSTEM("/system");
  
  private String path;
  
  FilePaths(String path) {
    this.path = path;
  }
  
  public String getPath() {
    return this.path;
  }
}
