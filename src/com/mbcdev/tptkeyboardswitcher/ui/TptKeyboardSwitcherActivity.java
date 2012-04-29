package com.mbcdev.tptkeyboardswitcher.ui;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mbcdev.tptkeyboardswitcher.R;

public class TptKeyboardSwitcherActivity extends RoboActivity {

  @InjectView(R.id.btnTest)
  private Button btnTest;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    
    btnTest.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(TptKeyboardSwitcherActivity.this, TesterActivity.class));
      }
    });
    
  }
  
}