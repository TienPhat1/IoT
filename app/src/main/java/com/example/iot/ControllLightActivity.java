package com.example.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

public class ControllLightActivity extends AppCompatActivity {
    private BubbleSeekBar bubbleseekbar;
    private TextView value_controller_light;
    private int value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controll_light);

        bubbleseekbar = (BubbleSeekBar) findViewById(R.id.sbar_controller_light);
        value_controller_light = (TextView) findViewById(R.id.tv_value_controller_light);

        bubbleseekbar.setProgress(0);
        bubbleseekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                value = progress;
                value_controller_light.setText(Integer.toString(value));
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });

    }
}