package org.me.myandroidstuff.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;

import org.me.myandroidstuff.CustomAdapter;
import org.me.myandroidstuff.Item;
import org.me.myandroidstuff.R;

import java.util.ArrayList;

public class DisplayListActivity extends Activity {

    Switch switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);

        Bundle bundle = getIntent().getExtras();
        final ArrayList<Item> displayList = (ArrayList<Item>)bundle.get("displayList");
        final Intent displayIntent = new Intent(DisplayListActivity.this, DisplayResultActivity.class);
        final Bundle displaybundle = new Bundle();
        final Intent ambientModeIntent = new Intent(DisplayListActivity.this, AmbientModeActivity.class);


        switchButton = (Switch)findViewById(R.id.switch1);
        switchButton.setChecked(false);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ambientModeIntent.putExtra("displayList", displayList);
                    startActivity(ambientModeIntent);
                }
            }
        });

        ListAdapter adapter = new CustomAdapter(this, displayList);
        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) parent.getItemAtPosition(position);
                displaybundle.putParcelable("item", item);
                displayIntent.putExtras(displaybundle);
                startActivity(displayIntent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        switchButton.setChecked(false);
    }
}
