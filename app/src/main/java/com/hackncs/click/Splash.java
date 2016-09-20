package com.hackncs.click;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Splash extends Activity {

    SplashView splashView;
    LinearLayout full;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        full = (LinearLayout)findViewById(R.id.llFull);
        splashView = new SplashView(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Bitmap mutableBitmap = Bitmap.createBitmap(displayMetrics.widthPixels,displayMetrics.heightPixels,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        splashView.draw(canvas);
        splashView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        full.addView(splashView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
