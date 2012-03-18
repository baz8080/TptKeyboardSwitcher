package com.mbcdev.tptkeyboardswitcher.ui;

import static com.mbcdev.tptkeyboardswitcher.util.NotificationUtils.toast;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mbcdev.tptkeyboardswitcher.R;
import com.mbcdev.tptkeyboardswitcher.command.MountCommand;
import com.mbcdev.tptkeyboardswitcher.command.MountCommand.MountOptions;

public class TptKeyboardSwitcherActivity extends RoboActivity {

  @InjectView(R.id.btnTestRO)
  private Button btnReadOnly;
  
  @InjectView(R.id.btnTestRW)
  private Button btnReadWrite;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    btnReadOnly.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        handleMount(MountOptions.READ_ONLY);
      }      
    });

    btnReadWrite.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        handleMount(MountOptions.READ_WRITE);
      }
    });
  }
  
  private void handleMount(MountOptions options) {
    
    boolean b = false;
    MountCommand mc = new MountCommand();
    
    try {
      
      switch (options) {
      case READ_ONLY:
        b = mc.roSystem();
        if (!b) { toast(this, R.string.error_mount_ro); }
        break;
      case READ_WRITE:
        b = mc.rwSystem();
        if (!b) { toast(this, R.string.error_mount_rw); }
        break;
      }
      
    } catch (Exception e) {
      toast(this, e.getMessage());
      Ln.e(e);
    } 
    
  }
  
  
}