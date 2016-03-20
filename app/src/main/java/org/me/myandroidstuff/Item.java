package org.me.myandroidstuff;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * RICARDO GUILHERME COELHO BARROS
 * S1314084
 */
public class Item implements Parcelable
{
    public String title;
    public String description;
    public String link;
    public List<String> descriptionInfo = new ArrayList<String>(); // 0 = Start Date, 1 = End Date, 2+ = Delay Information
    public String[] date = new String[3]; // 0 = weekday, 1 = day, 2 = month, 3 = year
    public String startDate;
    public Date endDate;
    public Date formatedStartDate;
    public enum Month{
        JANUARY, FEBRUARY, MARCH, APRIL,
        MAY, JUNE, JULY, AUGUST, SEPTEMBER,
        OCTOBER, NOVEMBER, DECEMBER
    }
    public Month month;

    public Item(String title, String description, String link, List<String> descriptionInfo, String[] date)
    {
        this.title = title;
        this.description = description;
        this.link = link;
        this.descriptionInfo = descriptionInfo;
        String s;
        s = descriptionInfo.get(0).replaceAll("Start Date:", " ");
        s = s.replaceAll("- 00:00", " ");
        s = s.trim();
        date = s.split(" ");
        date[0] = date[0].replace(',', ' ');
        date[0] = date[0].trim();
        this.date = date;

        String theStartDate = descriptionInfo.get(0).replaceAll("Start Date:", " ");
        theStartDate = theStartDate.replaceAll("- 00:00", " ");
        theStartDate = theStartDate.trim();
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyy", Locale.UK);
        Date formatedStartDate = null;

        String theEndDate = descriptionInfo.get(1).replaceAll("End Date:", " ");
        theEndDate = theEndDate.replaceAll("- 00:00", " ");
        theEndDate = theEndDate.trim();


        try {
            formatedStartDate = dateFormat.parse(theStartDate);
            this.formatedStartDate = formatedStartDate;
            formatedStartDate = dateFormat.parse(theEndDate);
            this.endDate = formatedStartDate;
        }
        catch(ParseException e){
            e.printStackTrace();
        }

        if(date[2].equals("January")) month = Month.JANUARY;
        else if(date[2].equals("February")) month = Month.FEBRUARY;
        else if(date[2].equals("March")) month = Month.MARCH;
        else if(date[2].equals("April")) month = Month.APRIL;
        else if(date[2].equals("May"))month = Month.MAY;
        else if(date[2].equals("June")) month = Month.JUNE;
        else if(date[2].equals("July")) month = Month.JULY;
        else if(date[2].equals("August")) month = Month.AUGUST;
        else if(date[2].equals("October")) month = Month.OCTOBER;
        else if(date[2].equals("November")) month = Month.NOVEMBER;
        else if(date[2].equals("December")) month = Month.DECEMBER;
        else month = null;

        this.startDate = date[1] + " " + month.ordinal() + " " + date[3];
    }

    public Item()
    {

    }

    public static final Parcelable.Creator<Item> CREATOR = new Creator<Item>() {
        public Item createFromParcel(Parcel source) {
            Item item = new Item();
            item.title = source.readString();
            item.description = source.readString();
            item.link = source.readString();
            source.readStringList(item.descriptionInfo);
            item.startDate = source.readString();
            item.endDate = (Date)source.readSerializable();
            item.formatedStartDate = (Date)source.readSerializable();
            return item;
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags)
    {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(link);
        parcel.writeStringList(descriptionInfo);
        parcel.writeString(startDate);
        parcel.writeSerializable(this.endDate);
        parcel.writeSerializable(this.formatedStartDate);
    }

    @Override
    public String toString() {
        return this.title;
    }
}