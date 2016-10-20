package com.hackncs.click;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

public class Splash extends Activity {

    ImageView logo;
    ProgressBar loader;
    int progress;
    final int INITIAL_DISPLAY = 0, LOGIN_DISPLAY = 1;
    EditText username, password;
    CheckBox rememberMe;
    Button submit;

    boolean isConnected;

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
        }, 3750);
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
        int x = 0;
        isConnected = isOnline();
        connectFolder();
        while (progress <= 100) {
            if (progress % 5 == 0)
                x++;
            try {
                Thread.sleep(10+x);
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

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Disconnected!", Toast.LENGTH_SHORT).show();
                }
            });
            return false;
        }
    }

    private boolean connectFolder() {
        File folder = new File(Environment.getExternalStorageDirectory()+"/InfoConnect");
        boolean status = folder.exists();
        if (!status) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Creating directory...", Toast.LENGTH_SHORT).show();
                }
            });
            status = folder.mkdir();
        }
        return status;
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
