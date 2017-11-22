package com.hackncs.click;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class FragmentStudentProfile extends Fragment implements View.OnClickListener{

    View view;
    Context context;
    EditText course, branch, year, section, univ_roll_no, contact_no, father_name, mother_name, address;
    CheckBox display;
    Button edit, save;
    String PROFILE_ID, TOKEN;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_profile,container,false);
        Iconify.with(new FontAwesomeModule());
        initialize();
        fetchAndDisplay();
        MainActivity.menu.getItem(0).setIcon( new IconDrawable(context, FontAwesomeIcons.fa_edit)
                .colorRes(R.color.white)
                .actionBarSize());
        MainActivity.menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String title=item.getTitle().toString();
                if(title.equals("Edit"))
                {
                    item.setIcon( new IconDrawable(context, FontAwesomeIcons.fa_save)
                            .colorRes(R.color.white)
                            .actionBarSize());
                    item.setTitle("Save");
                    enableViews();

                }
                else
                {
                    item.setIcon( new IconDrawable(context, FontAwesomeIcons.fa_edit)
                            .colorRes(R.color.white)
                            .actionBarSize());
                    item.setTitle("Edit");
                    uploadChanges();
                }


                return false;
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {

        /*Log.d("asd", "onclick");
        if (view.getId() == R.id.bEdit) {
            enableViews();
        }
        else if (view.getId() == R.id.bSave) {
            Log.d("llll", "a");
            uploadChanges();
        }*/
    }

    private void fetchAndDisplay() {
        String URL = Endpoints.student_profile_data;
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
        course.setText(jsonObject.getString("course"));
        branch.setText(jsonObject.getString("branch"));
        year.setText(jsonObject.getString("year"));
        section.setText(jsonObject.getString("section"));
        univ_roll_no.setText(jsonObject.getString("univ_roll_no"));
        contact_no.setText(jsonObject.getString("contact_no"));
        father_name.setText(jsonObject.getString("father_name"));
        mother_name.setText(jsonObject.getString("mother_name"));
        address.setText(jsonObject.getString("address"));
        display.setChecked(jsonObject.getBoolean("display_to_others"));
        disableViews();
    }

    private void disableViews() {
        course.setEnabled(false);
        branch.setEnabled(false);
        year.setEnabled(false);
        section.setEnabled(false);
        univ_roll_no.setEnabled(false);
        contact_no.setEnabled(false);
        father_name.setEnabled(false);
        mother_name.setEnabled(false);
        address.setEnabled(false);
        display.setEnabled(false);
        save.setVisibility(View.INVISIBLE);
    }
    private void enableViews() {
        course.setEnabled(true);
        branch.setEnabled(true);
        year.setEnabled(true);
        section.setEnabled(true);
        univ_roll_no.setEnabled(true);
        contact_no.setEnabled(true);
        father_name.setEnabled(true);
        mother_name.setEnabled(true);
        address.setEnabled(true);
        display.setEnabled(true);
        save.setVisibility(View.VISIBLE);
    }


    private void initialize() {
        context = getActivity().getApplicationContext();
        course = (EditText)view.findViewById(R.id.etCourse);
        branch = (EditText)view.findViewById(R.id.etBranch);
        year = (EditText)view.findViewById(R.id.etYear);
        section = (EditText)view.findViewById(R.id.etSection);
        univ_roll_no = (EditText)view.findViewById(R.id.etUnivRollNo);
        contact_no = (EditText)view.findViewById(R.id.etContactNo);
        father_name = (EditText)view.findViewById(R.id.etFatherName);
        mother_name = (EditText)view.findViewById(R.id.etMotherName);
        address = (EditText)view.findViewById(R.id.etAddress);
        display = (CheckBox)view.findViewById(R.id.cbDisplay);
        edit = (Button)view.findViewById(R.id.bEdit);
        save = (Button)view.findViewById(R.id.bSave);
        edit.setOnClickListener(this);
        save.setOnClickListener(this);
        save.setVisibility(View.INVISIBLE);
        PROFILE_ID = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("com.hackncs.click.PROFILE_ID","0");
        TOKEN = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("com.hackncs.click.TOKEN","0");
    }




    private void uploadChanges() {
        String URL = Endpoints.student_profile_data;
        Log.d("llll", "upload()");
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
                params.put("course",course.getText().toString());
                params.put("branch",branch.getText().toString());
                params.put("year",year.getText().toString());
                params.put("section",section.getText().toString());
                params.put("univ_roll_no",univ_roll_no.getText().toString());
                params.put("contact_no",contact_no.getText().toString());
                params.put("father_name",father_name.getText().toString());
                params.put("mother_name",mother_name.getText().toString());
                params.put("address",address.getText().toString());
                params.put("display_to_others",String.valueOf(display.isChecked()));
                Log.d("llll","insinde getParams");
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
