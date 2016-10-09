package com.hackncs.click;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.R.attr.button;

public class NoticesAdapter extends RecyclerView.Adapter<NoticesAdapter.ViewHolder> {

    public NoticesAdapter() {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_row, parent, false);

        ViewHolder viewHolder = new ViewHolder( );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        //Contact contact = mContacts.get(position);

        //TextView textView = viewHolder.nameTextView;
        //textView.setText(contact.getName());

        //Button button = viewHolder.messageButton;


    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }
}