package org.me.myandroidstuff.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TabHost;

import org.me.myandroidstuff.Item;
import org.me.myandroidstuff.R;
import org.me.myandroidstuff.TrafficListingTestProject;

import java.util.ArrayList;
import java.util.List;

public class TabsActivity extends ListActivity {

    ArrayList roadWorksParsedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs2);
        final Context context = this;

        //ArrayList roadWorksParsedList; //TODO put this on the search button listener
        //Log.d("Testing", roadWorksParsedList.size() + "");


        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpecs = tabHost.newTabSpec("tag1");
        tabSpecs.setContent(R.id.linearLayout2);
        tabSpecs.setIndicator("Roadworks");
        tabHost.addTab(tabSpecs);

        tabSpecs = tabHost.newTabSpec("tag2");
        tabSpecs.setContent(R.id.linearLayout3);
        tabSpecs.setIndicator("Planned RoadWorks");
        tabHost.addTab(tabSpecs);

        final List<Button> buttonList = new ArrayList<Button>();
        final LinearLayout roadWorksLayout = (LinearLayout)findViewById(R.id.linearLayout2);
        final DatePicker roadWorksDatePicker = (DatePicker)findViewById(R.id.roadWorksDatePicker);
        Button searchButton = (Button)findViewById(R.id.searchRoadWorks);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                roadWorksParsedList = getIntent().getParcelableArrayListExtra("roadWorksParsedList");
                for(Button b : buttonList){
                    //TODO REMOVE BUTTONS FROM DIALOG, STILL NOT SURE IF I ACTUALLY NEED TO DO THIS
                }
                buttonList.clear();

                int day = roadWorksDatePicker.getDayOfMonth();
                String dayResult = day + "";
                if(day < 10){
                    dayResult = "0" + day;
                }
                String date = dayResult + " " + roadWorksDatePicker.getMonth() + " " + roadWorksDatePicker.getYear();
                final List<Item> displayList = TrafficListingTestProject.DisplayList(date, roadWorksParsedList);

//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//                alertDialog.setTitle("RoadWorks");
//                alertDialog.setMessage("roadworks here");

                getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Item item = displayList.get(position);
                    }
                });

                if(!displayList.isEmpty()){
                    for(int i = 0; i < displayList.size(); i++){

                    }
                }

                //alertDialog.setItems()

               // AlertDialog alertDialog1 = alertDialog.create();
               // alertDialog1.show();
            }
        });

    }


}
