package com.mbcdev.tptkeyboardswitcher.util;

import android.content.Context;
import android.widget.Toast;

public class NotificationUtils {

  public static void toast(Context c, String text) {
    Toast.makeText(c, text, Toast.LENGTH_LONG).show();
  }
  
  public static void toast(Context c, int textResId) {
    Toast.makeText(c, textResId, Toast.LENGTH_LONG).show();
  }
  
}
