package org.me.myandroidstuff.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import org.me.myandroidstuff.Item;
import org.me.myandroidstuff.R;
import org.me.myandroidstuff.TrafficListingTestProject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TabsActivity extends Activity {

    ArrayList<Item> roadWorksParsedList;
    ArrayList<Item> plannedWorksParsedList;
    String roadWorksURL = "http://trafficscotland.org/rss/feeds/roadworks.aspx";
    String plannedWorksURL = "http://trafficscotland.org/rss/feeds/plannedroadworks.aspx";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs2);
        final Context context = this;

        final Intent listIntent = new Intent(TabsActivity.this, DisplayListActivity.class);

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Downloading data");
        progress.setCancelable(false);
        progress.show();

        //Get roadworks XML data in a seperate thread
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    final String result = TrafficListingTestProject.sourceListingString(roadWorksURL);
                    InputStream stream = new ByteArrayInputStream(result.getBytes("UTF-8"));
                    try {
                        roadWorksParsedList = TrafficListingTestProject.Parse(stream);
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IOException", e.toString());
                }
            }
        });
        thread.start();



        //Get planned roadworks in another thread
        Thread plannedThread = new Thread(new Runnable() {
            public void run() {
                try {
                    final String result = TrafficListingTestProject.sourceListingString(plannedWorksURL);
                    InputStream stream = new ByteArrayInputStream(result.getBytes("UTF-8"));
                    try {
                        plannedWorksParsedList = TrafficListingTestProject.Parse(stream);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progress.hide();
                            }
                        });
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IOException", e.toString());
                }
            }
        });
        plannedThread.start();

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpecs = tabHost.newTabSpec("tag1");
        tabSpecs.setContent(R.id.linearLayout2);
        tabSpecs.setIndicator("Roadworks");
        tabHost.addTab(tabSpecs);

        tabSpecs = tabHost.newTabSpec("tag2");
        tabSpecs.setContent(R.id.linearLayout3);
        tabSpecs.setIndicator("Planned RoadWorks");
        tabHost.addTab(tabSpecs);


        final DatePicker roadWorksDatePicker = (DatePicker) findViewById(R.id.roadWorksDatePicker);
        Button searchButton = (Button) findViewById(R.id.searchRoadWorks);

        final TextView noRoadworksText = (TextView)findViewById(R.id.noRoadworks);
        final TextView noPlannedworksText = (TextView)findViewById(R.id.noPlannedRoadworks);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int day = roadWorksDatePicker.getDayOfMonth();
                String dayResult = day + "";
                if (day < 10) {
                    dayResult = "0" + day;
                }
                String date = dayResult + " " + roadWorksDatePicker.getMonth() + " " + roadWorksDatePicker.getYear();

                final ArrayList<Item> displayList = TrafficListingTestProject.DisplayList(date, roadWorksParsedList);
                if (displayList.size() > 0) {
                    listIntent.putExtra("displayList", displayList);
                    startActivity(listIntent);
                } else {
                    noRoadworksText.setText("No Roadworks available!");
                }
            }
        });

        final DatePicker plannedWorksDatePicker = (DatePicker) findViewById(R.id.plannedWorksDatePicker);
        Button plannedSearchButton = (Button)findViewById(R.id.searchPlannedWorks);

        plannedSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int day = plannedWorksDatePicker.getDayOfMonth();
                String dayResult = day + "";
                if (day < 10) {
                    dayResult = "0" + day;
                }
                String date = dayResult + " " + plannedWorksDatePicker.getMonth() + " " + plannedWorksDatePicker.getYear();

                final ArrayList<Item> displayList = TrafficListingTestProject.DisplayList(date, plannedWorksParsedList);
                if(displayList.size() > 0) {
                    listIntent.putExtra("displayList", displayList);
                    startActivity(listIntent);
                }else{
                    noPlannedworksText.setText("No Roadworks available!");
                }

            }
        });
    }
}
