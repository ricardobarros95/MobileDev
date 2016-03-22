package org.me.myandroidstuff.activities;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.me.myandroidstuff.Item;
import org.me.myandroidstuff.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AmbientModeActivity extends Activity {

    AnimationDrawable workerAnimation;
    int speed = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambient_mode);

        Switch switchButton = (Switch)findViewById(R.id.switch1);
        switchButton.setChecked(true);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    finish();
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        ArrayList<Item> displayList = (ArrayList<Item>)bundle.get("displayList");
        ImageView workerImageView = (ImageView) findViewById(R.id.workerImageView);

        if(displayList.size() <= 3)
            workerImageView.setBackgroundResource(R.drawable.animation);
        else if(displayList.size() > 3 && displayList.size() <=6)
            workerImageView.setBackgroundResource(R.drawable.animation2);
        else
            workerImageView.setBackgroundResource(R.drawable.animation3);

        workerAnimation = (AnimationDrawable) workerImageView.getBackground();
        workerAnimation.start();


    }


}
