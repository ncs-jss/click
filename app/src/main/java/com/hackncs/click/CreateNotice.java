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

import static android.R.attr.button;
import static android.R.attr.x;


public class CreateNotice extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener  {

    private ArrayList mSelectedItem;
    private Boolean cb1_value, cb2_value, cb3_value, cb4_value, cb5_value;
    private Button create_bt, ok_button, cancel_button;
    private CheckBox b1, b2, b3, b4, b5;
    private Context context;
    private Dialog dialog;
    private EditText notice_title;
    private String cDescription, cTitle, cSpinselection;
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
        //  cSpinselection = spinner1.getSelectedItem().toString();
        //Log.d("---------->", cSpinselection);
//        spinner1.setOnItemSelectedListener(new AdapterView.OnItemClickListener() {
//           @Override
//            public void onItemSelected(AdapterView)
//           {
//               cSpinselection =
//           }
//        });


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


                //Networking
                URL = "http://210.212.85.155/api/notices/notice_create/";
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                StringRequest postrequest = new StringRequest(Request.Method.POST, URL,
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
                        params.put("category", cSpinselection);
                        params.put("visible_for_student", cb1_value.toString());
                        params.put("visible_for_hod", cb2_value.toString());
                        params.put("visible_for_faculty", cb3_value.toString());
                        params.put("visible_for_managemant", cb4_value.toString());
                        params.put("visible_for_others", cb5_value.toString());
                        params.put("course_branch_year", "");
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


                queue.add(postrequest);


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
        else if (v.getId() == R.id.cb1) {


        }
        else if (v.getId() == R.id.b_ok) {
            cb1_value = b1.isChecked();
            cb2_value = b2.isChecked();
            cb3_value = b3.isChecked();
            cb4_value = b4.isChecked();
            cb5_value = b5.isChecked();
            Log.d("---->", String.valueOf(cb1_value) + String.valueOf(cb2_value) + String.valueOf(cb3_value) +
                    String.valueOf(cb4_value) + String.valueOf(cb5_value));
            dialog.dismiss();
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    public String getTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        Log.d("Current time =>",formattedDate);
        return formattedDate;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView tvSpinselection  = (TextView) view;
        cSpinselection = tvSpinselection.getText().toString();
        Log.d("----->", cSpinselection);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}