package org.me.myandroidstuff.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class PlannedRoadworksActivity extends Activity {

    String urlString = "http://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    TextView textView;
    public List<Item> parsedList;
    private List<Button> buttonList;
    private List<TextView> roadWorksText = new ArrayList<TextView>();
    private TextView noRoadWorks = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planned_roadworks);
        textView = (TextView)findViewById(R.id.textView);
        new DownloadData().execute();

        final Intent intent = new Intent(PlannedRoadworksActivity.this, DisplayResultActivity.class);
        final Bundle bundle = new Bundle();
        buttonList = new ArrayList<Button>();

        final LinearLayout lL = (LinearLayout)findViewById(R.id.linearLayout);
        final DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
        Button b = (Button) findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (Button b : buttonList) {
                    lL.removeView(b);
                }
                buttonList.clear();
                lL.removeView(noRoadWorks);

                for(TextView view : roadWorksText){
                    lL.removeView(view);
                }


                int day = datePicker.getDayOfMonth();

                String dayResult = day + "";
                if (day < 10) {
                    dayResult = "0" + day;
                }

                String date = dayResult + " " + datePicker.getMonth() + " " + datePicker.getYear();
                final List<Item> displayList = TrafficListingTestProject.DisplayList(date, parsedList);

                if(!displayList.isEmpty())
                {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView textView = new TextView(PlannedRoadworksActivity.this);
                    textView.setText("Road Works");
                    textView.setLayoutParams(params);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.BLUE);
                    roadWorksText.add(textView);
                    Resources r = getResources();
                    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
                    float marginPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics());
                    params.setMargins((int) marginPx, (int) marginPx, (int) marginPx, (int) marginPx);
                    textView.setTextSize(px);
                    lL.addView(textView);
                    for(int i = 0; i < displayList.size(); i++)
                    {
                        Button btn = new Button(PlannedRoadworksActivity.this);
                        btn.setText(displayList.get(i).title);

                        //params.setMargins(0, 5, 0, 5); Need to convert px to dp http://stackoverflow.com/questions/12728255/in-android-how-do-i-set-margins-in-dp-programmatically
                        btn.setLayoutParams(params);
                        btn.setId(i);
                        //btn.setBackgroundColor(Color.parseColor("#e3e4ed"));
                        btn.getBackground().setColorFilter(Color.parseColor("#1c257a"), PorterDuff.Mode.DST);
                        btn.setTextColor(Color.parseColor("#58759f"));
                        lL.addView(btn);
                        buttonList.add(btn);

                        btn.setOnClickListener(new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                Item item = displayList.get(v.getId());
                                bundle.putParcelable("item", item);
                                intent.putExtras(bundle);

                                startActivity(intent);
                            }
                        });
                    }
                }
                else
                {
                    TextView tV = new TextView(PlannedRoadworksActivity.this);
                    tV.setText("There are no Roadworks that day!");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    tV.setGravity(Gravity.CENTER);
                    tV.setLayoutParams(params);
                    lL.addView(tV);
                    noRoadWorks = tV;
                }
            }
        });

    }

    private class DownloadData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params){
            String result = "";
            InputStream anInStream = null;
            int response = -1;
            URL url = null;

            try{

                url = new URL(urlString);
            }
            catch(MalformedURLException e ){
                e.printStackTrace();
            }
            URLConnection conn = null;
            try {
                conn = url.openConnection();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            try {
                // Check that the connection can be opened
                if (!(conn instanceof HttpURLConnection))
                    throw new IOException("Not an HTTP connection");
            }
            catch(IOException e){
                e.printStackTrace();
            }
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
                    String line = new String();
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
            catch (IOException ex)
            {
                ex.printStackTrace();
            }

            // Return result as a string for further processing
            return result;
        }

        protected void onPostExecute(String result){
            try {
                InputStream stream = new ByteArrayInputStream(result.getBytes("UTF-8"));
                try{
                    parsedList = TrafficListingTestProject.Parse(stream);
                }
                catch(XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }

        }

    }
}
