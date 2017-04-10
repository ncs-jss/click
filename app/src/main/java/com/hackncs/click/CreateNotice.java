package com.hackncs.click;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.nashapp.androidsummernote.Summernote;

import static com.hackncs.click.R.id.container;
import static com.hackncs.click.R.id.spinner_course;


public class CreateNotice extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ArrayList mSelectedItem;
    private Boolean cb1_value, cb2_value, cb3_value, cb4_value, cb5_value;
    private Button create_bt, ok_button, cancel_button, oknxt_button, cancelnxt_button;
    private CheckBox b1, b2, b3, b4, b5;
    private Context context;
    private Dialog dialog, dialognext;
    private EditText notice_title;
    private RequestQueue queue;
    private StringRequest postrequest;
    private String cDescription, cTitle, cSpinselection1, cSpinselection2, cSpinselection3, cSpinselection4, cSpinselection5, coursebranchyear;
    private String TOKEN = "token ";
    static String URL = null;
    private String FIRST_NAME, USER_NAME, GROUP, USER_ID, FACULTY_ID;
    private View view;
    static Summernote summernote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_createnotice, container, false);

        context = getActivity().getApplicationContext();
        summernote = (Summernote) view.findViewById(R.id.summernote);

        Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(context,
                R.array.uplaod_category, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);

        create_bt = (Button) view.findViewById(R.id.create_button);
        create_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_uploadfor();
                Log.d("------>", summernote.getText());

                notice_title = (EditText) getActivity().findViewById(R.id.etNoticeTitle);
                cTitle = notice_title.getText().toString();
                cDescription = summernote.getText();


                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                TOKEN = sp.getString("com.hackncs.click.TOKEN", "");
                USER_NAME = sp.getString("com.hackncs.click.USERNAME", "");
                FIRST_NAME = sp.getString("com.hackncs.click.FIRST_NAME", "");
                USER_ID = sp.getString("com.hackncs.click.USER_ID", "");
                FACULTY_ID = sp.getString("com.hackncs.click.FACULTY_ID", "");

                coursebranchyear = cSpinselection2 + "-" + cSpinselection3 + "-" + cSpinselection4 + "-" + cSpinselection5;

                //Networking
                URL = "http://210.212.85.155/api/notices/notice_create/";
                queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                postrequest = new StringRequest(Request.Method.POST, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("faculty", FACULTY_ID);
                        params.put("description", cDescription);
                        params.put("file_attached", "");
                        params.put("category", cSpinselection1);
                        params.put("visible_for_student", cb1_value.toString());
                        params.put("visible_for_hod", cb2_value.toString());
                        params.put("visible_for_faculty", cb3_value.toString());
                        params.put("visible_for_managemant", cb4_value.toString());
                        params.put("visible_for_others", cb5_value.toString());
                        params.put("course_branch_year", coursebranchyear);
                        params.put("created", getTime());
                        params.put("modified", getTime());
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Authorization", "token " + TOKEN);
                        params.put("username", USER_NAME);
                        return super.getHeaders();
                    }
                };


            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        summernote.onActivityResult(requestCode, resultCode, intent);
    }


    public void dialog_uploadfor() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialogue_layout);
        dialog.setTitle("Upload for");
        b1 = (CheckBox) dialog.findViewById(R.id.cb1);
        b2 = (CheckBox) dialog.findViewById(R.id.cb2);
        b3 = (CheckBox) dialog.findViewById(R.id.cb3);
        b4 = (CheckBox) dialog.findViewById(R.id.cb4);
        b5 = (CheckBox) dialog.findViewById(R.id.cb5);
        ok_button = (Button) dialog.findViewById(R.id.b_ok);
        cancel_button = (Button) dialog.findViewById(R.id.b_cancel);
        dialog.show();
        ok_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);
        b1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.b_cancel)
            dialog.dismiss();
        else if (v.getId() == R.id.b_ok) {
            cb1_value = b1.isChecked();
            cb2_value = b2.isChecked();
            cb3_value = b3.isChecked();
            cb4_value = b4.isChecked();
            cb5_value = b5.isChecked();
            Log.d("---->", String.valueOf(cb1_value) + String.valueOf(cb2_value) + String.valueOf(cb3_value) +
                    String.valueOf(cb4_value) + String.valueOf(cb5_value));
            dialog.dismiss();

            if (cb1_value) {

                dialognext = new Dialog(getActivity());
                dialognext.setContentView(R.layout.upload_for_details);

                Spinner spinner2 = (Spinner) dialognext.findViewById(R.id.spinner_course);
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context,
                        R.array.select_course, android.R.layout.simple_spinner_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);
                spinner2.setOnItemSelectedListener(this);

                Spinner spinner3 = (Spinner) dialognext.findViewById(R.id.spinner_branch);
                ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(context,
                        R.array.select_branch, android.R.layout.simple_spinner_item);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner3.setAdapter(adapter3);
                spinner3.setOnItemSelectedListener(this);

                Spinner spinner4 = (Spinner) dialognext.findViewById(R.id.spinner_year);
                ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(context,
                        R.array.select_year, android.R.layout.simple_spinner_item);
                adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner4.setAdapter(adapter4);
                spinner4.setOnItemSelectedListener(this);

                Spinner spinner5 = (Spinner) dialognext.findViewById(R.id.spinner_section);
                ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(context,
                        R.array.select_section, android.R.layout.simple_spinner_item);
                adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner5.setAdapter(adapter5);
                spinner5.setOnItemSelectedListener(this);

                oknxt_button = (Button) dialognext.findViewById(R.id.bnxt_ok);
                cancelnxt_button = (Button) dialognext.findViewById(R.id.bnxt_cancel);
                dialognext.show();
                oknxt_button.setOnClickListener(this);
                cancelnxt_button.setOnClickListener(this);
            }
        }
        if (v.getId() == R.id.bnxt_cancel)
            dialognext.dismiss();
        if (v.getId() == R.id.bnxt_ok) {
            queue.add(postrequest);
            dialognext.dismiss();
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    public String getTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        Log.d("Current time =>", formattedDate);
        return formattedDate;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView tvSpinselection = (TextView) view;
        switch (parent.getId()) {
            case R.id.spinner_category:
                cSpinselection1 = tvSpinselection.getText().toString();
                Log.d("----->", cSpinselection1);
                break;
            case R.id.spinner_course:
                cSpinselection2 = tvSpinselection.getText().toString();
                Log.d("----->", cSpinselection2);
                break;
            case R.id.spinner_branch:
                cSpinselection3 = tvSpinselection.getText().toString();
                Log.d("----->", cSpinselection3);
                break;
            case R.id.spinner_year:
                cSpinselection4 = tvSpinselection.getText().toString();
                Log.d("----->", cSpinselection4);
                break;
            case R.id.spinner_section:
                cSpinselection5 = tvSpinselection.getText().toString();
                Log.d("----->", cSpinselection5);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}