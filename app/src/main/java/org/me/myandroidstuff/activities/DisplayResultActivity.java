package org.me.myandroidstuff.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.me.myandroidstuff.Item;
import org.me.myandroidstuff.R;

/**
 * RICARDO GUILHERME COELHO BARROS
 * S1314084
 */

public class DisplayResultActivity extends Activity {

    Intent intent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);
        final Item item = (Item)getIntent().getParcelableExtra("item");

        TextView titleTV = (TextView)findViewById(R.id.Title);
        titleTV.setText(item.title);

        TextView descriptionTV = (TextView)findViewById(R.id.Description);
        String description = "";
        String sep = System.lineSeparator();
        for(int i = 0; i < item.descriptionInfo.size(); i++)
        {
            if(item.descriptionInfo.get(i) != null)
                description += sep + sep + " " + item.descriptionInfo.get(i);
        }
        descriptionTV.setText(description);

        Button btn = (Button)findViewById(R.id.linkButton);

        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Uri url = Uri.parse(item.link);
                Intent intent = new Intent(Intent.ACTION_VIEW, url);
                startActivity(intent);
            }
        });
    }
}