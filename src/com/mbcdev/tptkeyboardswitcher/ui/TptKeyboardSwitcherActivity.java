package com.mbcdev.tptkeyboardswitcher.ui;

import static com.mbcdev.tptkeyboardswitcher.util.NotificationUtils.toast;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mbcdev.tptkeyboardswitcher.R;
import com.mbcdev.tptkeyboardswitcher.command.MountCommand;
import com.mbcdev.tptkeyboardswitcher.command.MountCommand.MountOptions;
import com.mbcdev.tptkeyboardswitcher.command.TestFileCommand;

public class TptKeyboardSwitcherActivity extends RoboActivity {

  @InjectView(R.id.btnTestRO)
  private Button btnReadOnly;
  
  @InjectView(R.id.btnTestRW)
  private Button btnReadWrite;
  
  @InjectView(R.id.btnTestSym)
  private Button btnTestSym;
  
  @InjectView(R.id.txtTestFile)
  private EditText etTestFile;

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
    
    btnTestSym.setOnClickListener(new View.OnClickListener() {
      
      @Override
      public void onClick(View v) {
        handleTestSym();
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
  
  private void handleTestSym() {
    String filePath = etTestFile.getText().toString();
    
    TestFileCommand tfc = new TestFileCommand();
    
    try {
      boolean isSymbolic = tfc.isSymbolic(filePath);
      
      if (!isSymbolic) { toast(this, "Fook"); } else {toast(this, "Nook");}
      
    } catch (Exception e) {
      toast(this, e.getMessage());
      Ln.e(e);
    }
    
  }
  
}