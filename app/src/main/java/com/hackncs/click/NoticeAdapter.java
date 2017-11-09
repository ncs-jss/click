package com.hackncs.click;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;


public class NoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Notice> data = Collections.emptyList();
    Notice current;
    int currentPos = 0;

    public NoticeAdapter(Context context, List<Notice> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    //private final View.OnClickListener mOnClickListener = new MyOnClickListener();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.notice_row_layout, parent, false);
        MyHolder holder = new MyHolder(view);
        //view.setOnClickListener(mOnClickListener);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        final Notice current = data.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(view.getContext(),"Recycle Click" + position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,DescriptionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Notice", current);
                context.startActivity(intent);
            }
        }
        );
        myHolder.textmTitle.setText(current.mTitle);
        myHolder.textmPostedby.setText(current.mPosted_by);
        myHolder.textmDate.setText(current.numDate);
        myHolder.textmMonth.setText(current.month);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView textmTitle;
        TextView textmMonth;
        TextView textmDate;
        TextView textmPostedby;

        //ImageView ivstarred;
        //ImageView ivattachment;

        public MyHolder(View itemView) {
            super(itemView);
            textmTitle = (TextView) itemView.findViewById(R.id.textmTitle);
            //ivstarred = (ImageView) itemView.findViewById(R.id.ivstarred);
            //ivattachment = (ImageView) itemView.findViewById(R.id.ivattachment);
            textmMonth = (TextView) itemView.findViewById(R.id.textmMonth);
            textmDate = (TextView) itemView.findViewById(R.id.textmDate);
            textmPostedby = (TextView) itemView.findViewById(R.id.textmPostedby);
        }

    }
}
