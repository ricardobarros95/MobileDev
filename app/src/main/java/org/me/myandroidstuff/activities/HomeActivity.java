package org.me.myandroidstuff.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    String roadWorksString = "http://trafficscotland.org/rss/feeds/roadworks.aspx";
    String plannedWorksString = "http://trafficscotland.org/rss/feeds/plannedroadworks.aspx";

    public ArrayList<Item> roadWorksParsedList;
    public List<Item> plannedWorksParsedList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        roadWorksParsedList = new ArrayList<Item>();

        new DownloadData(roadWorksString, roadWorksParsedList).execute();
        new DownloadData(plannedWorksString, plannedWorksParsedList).execute();


        Button b = (Button) findViewById(R.id.okButton);
        final Intent intent = new Intent(HomeActivity.this, TabsActivity.class);
        final Bundle bundle = new Bundle();

        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bundle.putParcelableArrayList("roadWorksParsedList", roadWorksParsedList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        String worksURL;
        List<Item> worksParsedList;

        public DownloadData(String url, List<Item> parsedList) {
            worksURL = url;
            worksParsedList = parsedList;
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            InputStream anInStream = null;
            int response = -1;
            URL url = null;

            try {

                url = new URL(worksURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            URLConnection conn = null;
            try {
                conn = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                // Check that the connection can be opened
                if (!(conn instanceof HttpURLConnection))
                    throw new IOException("Not an HTTP connection");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                // Open connection
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                response = httpConn.getResponseCode();
                // Check that connection is Ok
                if (response == HttpURLConnection.HTTP_OK) {
                    // Connection is Ok so open a reader
                    anInStream = httpConn.getInputStream();
                    InputStreamReader in = new InputStreamReader(anInStream);
                    BufferedReader bin = new BufferedReader(in);

                    // Read in the data from the RSS stream
                    String line = new String();
                    // Read past the RSS headers
                    bin.readLine();
                    bin.readLine();
                    // Keep reading until there is no more data
                    while (((line = bin.readLine())) != null) {
                        result = result + "\n" + line;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Return result as a string for further processing
            return result;
        }

        protected void onPostExecute(String result) {
            try {
                InputStream stream = new ByteArrayInputStream(result.getBytes("UTF-8"));
                try {
                    if(worksURL.equals(roadWorksString)){
                        roadWorksParsedList = TrafficListingTestProject.Parse(stream);
                    }
                    else if(worksURL.equals(plannedWorksString)) {
                        plannedWorksParsedList = TrafficListingTestProject.Parse(stream);
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
