package com.hackncs.click;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class Notice implements Serializable {

    public String mTitle;
    public String mNotice_description;
    public boolean mAttachment;
    public String mDate;
    public String mPosted_by;
    public String mAttachment_link;
    public String mId;
    public int mCount;
    public String mNextUrl;
    public String numDate;
    public String month;
    public String date;

    public static Notice getNoticeObject(JSONObject json_data) throws JSONException {
        Notice notice = new Notice();
        notice.mId = json_data.getString("notice_id");
        notice.mPosted_by = json_data.getString("faculty");
        notice.mTitle = json_data.getString("title");
        notice.mNotice_description = json_data.getString("description");
        notice.mAttachment_link = json_data.getString("file_attached");
        notice.mDate = json_data.getString("created");
        notice.numDate = (notice.mDate).substring(8, 10);
        notice.month = (notice.mDate).substring(5, 7);
        if (notice.month.equals("01"))
            notice.month = "Jan";
        else if (notice.month.equals("02"))
            notice.month = "Feb";
        else if (notice.month.equals("03"))
            notice.month = "Mar";
        else if (notice.month.equals("04"))
            notice.month = "Apr";
        else if (notice.month.equals("05"))
            notice.month = "May";
        else if (notice.month.equals("06"))
            notice.month = "Jun";
        else if (notice.month.equals("07"))
            notice.month = "Jul";
        else if (notice.month.equals("08"))
            notice.month = "Aug";
        else if (notice.month.equals("09"))
            notice.month = "Sep";
        else if (notice.month.equals("10"))
            notice.month = "Oct";
        else if (notice.month.equals("11"))
            notice.month = "Nov";
        else if (notice.month.equals("12"))
            notice.month = "Dec";


        notice.date = notice.numDate + "-" + notice.month + "-" + notice.mDate.substring(0, 4) + " " + notice.mDate.substring(11, 16);


        if (notice.mAttachment_link != "null")
            notice.mAttachment = true;

        return notice;
    }

    public static void getNoticeArray(String jsonArray, List<Notice> data) throws JSONException {
        JSONArray jArray = new JSONArray(jsonArray);

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject json_data = jArray.getJSONObject(i);
            Notice notice = getNoticeObject(json_data);
            data.add(notice);
        }
    }


}

