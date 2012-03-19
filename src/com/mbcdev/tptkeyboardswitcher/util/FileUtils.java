package com.mbcdev.tptkeyboardswitcher.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
