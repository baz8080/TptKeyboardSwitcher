package com.mbcdev.tptkeyboardswitcher.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.res.AssetFileDescriptor;

import roboguice.util.Ln;

public class FileUtils {

  public enum FilePaths {
    SYSTEM("/system"),
    KEYCHARS("/system/usr/keychars"),
    KEYLAYOUTS("/system/usr/keylayouts")
    ;
    
    private String path;
    
    FilePaths(String path) {
      this.path = path;
    }
    
    public String getPath() {
      return this.path;
    }
  }

  public static String computeMD5Hash(String filePath) {
    String hash = null;
    MessageDigest md = null;
    InputStream fis = null;
    
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      Ln.e("Couldn't get MD5 algorithm");
      return null;
    }
    
    try {
      fis = new FileInputStream(filePath);
      byte[] buffer = new byte[1024 * 8];
      int bytesRead = 0;
      
      while ((bytesRead = fis.read(buffer)) > 0) {
        md.update(buffer, 0, bytesRead);
      }
      
      byte[] md5 = md.digest();
      BigInteger bigInt = new BigInteger(1, md5);
      hash = bigInt.toString(16);
      
    } catch (FileNotFoundException e) {
      Ln.e("File was not found");
      return null;
    } catch (IOException e) {
      Ln.e(e.getMessage());
      return null;
    } finally {
      try {
        if (fis != null) {
          fis.close();
        }
      } catch (IOException e) {
        Ln.e("Failed to close file"); 
      }
    }
    
    return hash;
  }
  
  public static void copyRaw(AssetFileDescriptor fd, String destinationPath) throws IOException {
    copyRaw(fd, new File(destinationPath));
  }
  
  public static void copyRaw(AssetFileDescriptor fd, File destinationFile) throws IOException {
    FileChannel sourceChannel = new FileInputStream(fd.getFileDescriptor()).getChannel();
    copyNIO(sourceChannel, destinationFile, fd.getStartOffset(), fd.getLength());
  }
  
  public static void copyNIO(FileChannel source, File destination, long startOffset, long length) throws IOException  {
    if (source == null || destination == null) {
      throw new IllegalArgumentException("Source & destination must be non null");
    }
        
    FileChannel destinationChannel = null;
    
    try {
      destinationChannel = new FileOutputStream(destination).getChannel();
      source.transferTo(startOffset, length, destinationChannel);
    } finally {
      
      if (source != null) {
        source.close();
      }
      
      if (destinationChannel != null) {
        destinationChannel.close();
      }
    }
  }
}
