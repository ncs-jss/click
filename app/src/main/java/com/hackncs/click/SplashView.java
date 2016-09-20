package com.hackncs.click;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public class SplashView extends View{
    Bitmap logo;
    float coordinateX, coordinateY;
    public SplashView(Context context) {
        super(context);
        logo = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#2d2251"));
        canvas.drawBitmap(Bitmap.createScaledBitmap(logo,300,300,false),((canvas.getWidth()/2)-150),((canvas.getHeight()/2)-150),null);
    }
}
