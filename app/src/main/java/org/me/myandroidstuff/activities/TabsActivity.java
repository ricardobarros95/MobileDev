package org.me.myandroidstuff.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.me.myandroidstuff.CustomAdapter;
import org.me.myandroidstuff.Item;
import org.me.myandroidstuff.R;
import org.me.myandroidstuff.TrafficListingTestProject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class TabsActivity extends Activity {

    ArrayList roadWorksParsedList;
    static Spinner spinner = null;
    String url = "http://trafficscotland.org/rss/feeds/roadworks.aspx";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs2);
        final Context context = this;
        final Intent displayIntent = new Intent(TabsActivity.this, DisplayResultActivity.class);
        final Bundle displaybundle = new Bundle();

        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    final String result = TrafficListingTestProject.sourceListingString(url);
                    InputStream stream = new ByteArrayInputStream(result.getBytes("UTF-8"));
                    try {
                        roadWorksParsedList = TrafficListingTestProject.Parse(stream);
                    }
                    catch(XmlPullParserException e){
                        e.printStackTrace();
                    }
                }
                catch(IOException e){
                    e.printStackTrace();
                    Log.e("IOException", e.toString());
                }
            }
        }); thread.start();
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
               // roadWorksParsedList = getIntent().getParcelableArrayListExtra("roadWorksParsedList");
                for(Button b : buttonList){
                    //TODO REMOVE BUTTONS FROM DIALOG, STILL NOT SURE IF I ACTUALLY NEED TO DO THIS
                }
                buttonList.clear();

                Log.d("Testing", "Hello");
                int day = roadWorksDatePicker.getDayOfMonth();
                String dayResult = day + "";
                if(day < 10){
                    dayResult = "0" + day;
                }
                String date = dayResult + " " + roadWorksDatePicker.getMonth() + " " + roadWorksDatePicker.getYear();

                //HomeActivity homeActivity = new HomeActivity();
                final ArrayList<Item> displayList = TrafficListingTestProject.DisplayList(date, roadWorksParsedList);


                Log.d("Testing", displayList.size() + "");


                ListAdapter adapter = new CustomAdapter(context, displayList);
                ListView listView = (ListView)findViewById(R.id.listView);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Item item = (Item)parent.getItemAtPosition(position);
                        displaybundle.putParcelable("item", item);
                        displayIntent.putExtras(displaybundle);
                        startActivity(displayIntent);
                    }
                });

            }
        });

    }


    public void DownloadData(String urlString) throws IOException{
        String result = "";
        InputStream anInStream = null;
        int response = -1;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        // Check that the connection can be opened
        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try
        {
            // Open connection
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            // Check that connection is Ok
            if (response == HttpURLConnection.HTTP_OK)
            {
                // Connection is Ok so open a reader
                anInStream = httpConn.getInputStream();
                InputStreamReader in= new InputStreamReader(anInStream);
                BufferedReader bin= new BufferedReader(in);

                // Read in the data from the RSS stream
                String line = "";
                // Read past the RSS headers
                bin.readLine();
                bin.readLine();
                // Keep reading until there is no more data
                while (( (line = bin.readLine())) != null)
                {
                    result = result + "\n" + line;
                }
            }
        }
        catch (Exception ex)
        {
            throw new IOException("Error connecting");
        }

        // Return result as a string for further processing
    }
}
