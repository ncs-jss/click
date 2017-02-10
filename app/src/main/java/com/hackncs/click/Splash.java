package com.hackncs.click;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    TextView conn_err_msg;
    boolean isConnected, logged_in;
    View.OnClickListener submitListener;
    ProgressDialog progressDialog;
    String sUsername, sPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initialize(INITIAL_DISPLAY);
        verifyStoragePermissions(this);
        new BackgroundTasks().execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isConnected) {
                    if (logged_in) {
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        intent.putExtra("mode", "ONLINE_MODE");
                        startActivity(intent);
                    } else {
                        changeView();
                    }
                } else {

                    if (logged_in) {
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        intent.putExtra("mode", "OFFLINE_MODE");
                        startActivity(intent);
                    } else {
                        conn_err_msg.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, 3750);
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private void initialize(int condition) {
        switch (condition) {
            case INITIAL_DISPLAY:
                logo = (ImageView) findViewById(R.id.ivLogo);
                loader = (ProgressBar) findViewById(R.id.pbLoader);
                conn_err_msg = (TextView) findViewById(R.id.tvConnErrMsg);
                progress = 0;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                break;
            case LOGIN_DISPLAY:
                username = (EditText)findViewById(R.id.etUsername);
                password = (EditText)findViewById(R.id.etPassword);
                submit = (Button)findViewById(R.id.bSubmit);
                submit.setClickable(false);
                break;
        }
    }

    private void backgroundTasks() {
        int x = 0;
        isConnected = isOnline();
        logged_in = sharedPreferences.getBoolean("com.hackncs.click.LOGGED_IN", false);
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
                progressDialog = new ProgressDialog(Splash.this);
                sUsername = username.getText().toString().trim();
                sPassword = password.getText().toString().trim();
                new LoginTasks().execute();
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
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        /*NetworkInfo netInfo_bluetooth = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
        //NetworkInfo netInfo_dummy = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_DUMMY);
        //NetworkInfo netInfo_ethernet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        NetworkInfo netInfo_mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo netInfo_mobile_dun = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE_DUN);
        //NetworkInfo netInfo_vpn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_VPN);
        NetworkInfo netInfo_wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //NetworkInfo netInfo_wimax = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
        if (
                (       netInfo_bluetooth != null ||
                        //netInfo_dummy != null ||
                        //netInfo_ethernet != null ||
                        netInfo_mobile != null ||
                        netInfo_mobile_dun != null ||
                        //netInfo_vpn != null ||
                        netInfo_wifi != null)
                        //netInfo_wimax != null)

                        &&

                (       netInfo_bluetooth.isConnected() ||
                        //netInfo_dummy.isConnected() ||
                        //netInfo_ethernet.isConnected() ||
                        netInfo_mobile.isConnected() ||
                        netInfo_mobile_dun.isConnected() ||
                        //netInfo_vpn.isConnected() ||
                        netInfo_wifi.isConnected())
                        //netInfo_wimax.isConnected())
                ) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }*/
        if (networkInfo != null && networkInfo.isConnected()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }
        else {
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

    public class LoginTasks extends AsyncTask<String, Integer, String> {

        String token = "", group = "", user_id = "", first_name="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Logging you in...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
                if (!s.equals("ok")) {
                    Toast.makeText(Splash.this, "Login failed!", Toast.LENGTH_SHORT).show();
                } else {
                    editor = sharedPreferences.edit();
                    editor.putBoolean("com.hackncs.click.LOGGED_IN", true);
                    editor.putString("com.hackncs.click.TOKEN", token);
                    editor.putString("com.hackncs.click.GROUP", group);
                    editor.putString("com.hackncs.click.USER_ID", user_id);
                    editor.putString("com.hackncs.click.USERNAME", sUsername);
                    editor.putString("com.hackncs.click.FIRST_NAME", first_name);
                    editor.commit();
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    intent.putExtra("mode", "ONLINE_MODE");
                    startActivity(intent);
                }
        }

        @Override
        protected String doInBackground(String... strings) {
            final String[] status = {"fail"};
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://210.212.85.155/api/profiles/login/",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                token = jsonObject.getString("token");
                                group = jsonObject.getString("group");
                                user_id = jsonObject.getString("user_id");
                                first_name = jsonObject.getString("first_name");
                                //Toast.makeText(Splash.this, token+"\n"+group+"\n"+user_id, Toast.LENGTH_SHORT).show();
                                status[0] = "ok";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Splash.this, error.toString(), Toast.LENGTH_LONG).show();
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
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return status[0];
        }
    }
}
