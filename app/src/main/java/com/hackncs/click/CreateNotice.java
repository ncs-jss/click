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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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


public class CreateNotice extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener , CheckBox.OnCheckedChangeListener {

    private ArrayList mSelectedItem;
    private Boolean cb1_value, cb2_value, cb3_value, cb4_value, cb5_value;
    private Button create_bt, ok_button, cancel_button, oknxt_button, cancelnxt_button;
    private CheckBox b1, b2, b3, b4, b5;
    private Context context;
    //private Dialog view, view;
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
                R.array.uplaod_category, R.layout.spinner_item);
        adapter1.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);

        //Defining Spinners

        defineSpinner();
        ToggleButton toggle = (ToggleButton) view.findViewById(R.id.upload_button);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            LinearLayout uploadLayout=(LinearLayout)view.findViewById(R.id.upload_for);
            ScrollView sv = (ScrollView)view.findViewById(R.id.scrl);
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled

                    uploadLayout.setVisibility(View.VISIBLE);
                    sv.post(new Runnable() {
                        public void run() {
                            sv.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                    //sv.fullScroll(ScrollView.FOCUS_DOWN);
                    //sv.scrollTo(0, );


                } else {
                    // The toggle is disabled
                    uploadLayout.setVisibility(View.GONE);
                    sv.post(new Runnable() {
                        public void run() {
                            sv.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            }
        });

        create_bt = (Button) view.findViewById(R.id.create_button);
        create_bt.setEnabled(false);
        create_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkDetails()) {
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
                    Toast.makeText(context, "CourseBranchYear="+coursebranchyear, Toast.LENGTH_SHORT).show();

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
                                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
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
                    queue.add(postrequest);
                }


            }
        });

        b1 = (CheckBox) view.findViewById(R.id.cb1);
        b2 = (CheckBox) view.findViewById(R.id.cb2);
        b3 = (CheckBox) view.findViewById(R.id.cb3);
        b4 = (CheckBox) view.findViewById(R.id.cb4);
        b5 = (CheckBox) view.findViewById(R.id.cb5);
        b1.setOnCheckedChangeListener(this);
        b2.setOnCheckedChangeListener(this);
        b3.setOnCheckedChangeListener(this);
        b4.setOnCheckedChangeListener(this);
        b5.setOnCheckedChangeListener(this);






        return view;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        summernote.onActivityResult(requestCode, resultCode, intent);
    }


    public boolean checkDetails () {
        /*view = new Dialog(getActivity());
        view.setContentView(R.layout.viewue_layout);
        view.setTitle("Upload for");

        //ok_button = (Button) view.findViewById(R.id.b_ok);
        //ancel_button = (Button) view.findViewById(R.id.b_cancel);
        view.show();
        ok_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);
        b1.setOnClickListener(this);*/



        cb1_value = b1.isChecked();
        cb2_value = b2.isChecked();
        cb3_value = b3.isChecked();
        cb4_value = b4.isChecked();
        cb5_value = b5.isChecked();
        Log.d("---->", String.valueOf(cb1_value) + String.valueOf(cb2_value) + String.valueOf(cb3_value) +
                String.valueOf(cb4_value) + String.valueOf(cb5_value));
       

        if (cb1_value||cb2_value||cb3_value||cb4_value||cb5_value)
        {
            int spinner_category_length=cSpinselection1.length();
            int spinner_course_length=cSpinselection2.length();
            int spinner_branch_length=cSpinselection3.length();
            int spinner_year_length=cSpinselection4.length();
            int spinner_section_length=cSpinselection5.length();
            if(spinner_category_length>0&&spinner_branch_length>0&&spinner_course_length>0&&spinner_year_length>0&&spinner_section_length>0) {

                return true;
            }
            else {
                Toast.makeText(context, "Please select the required Details", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else {
            Toast.makeText(context, "Please check the boxes", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    @Override
    public void onClick(View v) {



    }


    public String getTime() {
        Calendar c = Calendar.getInstance();
        /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());*/
        String formattedDate = String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS",c.getTime());
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
                defineAdapter3(cSpinselection2);
                break;
            case R.id.spinner_branch:
                cSpinselection3 = tvSpinselection.getText().toString();
                Log.d("----->", cSpinselection3);
                defineAdapter4(cSpinselection2);
                break;
            case R.id.spinner_year:
                cSpinselection4 = tvSpinselection.getText().toString();
                Log.d("----->", cSpinselection4);
                defineAdapter5(cSpinselection2,cSpinselection3);
                break;
            case R.id.spinner_section:
                cSpinselection5 = tvSpinselection.getText().toString();
                Log.d("----->", cSpinselection5);
                //create_bt.setEnabled(true);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void defineSpinner()
    {
        Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner_course);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context,
                R.array.select_course, R.layout.spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        /*Spinner spinner4 = (Spinner) view.findViewById(R.id.spinner_year);
        adapter4 = ArrayAdapter.createFromResource(context,
                R.array.select_year, R.layout.spinner_item);
        adapter4.setDropDownViewResource(R.layout.spinner_item);
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(this);
        spinner4.setEnabled(false);

        Spinner spinner5 = (Spinner) view.findViewById(R.id.spinner_section);
        adapter5 = ArrayAdapter.createFromResource(context,
                R.array.select_section, R.layout.spinner_item);
        adapter5.setDropDownViewResource(R.layout.spinner_item);
        spinner5.setAdapter(adapter5);
        spinner5.setOnItemSelectedListener(this);
        spinner5.setEnabled(false);*/
    }
    public void defineAdapter3(String course)
    {
        int resId=R.array.select_branch_Btech;
        if(course.equals("BTech"))
        resId=R.array.select_branch_Btech;
        else if (course.equals("MTech"))
            resId=R.array.select_branch_Mtech;
        else if (course.equals("MCA"))
            resId=R.array.select_branch_MCA;
        else if (course.equals("MBA"))
            resId=R.array.select_branch_MBA;
        else if (course.equals("MTech"))
            resId=R.array.select_branch_Mtech;


        //adapter3.notifyDataSetChanged();
        Spinner spinner3 = (Spinner) view.findViewById(R.id.spinner_branch);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(context,
                resId, R.layout.spinner_item);
        adapter3.setDropDownViewResource(R.layout.spinner_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(this);
        //spinner3.setEnabled(false);

    }

    public void defineAdapter4(String course)
    {
        int resId=R.array.select_year_Btech;
        if(course.equals("BTech"))
            resId=R.array.select_year_Btech;
        else if (course.equals("MTech")||course.equals("MBA"))
            resId=R.array.select_year_Others;
        else if (course.equals("MCA"))
            resId=R.array.select_year_MCA;


        Spinner spinner4 = (Spinner) view.findViewById(R.id.spinner_year);

        //adapter3.notifyDataSetChanged();
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(context,
                resId, R.layout.spinner_item);

        adapter4.setDropDownViewResource(R.layout.spinner_item);
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(this);
        //spinner3.setEnabled(false);

    }

    public void defineAdapter5(String course,String branch)
    {
        int resId=R.array.select_section;
        if(course.equals("BTech")) {
            if(branch.equals("CSE"))
                resId = R.array.select_section_CSE;
            else if (branch.equals("IT"))
                resId = R.array.select_section_IT;
            else if (branch.equals("ME"))
                resId = R.array.select_section_ME;
            else if (branch.equals("ECE"))
                resId = R.array.select_section_ECE;
            else if (branch.equals("CE"))
                resId = R.array.select_section_CE;
            else if (branch.equals("EE"))
                resId = R.array.select_section_EE;
            else if (branch.equals("IC"))
                resId = R.array.select_section_IC;
        }
        else if (course.equals("MTech") || course.equals("MBA")||course.equals("MCA"))
                resId = R.array.select_section_Others;


        Spinner spinner5 = (Spinner) view.findViewById(R.id.spinner_section);
       ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(context,
                resId, R.layout.spinner_item);
        //adapter3.notifyDataSetChanged();


        adapter5.setDropDownViewResource(R.layout.spinner_item);
        spinner5.setAdapter(adapter5);
        spinner5.setOnItemSelectedListener(this);
        //spinner3.setEnabled(false);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
        {
            create_bt.setEnabled(true);
        }
        else if(!(b1.isChecked()||b2.isChecked()||b3.isChecked()||b4.isChecked()||b5.isChecked()))
        {
            create_bt.setEnabled(false);
        }
    }
}