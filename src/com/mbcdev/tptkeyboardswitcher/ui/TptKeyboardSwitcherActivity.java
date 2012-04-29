package com.mbcdev.tptkeyboardswitcher.ui;

import static com.mbcdev.tptkeyboardswitcher.util.NotificationUtils.toast;

import java.io.File;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.content.res.Resources.NotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mbcdev.tptkeyboardswitcher.R;
import com.mbcdev.tptkeyboardswitcher.command.CommandRunner;
import com.mbcdev.tptkeyboardswitcher.command.CopyCommand;
import com.mbcdev.tptkeyboardswitcher.command.DeleteCommand;
import com.mbcdev.tptkeyboardswitcher.command.FileTestCommand;
import com.mbcdev.tptkeyboardswitcher.command.FileTestCommand.FileTestType;
import com.mbcdev.tptkeyboardswitcher.command.MountCommand;
import com.mbcdev.tptkeyboardswitcher.command.MountCommand.MountOptions;
import com.mbcdev.tptkeyboardswitcher.util.FileUtils;
import com.mbcdev.tptkeyboardswitcher.util.FileUtils.FilePaths;

public class TptKeyboardSwitcherActivity extends RoboActivity {

  @InjectView(R.id.btnTestRO)
  private Button btnReadOnly;
  
  @InjectView(R.id.btnTestRW)
  private Button btnReadWrite;
  
  @InjectView(R.id.btnTestSym)
  private Button btnTestSym;
  
  @InjectView(R.id.txtTestFile)
  private EditText etTestFile;

  @InjectView(R.id.btnTestCopy) 
  private Button btnTestCopy;
  
  @InjectView(R.id.tvBuildInfo)
  private TextView tvBuildInfo;
  
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
        handleFileTest(FileTestType.SYMLINK);
      }
    });
    
    btnTestCopy.setOnClickListener(new View.OnClickListener() {
      
      @Override
      public void onClick(View v) {
        handleCopy();
      }
    });
    
    populateBuildInfo();
  }
  
  private void populateBuildInfo () {
  	StringBuilder sb = new StringBuilder();
  	
  	sb.append("Device: ").append(Build.DEVICE).append("\n")
  	.append("Product: ").append(Build.PRODUCT).append("\n")
  	.append("Model: ").append(Build.MODEL).append("\n");
  	
  	tvBuildInfo.setText(sb.toString());
  }
  
  private void handleMount(MountOptions options) {
    
    CommandRunner.Builder builder = new CommandRunner.Builder();
    
    CommandRunner runner = builder
        .command(new MountCommand("/system", options))
        .runAsRoot()
        .build();
    
    try {
      int i = runner.execute();
      toast(this, "rc is " + i);
    } catch (Exception e) {
      toast(this, e.getMessage());
      Ln.e(e);
    }
    
  }
  
  private void handleFileTest(FileTestType testType) {
    String filePath = etTestFile.getText().toString();
    
    
    CommandRunner.Builder builder = new CommandRunner.Builder();
    
    CommandRunner runner = builder
        .command(new FileTestCommand(testType, filePath))
        .build();
    
    try {
      int rc = runner.execute();
      
      if (rc == 0) { toast(this, "rc==0"); } else {toast(this, "rc<>0");}
      
    } catch (Exception e) {
      Ln.e(e);
      toast(this, e.getMessage());
    }   
  }
  
  private void handleCopy() {
        
    File extFile = new File(getExternalFilesDir(null), "uk.kcm");
    String extFilePath = extFile.getAbsolutePath();
    
    CommandRunner.Builder builder = new CommandRunner.Builder();
    
    CommandRunner runner = builder
      .runAsRoot()
      .command(new MountCommand(FilePaths.SYSTEM.getPath(), MountOptions.READ_WRITE))
      .command(new CopyCommand(extFilePath, FilePaths.KEYCHARS.getPathTerminated()))
      .command(new DeleteCommand(extFilePath))
      .command(new MountCommand(FilePaths.SYSTEM.getPath(), MountOptions.READ_ONLY))
      .build();
    
    try {
      FileUtils.copyRaw(
          getResources().openRawResourceFd(R.raw.thinkpaduk), extFilePath);
      
      int rc = runner.execute();
      if (rc == 0) { toast(this, "rc==0"); } else {toast(this, "rc<>0");}
      
    } catch (NotFoundException e) {
      toast(this, e.getMessage());
      Ln.e(e);
    } catch (Exception e) {
      toast(this, e.getMessage());
      Ln.e(e);
    }
  }
}