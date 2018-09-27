package com.hackncs.click;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


public class FragmentFillerNotices extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filler_notices, container, false);
    }

    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);

    }
}
