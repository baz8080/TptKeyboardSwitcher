package com.mbcdev.tptkeyboardswitcher.ui;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mbcdev.tptkeyboardswitcher.R;
import com.mbcdev.tptkeyboardswitcher.util.FsUtil;

public class TptKeyboardSwitcherActivity extends RoboActivity {
	
		@InjectView(R.id.btnTestRO) private Button btnReadOnly;
		@InjectView(R.id.btnTestRW) private Button btnReadWrite;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnReadOnly.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						FsUtil.roSystem();
					}
				});
        
        
        btnReadWrite.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						FsUtil.rwSystem();
					}
				});
    }
}