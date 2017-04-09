package com.hackncs.click;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentNotice extends Fragment {
    public FragmentNotice fragmentNotice;
    private RecyclerView mRVNoticeList;
    private NoticeAdapter mAdapter;
    private String TOKEN = "token ";
    static String URL = null;
    private Context context;
    private View view;
    private String mNext = null;
    private EndlessRecyclerViewScrollListener scrollListener;
    private List<Notice> data = new ArrayList<>();
    private String USER_NAME;
    public static String NOTICE_CATEGORY = "none";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notice_layout, container, false);
        context = getActivity().getApplicationContext();
        mRVNoticeList = (RecyclerView) view.findViewById(R.id.noticeList);
        fragmentNotice = new FragmentNotice();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        TOKEN = sp.getString("com.hackncs.click.TOKEN", "");
        USER_NAME = sp.getString("com.hackncs.click.USERNAME", "");
        Log.d("---->", TOKEN);
        Log.d("---->", USER_NAME);


        mAdapter = new NoticeAdapter(context, data);
        mRVNoticeList.setAdapter(mAdapter);
        loadNextDataFromApi(1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRVNoticeList.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };
        mRVNoticeList.addOnScrollListener(scrollListener);
        return view;
    }

    public void loadNextDataFromApi(int offset) {

        URL = "http://210.212.85.155/api/notices/notice_list/";
        if (offset != 1)
            URL = URL + "?page=" + offset;
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            Notice.getNoticeArray(obj.getString("results"), data);

                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "token " + TOKEN);
                params.put("username", USER_NAME);
                params.put("category", NOTICE_CATEGORY);
                return params;
            }


        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(postRequest);
    }


    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);

    }

}