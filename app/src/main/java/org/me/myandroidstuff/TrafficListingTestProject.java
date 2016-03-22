package org.me.myandroidstuff;

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

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.me.myandroidstuff.activities.DisplayResultActivity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * RICARDO GUILHERME COELHO BARROS
 * S1314084
 */

public class TrafficListingTestProject extends Activity
{
    private static final String ns = null;
    public List<Item> parsedList;
    private List<Button> buttonList;
    private TextView noRoadWorks = null;

    /** Called when the activity is first created. */
    @Override 
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.d("testing", "hello?");
        // Get the TextView object on which to display the results
        buttonList = new ArrayList<Button>();
        final Intent intent = new Intent(TrafficListingTestProject.this, DisplayResultActivity.class);
        final Bundle bundle = new Bundle();
        Log.d("testing", "hello1?");
        String result;
        String sourceListingURL = "http://trafficscotland.org/rss/feeds/roadworks.aspx";

        try
        {
            Log.d("testing", "hello2?");
        	// Get the data from the XML stream as a string
        	result =  sourceListingString(sourceListingURL);
            InputStream stream = new ByteArrayInputStream(result.getBytes("UTF-8"));
            try
            {
                parsedList = Parse(stream);
                Log.d("testing", "hello3?");
            }
            catch(XmlPullParserException e)
            {
                Log.w("myApp", e.toString());
            }
            Log.d("testing", "hello4?");
            final LinearLayout lL = (LinearLayout)findViewById(R.id.linearLayout);
            final DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
            Button b = (Button) findViewById(R.id.searchButton);
            Log.d("testing", b.getId() + "");
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    for (Button b: buttonList)
                    {
                        lL.removeView(b);
                    }
                    Log.d("testing", "Search button clicked");
                    buttonList.clear();
                    lL.removeView(noRoadWorks);

                    int day = dp.getDayOfMonth();

                    String dayResult = day + "";
                    if(day <10)
                    {
                        dayResult = "0" + day;
                    }
                    String date = dayResult + " " + dp.getMonth() + " " + dp.getYear();

                    final List<Item> displayList = DisplayList(date, parsedList);
                    Log.d("Testing", displayList.size() + "");
                    if(!displayList.isEmpty())
                    {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        TextView textView = new TextView(TrafficListingTestProject.this);
                        textView.setText("Road Works");
                        textView.setLayoutParams(params);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextColor(Color.BLUE);
                        Resources r = getResources();
                        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
                        float marginPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics());
                        params.setMargins((int) marginPx, (int) marginPx, (int) marginPx, (int) marginPx);
                        textView.setTextSize(px);
                        lL.addView(textView);
                        for(int i = 0; i < displayList.size(); i++)
                        {
                            Button btn = new Button(TrafficListingTestProject.this);
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
                        TextView tV = new TextView(TrafficListingTestProject.this);
                        tV.setText("There are no Roadworks that day!");
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        tV.setGravity(Gravity.CENTER);
                        tV.setLayoutParams(params);
                        lL.addView(tV);
                        noRoadWorks = tV;
                    }
                }
            });

        	// Do some processing of the data to get the individual parts of the XML stream
        	// At some point put this processing into a separate thread of execution
        	
        	// Display the string in the TextView object just to demonstrate this capability
        	// This will need to be removed at some point


        }
        catch(IOException ae)
        {
        	ae.printStackTrace();

        } 
        
    } // End of onCreate



    // Method to handle the reading of the data from the XML stream
    public static String sourceListingString(String urlString)throws IOException
    {
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
    	return result;
    	
    } // End of sourceListingString

    public static ArrayList Parse(InputStream inputStream) throws XmlPullParserException, IOException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return ReadFeed(parser);
        }
        catch(XmlPullParserException e)
        {
            Log.w("myApp", e.toString());
            return null;
        }
        finally
        {
            inputStream.close();
        }
    }

	private static ArrayList ReadFeed(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		ArrayList entries = new ArrayList();

		parser.require(XmlPullParser.START_TAG, ns, "channel");
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if(parser.getEventType() != XmlPullParser.START_TAG)
			{
				continue;
			}
			String name = parser.getName();
			if(name.equals("item"))
			{
				entries.add(ReadItem(parser));
			}
			else
			{
				Skip(parser);
			}
		}
		return entries;
	}

    private static Item ReadItem(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String description = null;
        String link = null;
        List<String> descriptionInfo = new ArrayList<String>();
        String date[] = null;

        while(parser.next() != XmlPullParser.END_TAG)
        {
            if(parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            if(name.equals("title"))
            {
                title = ReadTitle(parser);
            }
            else if(name.equals("description"))
            {
                description = ReadDescription(parser);
                String[] s = description.split("(<br />)|(<br/>)");
                for(int i = 0; i < s.length; i++) {
                    descriptionInfo.add(s[i]);
                }
            }
            else if(name.equals("link"))
            {
                link = ReadLink(parser);
            }
            else Skip(parser);
        }
        return new Item(title, description, link, descriptionInfo, date);
    }

    private static void Skip(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        if(parser.getEventType()!= XmlPullParser.START_TAG)
        {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0)
        {
            switch (parser.next())
            {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private static String ReadTitle(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = ReadText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    private static String ReadDescription(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = ReadText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    private static String ReadLink(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = ReadText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    private static String ReadText(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String result = "";
        if(parser.next() == XmlPullParser.TEXT)
        {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    public static ArrayList DisplayList(String startDate, List<Item> parsedList)
    {
        ArrayList<Item> list = new ArrayList<Item>();
        for(int i = 0; i<parsedList.size(); i++)
        {
            if(parsedList.get(i).startDateString.trim().equals(startDate))
            {
                list.add(parsedList.get(i));
            }
        }
        return list;
    }

} // End of Activity class

