package org.me.myandroidstuff.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.me.myandroidstuff.R;
import org.me.myandroidstuff.TrafficListingTestProject;

public class ChooseWorksActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_works);

        Button roadWorksButton = (Button)findViewById(R.id.roadworksButton);
        final Intent roadWorksIntent = new Intent(ChooseWorksActivity.this, RoadWorksActivity.class);

        roadWorksButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(roadWorksIntent);
            }
        });

        Button plannedRoadWorksButton = (Button)findViewById(R.id.plannedRoadworksButton);
        final Intent plannedRoadworksIntent = new Intent(ChooseWorksActivity.this, PlannedRoadworksActivity.class);

        plannedRoadWorksButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(plannedRoadworksIntent);
            }
        });
    }
}
