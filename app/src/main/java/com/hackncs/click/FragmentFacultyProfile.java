package com.hackncs.click;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class FragmentFacultyProfile extends Fragment implements View.OnClickListener{

    View view;
    Context context;
    EditText designation, department, contact_no, address, alternate_email;
    CheckBox display;
    Button edit, save;
    String PROFILE_ID, TOKEN;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_faculty_profile,container,false);
        initialize();
        fetchAndDisplay();
        return view;
    }

    private void fetchAndDisplay() {
        String URL = "http://210.212.85.155/api/profiles/faculty_profile_data/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL + PROFILE_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            setTexts(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "token " + TOKEN);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void setTexts(JSONObject jsonObject) throws JSONException {
        designation.setText(jsonObject.getString("designation"));
        department.setText(jsonObject.getString("department"));
        contact_no.setText(jsonObject.getString("contact_no"));
        address.setText(jsonObject.getString("address"));
        alternate_email.setText(jsonObject.getString("alternate_email"));
        display.setChecked(jsonObject.getBoolean("display_to_others"));
        disableViews();
    }

    private void disableViews() {
        designation.setEnabled(false);
        department.setEnabled(false);
        contact_no.setEnabled(false);
        address.setEnabled(false);
        alternate_email.setEnabled(false);
        display.setEnabled(false);
        save.setVisibility(View.INVISIBLE);
    }

    private void initialize() {
        context = getActivity().getApplicationContext();
        designation = (EditText)view.findViewById(R.id.etDesignationF);
        department = (EditText)view.findViewById(R.id.etDepartmentF);
        contact_no = (EditText)view.findViewById(R.id.etContactF);
        address = (EditText)view.findViewById(R.id.etAddressF);
        alternate_email = (EditText)view.findViewById(R.id.etAltEmailF);
        edit = (Button)view.findViewById(R.id.bEditF);
        save = (Button)view.findViewById(R.id.bSaveF);
        display = (CheckBox)view.findViewById(R.id.cbDisplayF);
        edit.setOnClickListener(this);
        save.setOnClickListener(this);
        save.setVisibility(View.INVISIBLE);
        PROFILE_ID = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("com.hackncs.click.PROFILE_ID","0");
        TOKEN = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("com.hackncs.click.TOKEN","0");
    }

    @Override
    public void onClick(View view) {

        Log.d("asd", "onclick");
        if (view.getId() == R.id.bEditF) {
            enableViews();
        }
        else if (view.getId() == R.id.bSaveF) {
            Log.d("llll", "a");
            uploadChanges();
        }
    }

    private void enableViews() {
        designation.setEnabled(true);
        department.setEnabled(true);
        contact_no.setEnabled(true);
        address.setEnabled(true);
        alternate_email.setEnabled(true);
        display.setEnabled(true);
        save.setVisibility(View.VISIBLE);
    }

    private void uploadChanges() {
        String URL = "http://210.212.85.155/api/profiles/faculty_profile_data/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + PROFILE_ID + "/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        disableViews();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("designation",designation.getText().toString());
                params.put("department",department.getText().toString());
                params.put("contact_no",contact_no.getText().toString());
                params.put("address",address.getText().toString());
                params.put("alternate_email",alternate_email.getText().toString());
                params.put("display_to_others",String.valueOf(display.isChecked()));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Token " + TOKEN);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
