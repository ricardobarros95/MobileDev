package org.me.myandroidstuff;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * RICARDO GUILHERME COELHO BARROS
 * S1314084
 */
public class CustomAdapter extends ArrayAdapter<Item> {

    public CustomAdapter(Context context, List<Item> itemList){
        super(context, R.layout.custom_item, itemList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_item, parent, false);

        Item item = getItem(position);
        TextView roadWorksTitle = (TextView) customView.findViewById(R.id.roadworksTitle);
        ImageView imageView = (ImageView) customView.findViewById(R.id.colorRepres);

        //Assign a different image based on the duration
        if(item.duration < 7) {
            imageView.setImageResource(R.drawable.greenroadworksign);
        }

        else if(item.duration >=7 && item.duration <= 14){
            imageView.setImageResource(R.drawable.ywlloeroadworksign);
        }

        else if(item.duration > 15){
            imageView.setImageResource(R.drawable.roadworksign);
        }

        roadWorksTitle.setText(item.title);

        return customView;
    }
}
