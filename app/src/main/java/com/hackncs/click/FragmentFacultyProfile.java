package com.hackncs.click;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentFacultyProfile extends Fragment implements View.OnClickListener {

    View view;
    Context context;
    TextInputEditText designation, department, contact_no, address, alternate_email;
    String PROFILE_ID, TOKEN;
    Menu menu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_faculty_profile, container, false);
        initialize();
        fetchAndDisplay();
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String title = item.getTitle().toString();
                if (title.equals("Edit")) {
                    item.setIcon(new IconDrawable(context, FontAwesomeIcons.fa_save)
                            .colorRes(R.color.white)
                            .actionBarSize());
                    item.setTitle("Save");
                    enableViews();

                } else {
                    item.setIcon(new IconDrawable(context, FontAwesomeIcons.fa_edit)
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

    private void fetchAndDisplay() {
        String URL = Endpoints.faculty_profile_data;
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
//                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
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
        disableViews();
    }

    private void disableViews() {
        designation.setEnabled(false);
        department.setEnabled(false);
        contact_no.setEnabled(false);
        address.setEnabled(false);
        alternate_email.setEnabled(false);
    }

    private void initialize() {
        context = getActivity().getApplicationContext();
        designation = (TextInputEditText) view.findViewById(R.id.designation_edit_input);
        department = (TextInputEditText) view.findViewById(R.id.department_edit_text);
        contact_no = (TextInputEditText) view.findViewById(R.id.contact_no_edit_text);
        address = (TextInputEditText) view.findViewById(R.id.address_edit_text);
        alternate_email = (TextInputEditText) view.findViewById(R.id.email_edit_text);
        menu = MainActivity.menu;
        menu.getItem(0).setIcon(new IconDrawable(context, FontAwesomeIcons.fa_edit)
                .colorRes(R.color.white)
                .actionBarSize());
        menu.getItem(0).setTitle("Edit");

        menu.getItem(0).setEnabled(true);


        PROFILE_ID = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("com.hackncs.click.PROFILE_ID", "0");
        TOKEN = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("com.hackncs.click.TOKEN", "0");
    }

    @Override
    public void onClick(View view) {

    }

    private void enableViews() {
        designation.setEnabled(true);
        department.setEnabled(true);
        contact_no.setEnabled(true);
        address.setEnabled(true);
        alternate_email.setEnabled(true);
    }

    private void uploadChanges() {
        String URL = Endpoints.faculty_profile_data;
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
                params.put("designation", designation.getText().toString());
                params.put("department", department.getText().toString());
                params.put("contact_no", contact_no.getText().toString());
                params.put("address", address.getText().toString());
                params.put("alternate_email", alternate_email.getText().toString());
                params.put("display_to_others", String.valueOf(false));
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
