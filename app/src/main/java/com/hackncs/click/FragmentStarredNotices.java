package com.hackncs.click;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FragmentStarredNotices extends Fragment {

    View view;
    Context context;
    ListView listView;
    List<Notice> notices = new ArrayList<>();
    MyAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_starred_notices,container,false);
        context = getActivity().getApplicationContext();
        listView = (ListView) view.findViewById(R.id.lvList2);
        fetchNotices();
        return view;
    }

    private void fetchNotices() {
        notices.clear();
        notices = new OfflineDatabaseHandler(context).getStarredNotices();
        myAdapter = new MyAdapter(context, notices);
        listView.setAdapter(myAdapter);
    }

    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);

    }

    private class MyAdapter extends ArrayAdapter<Notice> {

        List<Notice> mObjects;
        Notice obj;

        public MyAdapter(Context context, List<Notice> objects) {
            super(context, R.layout.notice_row_layout, android.R.id.text1, objects);
            mObjects = objects;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            if (convertView == null) {
                LayoutInflater  inflater = getActivity().getLayoutInflater();
                row = inflater.inflate(R.layout.notice_row_layout, parent, false);
            }
            else
                row = convertView;
            obj = mObjects.get(position);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DescriptionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Notice", obj);
                    context.startActivity(intent);
                }
            });
            TextView title = (TextView)row.findViewById(R.id.textmTitle);
            TextView postedBy = (TextView)row.findViewById(R.id.textmPostedby);
            TextView month = (TextView)row.findViewById(R.id.textmMonth);
            TextView date = (TextView)row.findViewById(R.id.textmDate);
            title.setText(obj.mTitle);
            postedBy.setText(obj.mPosted_by);
            obj.numDate = (obj.mDate).substring(8,10);
            obj.month = (obj.mDate).substring(5,7);
            if(obj.month.equals("01"))
                obj.month = "Jan";
            else if (obj.month.equals("02"))
                obj.month = "Feb";
            else if (obj.month.equals("03"))
                obj.month = "Mar";
            else if (obj.month.equals("04"))
                obj.month = "Apr";
            else if (obj.month.equals("05"))
                obj.month = "May";
            else if (obj.month.equals("06"))
                obj.month = "Jun";
            else if (obj.month.equals("07"))
                obj.month = "Jul";
            else if (obj.month.equals("08"))
                obj.month = "Aug";
            else if (obj.month.equals("09"))
                obj.month = "Sep";
            else if (obj.month.equals("10"))
                obj.month = "Oct";
            else if (obj.month.equals("11"))
                obj.month = "Nov";
            else if (obj.month.equals("12"))
                obj.month = "Dec";
            month.setText(obj.month);
            date.setText(obj.numDate);
            return row;
        }
    }
}
