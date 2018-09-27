package com.hackncs.click;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OfflineDatabaseHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "InfoConnectDB";
    private static final String TABLE_LABEL_NOTICES = "notices";
    private Context context;
    //Columns
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_POSTED_BY = "postedby";
    private static final String KEY_ATTACHMENT_URL = "attachmenturl";
    private static final String KEY_NEXT_URL = "nexturl";
    private static final String KEY_NOTICE_ID = "noticeid";
    private static final String KEY_ATTACHMENT = "attachment";

    public OfflineDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_LABEL_NOTICES+"(" +
                KEY_NOTICE_ID +         " INT PRIMARY KEY, " +
                KEY_TITLE +             " VARCHAR, " +
                KEY_DESCRIPTION +       " TEXT, " +
                KEY_DATE +              " DATE, " +
                KEY_POSTED_BY +         " VARCHAR, " +
                KEY_ATTACHMENT +        " BIT, " +
                KEY_ATTACHMENT_URL +    " VARCHAR, " +
                KEY_NEXT_URL +        " VARCHAR)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_LABEL_NOTICES);
        onCreate(sqLiteDatabase);
    }

    public void flush() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_LABEL_NOTICES);
    }

    public boolean insertNotice(Notice notice) {
        /*
            * When a notice is starred, this function can be called to insert that
            * notice (instance of Notice) into device's offline database.
         */

        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NOTICE_ID, notice.mId);
            values.put(KEY_TITLE, notice.mTitle);
            values.put(KEY_DESCRIPTION, notice.mNotice_description);
            values.put(KEY_DATE, notice.mDate);
            values.put(KEY_POSTED_BY, notice.mPosted_by);
            values.put(KEY_ATTACHMENT, notice.mAttachment);
            values.put(KEY_ATTACHMENT_URL, notice.mAttachment_link);
            values.put(KEY_NEXT_URL, notice.mNextUrl);
            db.insert(TABLE_LABEL_NOTICES, null, values);
            values.clear();
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean deleteNotice(Notice notice) {

        /*
            * A notice which is unstarred by the user should be sent to this function
            * (instance of Notice) to remove it from device's offline database.
         */

        try {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_LABEL_NOTICES, KEY_NOTICE_ID + " = ?", new String[]{notice.mId});
        } catch(SQLiteException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public List<Notice> getStarredNotices() {

        /*
            * When a list of starred notices (objects) is required.
         */

        List<Notice> noticeList = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_LABEL_NOTICES, null);
            if (cursor.moveToFirst()){
                do {
                    Notice notice = new Notice();
                    notice.mId = cursor.getString(0);
                    notice.mTitle = cursor.getString(1);
                    notice.mNotice_description = cursor.getString(2);
                    notice.mDate = cursor.getString(3);
                    notice.mPosted_by = cursor.getString(4);
                    notice.mAttachment = cursor.getString(5).equals("1");
                    notice.mAttachment_link = cursor.getString(6);
                    notice.mNextUrl = cursor.getString(7);
                    noticeList.add(notice);
                }while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch(SQLiteException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return noticeList;
    }

    public List<String> getStarredNoticesIds() {

        /*
            * When a list of ids of starred notices is required.
         */

        List<String> idList = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_LABEL_NOTICES, null);
            if (cursor.moveToFirst()){
                do {
                    idList.add(cursor.getString(0));
                }while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch(SQLiteException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return idList;
    }
}
