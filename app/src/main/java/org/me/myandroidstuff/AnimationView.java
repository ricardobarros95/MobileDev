package org.me.myandroidstuff;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

/**
 * Created by Barros on 3/21/2016.
 */
public class AnimationView extends View {

    Bitmap worker;
    Bitmap worker1;
    Bitmap worker2;
    Bitmap worker3;
    Bitmap worker4;
    Bitmap currentWorker;

    double startTime = System.currentTimeMillis();


    public AnimationView(Context context){
        super(context);
        worker = BitmapFactory.decodeResource(getResources(), R.drawable.ani1);
        worker1 = BitmapFactory.decodeResource(getResources(), R.drawable.ani2);
        worker2 = BitmapFactory.decodeResource(getResources(), R.drawable.ani3);
        worker3 = BitmapFactory.decodeResource(getResources(), R.drawable.ani4);
        worker4 = BitmapFactory.decodeResource(getResources(), R.drawable.ani5);
        currentWorker = worker1;
    }

    @Override
    protected  void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(currentWorker, (canvas.getWidth()/2), 0, null);

        double currentTime = System.currentTimeMillis() - startTime;

        Log.d("testing", startTime + "");
        if(startTime > 2000)
        {
            currentWorker = worker1;
        }
        else if(startTime > 4000){
            currentWorker = worker2;
        }
        //invalidate();
    }
}
