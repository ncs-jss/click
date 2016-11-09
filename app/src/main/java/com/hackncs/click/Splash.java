package com.hackncs.click;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Splash extends Activity {

    ImageView logo;
    ProgressBar loader;
    int progress;
    final int INITIAL_DISPLAY = 0, LOGIN_DISPLAY = 1;
    EditText username, password;
    Button submit;

    boolean isConnected;

    View.OnClickListener submitListener;

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
                submit.startAnimation(fade_in);
                break;
        }
    }

    private void changeView() {
        setContentView(R.layout.login);
        initialize(LOGIN_DISPLAY);
        animate(LOGIN_DISPLAY);
        submitListener =  new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String sUsername = username.getText().toString().trim();
                final String sPassword = password.getText().toString().trim();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Toast.makeText(Splash.this, jsonObject.getString("token"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Splash.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("username",sUsername);
                        map.put("password",sPassword);
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Splash.this);
                requestQueue.add(stringRequest);
            }
        };
        submit.setOnClickListener(submitListener);
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
