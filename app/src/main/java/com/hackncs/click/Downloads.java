package com.hackncs.click;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Downloads extends Fragment implements AdapterView.OnItemClickListener {

    private View view;

    File initialDirectory;
    ArrayList<File> filesList;
    MyAdapter myAdapter;
    ListView listView;
    Context context;
    protected String[] acceptedFileExtensions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_downloads,container,false);
        context = getActivity().getApplicationContext();
        listView = (ListView) view.findViewById(R.id.lvList);
        initialDirectory = new File(Environment.getExternalStorageDirectory()+"/InfoConnect");
        filesList = new ArrayList<>();
        acceptedFileExtensions = new String[] {};
        refreshFilesList();
        return view;
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);
        listView = (ListView) findViewById(R.id.lvList);
        initialDirectory = new File(Environment.getExternalStorageDirectory()+"/InfoConnect");
        filesList = new ArrayList<>();
        acceptedFileExtensions = new String[] {};
        refreshFilesList();
    }

    @Override
    protected void onResume() {
        refreshFilesList();
        super.onResume();
    }
*/
    private void refreshFilesList() {
        filesList.clear();
        ExtensionFilenameFilter filter = new ExtensionFilenameFilter(acceptedFileExtensions);
        File[] files = initialDirectory.listFiles(filter);
        if(files != null && files.length > 0) {
            for(File f : files) {
                filesList.add(f);
            }
            Collections.sort(filesList, new FileComparator());
        }
        else {
            Toast.makeText(context , "No files found!", Toast.LENGTH_SHORT).show();
        }
        myAdapter = new MyAdapter(context , filesList);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        File selectedFile = (File) adapterView.getItemAtPosition(i);
        if (selectedFile.isFile()) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            if (selectedFile.getName().endsWith(".doc") || selectedFile.getName().endsWith(".docx")) {
                intent.setDataAndType(Uri.fromFile(selectedFile), "application/msword");
            }
            else if(selectedFile.getName().endsWith(".pdf")) {
                intent.setDataAndType(Uri.fromFile(selectedFile), "application/pdf");
            }
            else if(selectedFile.getName().endsWith(".ppt") || selectedFile.getName().endsWith(".pptx")) {
                intent.setDataAndType(Uri.fromFile(selectedFile), "application/vnd.ms-powerpoint");
            }
            else if(selectedFile.getName().endsWith(".xls") || selectedFile.getName().endsWith(".xlsx")) {
                intent.setDataAndType(Uri.fromFile(selectedFile), "application/vnd.ms-excel");
            }
            else if(selectedFile.getName().endsWith(".rtf")) {
                intent.setDataAndType(Uri.fromFile(selectedFile), "application/rtf");
            }
            else if(selectedFile.getName().endsWith(".jpg") || selectedFile.getName().endsWith(".jpeg") || selectedFile.getName().endsWith(".png")) {
                intent.setDataAndType(Uri.fromFile(selectedFile), "image/jpeg");
            }
            else if(selectedFile.getName().endsWith(".txt")) {
                intent.setDataAndType(Uri.fromFile(selectedFile), "text/plain");
            }
            else {
                intent.setDataAndType(Uri.fromFile(selectedFile), "*/*");
            }
            startActivity(intent);
        }
    }

    private class MyAdapter extends ArrayAdapter<File> {

        private List<File> mObjects;
        public MyAdapter(Context context, List<File> objects) {
            super(context, R.layout.list_item, android.R.id.text1, objects);
            mObjects = objects;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            if (convertView == null) {
                LayoutInflater  inflater = getActivity().getLayoutInflater();
//                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.list_item, parent, false);
            }
            else
                row = convertView;
            File displayFile = mObjects.get(position);
            ImageView imageView = (ImageView)row.findViewById(R.id.file_picker_image);
            TextView textView = (TextView)row.findViewById(R.id.file_picker_text);
            textView.setSingleLine(true);
            textView.setText(displayFile.getName());
            if (displayFile.isFile()) {
                if (displayFile.getName().endsWith(".doc") || displayFile.getName().endsWith(".docx")) {
                    imageView.setImageResource(R.drawable.doc);
                }
                else if(displayFile.getName().endsWith(".pdf")) {
                    imageView.setImageResource(R.drawable.pdf);
                }
                else if(displayFile.getName().endsWith(".ppt") || displayFile.getName().endsWith(".pptx")) {
                    imageView.setImageResource(R.drawable.ppt);
                }
                else if(displayFile.getName().endsWith(".xls") || displayFile.getName().endsWith(".xlsx")) {
                    imageView.setImageResource(R.drawable.xls);
                }
                else if(displayFile.getName().endsWith(".rtf")) {
                    imageView.setImageResource(R.drawable.rtf);
                }
                else if(displayFile.getName().endsWith(".jpg") || displayFile.getName().endsWith(".jpeg") || displayFile.getName().endsWith(".png")) {
                    imageView.setImageResource(R.drawable.jpg);
                }
                else if(displayFile.getName().endsWith(".txt")) {
                    imageView.setImageResource(R.drawable.txt);
                }
                else {
                    imageView.setImageResource(R.drawable.file);
                }
            }
            return row;
        }
    }

    private class ExtensionFilenameFilter implements FilenameFilter {

        private String[] acceptedExtensions;
        public ExtensionFilenameFilter(String[] extensions) {
            super();
            acceptedExtensions = extensions;
        }

        @Override
        public boolean accept(File file, String s) {
            if(new File(file, s).isDirectory()) {
                return false;
            }
            if(acceptedExtensions != null && acceptedExtensions.length > 0) {
                for(int i = 0; i < acceptedExtensions.length; i++) {
                    if(s.endsWith(acceptedExtensions[i])) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
    }

    private class FileComparator implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            if (f1 == f2)
                return 0;
            if (f1.isDirectory() && f2.isFile())
                return -1;
            if (f1.isFile() && f2.isDirectory())
                return 1;
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
    }
}
