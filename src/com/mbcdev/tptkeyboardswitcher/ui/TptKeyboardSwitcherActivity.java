package com.mbcdev.tptkeyboardswitcher.ui;

import static com.mbcdev.tptkeyboardswitcher.util.NotificationUtils.toast;

import java.io.File;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mbcdev.tptkeyboardswitcher.R;
import com.mbcdev.tptkeyboardswitcher.command.CommandRunner;
import com.mbcdev.tptkeyboardswitcher.command.CopyCommand;
import com.mbcdev.tptkeyboardswitcher.command.DeleteCommand;
import com.mbcdev.tptkeyboardswitcher.command.MountCommand;
import com.mbcdev.tptkeyboardswitcher.command.MountCommand.MountOptions;
import com.mbcdev.tptkeyboardswitcher.command.SymlinkCommand;
import com.mbcdev.tptkeyboardswitcher.util.FileUtils;
import com.mbcdev.tptkeyboardswitcher.util.FileUtils.FilePaths;

public class TptKeyboardSwitcherActivity extends RoboActivity {

  @InjectView(R.id.btnEnglishUk)
  private Button btnEnUk;
  
  @InjectView(R.id.btnEnglishUs)
  private Button btnEnUs;
  
  @InjectView(R.id.btnRevert)
  private Button btnRevert;
  
  private final static String DEVICE = "thinkpadtablet";
  private final static String KCM_SYMLINK_NAME = "Vendor_1241_Product_0003_Version_0110.kcm";
  private final static String KL_SYMLINK_NAME  = "Vendor_1241_Product_0003_Version_0110.kl";
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    btnEnUk.setOnClickListener(new View.OnClickListener() {
      
      @Override
      public void onClick(View v) {
        copyKeyboardConfig("en_uk");
      }
    });
    
    btnEnUs.setOnClickListener(new View.OnClickListener() {
      
      @Override
      public void onClick(View v) {
        copyKeyboardConfig("en_us");
      }
    });
    
    btnRevert.setOnClickListener(new View.OnClickListener() {
      
      @Override
      public void onClick(View v) {
        deleteSymLinks();
      }
    });
    
  }
  
  private void deleteSymLinks() {
    
    CommandRunner.Builder builder = new CommandRunner.Builder();
    
    CommandRunner runner = builder
        .runAsRoot()
        .command(new MountCommand(FilePaths.SYSTEM.getPath(), MountOptions.READ_WRITE))
        .command(new DeleteCommand(FilePaths.KEYCHARS.getPathTerminated() + KCM_SYMLINK_NAME))
        .command(new DeleteCommand(FilePaths.KEYLAYOUTS.getPathTerminated() + KL_SYMLINK_NAME))
        .command(new MountCommand(FilePaths.SYSTEM.getPath(), MountOptions.READ_ONLY))
        .build();
    
    int rc;
    
    try {
      rc = runner.execute();
      
      if (rc == 0) {
        toast(this, getString(R.string.success));
      } else {
        toast(this, getString(R.string.error_general));
      }
      
    } catch (Exception e) {
      toast(this, e.getMessage());
      Ln.e(e);
    }
  }
  
  private void copyKeyboardConfig(final String path) {
    
    File kcm = new File(getExternalFilesDir(null), DEVICE + "_" + path + ".kcm");
    File kl  = new File(getExternalFilesDir(null), DEVICE + "_" + path + ".kl" );
    
    String kcmAssetPath = DEVICE + File.separator + path + File.separator + "kcm.mp3";
    String klAssetPath  = DEVICE + File.separator + path + File.separator + "kl.mp3";
    
    CommandRunner.Builder builder = new CommandRunner.Builder();
    
    String kcmDestinationPath = FilePaths.KEYCHARS.getPathTerminated() + kcm.getName();
    String klDestinationPath  = FilePaths.KEYLAYOUTS.getPathTerminated() + kl.getName(); 
    
    CommandRunner runner = builder
      .runAsRoot()
      .command(new MountCommand(FilePaths.SYSTEM.getPath(), MountOptions.READ_WRITE))
      .command(new CopyCommand(kcm.getAbsolutePath(), kcmDestinationPath))
      .command(new CopyCommand(kl.getAbsolutePath(), klDestinationPath))
      .command(new DeleteCommand(FilePaths.KEYCHARS.getPathTerminated() + KCM_SYMLINK_NAME))
      .command(new DeleteCommand(FilePaths.KEYLAYOUTS.getPathTerminated() + KL_SYMLINK_NAME))
      .command(new SymlinkCommand(FilePaths.KEYCHARS.getPathTerminated() + KCM_SYMLINK_NAME, kcmDestinationPath))
      .command(new SymlinkCommand(FilePaths.KEYLAYOUTS.getPathTerminated() + KL_SYMLINK_NAME, klDestinationPath))
      .command(new DeleteCommand(kcm.getAbsolutePath()))
      .command(new DeleteCommand(kl.getAbsolutePath()))
      
      .command(new MountCommand(FilePaths.SYSTEM.getPath(), MountOptions.READ_ONLY))
      .build();
    
    try {
      AssetFileDescriptor kcmAssetDescriptor = getAssets().openFd(kcmAssetPath);
      AssetFileDescriptor klAssetDescriptor  = getAssets().openFd(klAssetPath);
      
      FileUtils.copyRaw(kcmAssetDescriptor, kcm.getAbsolutePath());
      FileUtils.copyRaw(klAssetDescriptor, kl.getAbsolutePath());
      
      int rc = runner.execute();
      
      if (rc == 0) {
        toast(this, getString(R.string.success));
      } else {
        toast(this, getString(R.string.error_general));
      }
      
    } catch (Exception e) {
      toast(this, e.getMessage());
      Ln.e(e);
    }
  }
  
}