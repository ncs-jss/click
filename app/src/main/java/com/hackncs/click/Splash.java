package com.hackncs.click;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class Splash extends Activity {

    ImageView logo;
    ProgressBar loader;
    int progress;
    final int INITIAL_DISPLAY = 0, LOGIN_DISPLAY = 1;
    EditText username, password;
    CheckBox rememberMe;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initialize(INITIAL_DISPLAY);
        new BackgroundTasks().execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeView();
            }
        }, 4500);
    }

    private void initialize(int condition) {
        switch (condition) {
            case INITIAL_DISPLAY:
                logo = (ImageView) findViewById(R.id.ivLogo);
                loader = (ProgressBar) findViewById(R.id.pbLoader);
                progress = 0;
                break;
            case LOGIN_DISPLAY:
                username = (EditText)findViewById(R.id.etUsername);
                password = (EditText)findViewById(R.id.etPassword);
                rememberMe = (CheckBox)findViewById(R.id.cbRememberMe);
                submit = (Button)findViewById(R.id.bSubmit);
                break;
        }
    }

    private void backgroundTasks() {
        while (progress <= 100) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progress += 1;
            loader.setProgress(progress);
        }
    }

    private void animate(int condition) {
        switch (condition) {
            case INITIAL_DISPLAY:
                loader.animate().alpha(0.0f).setDuration(1000).start();
                logo.animate().y(200).setDuration(1000).setInterpolator(new AccelerateDecelerateInterpolator()).setStartDelay(500).start();
                break;
            case LOGIN_DISPLAY:
                Animation fade_in = new AnimationUtils().loadAnimation(this, android.R.anim.fade_in);
                fade_in.setInterpolator(new DecelerateInterpolator());
                fade_in.setDuration(500);
                username.startAnimation(fade_in);
                password.startAnimation(fade_in);
                rememberMe.startAnimation(fade_in);
                submit.startAnimation(fade_in);
                break;
        }
    }

    private void changeView() {
        setContentView(R.layout.login);
        initialize(LOGIN_DISPLAY);
        animate(LOGIN_DISPLAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public class BackgroundTasks extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            backgroundTasks();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            animate(INITIAL_DISPLAY);
        }
    }
}
