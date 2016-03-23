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

/**
 * RICARDO GUILHERME COELHO BARROS
 * S1314084
 */

public class HomeActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button b = (Button) findViewById(R.id.okButton);
        intent = new Intent(HomeActivity.this, TabsActivity.class);


        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(intent);
            }
        });

    }

}