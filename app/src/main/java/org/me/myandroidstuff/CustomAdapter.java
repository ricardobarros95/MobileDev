package org.me.myandroidstuff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barros on 3/20/2016.
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

        roadWorksTitle.setText(item.title);
        imageView.setImageResource(R.drawable.roadworksign);
        return customView;
    }
}
