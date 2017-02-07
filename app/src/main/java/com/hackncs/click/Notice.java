package com.hackncs.click;

<<<<<<< HEAD
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class Notice implements Serializable{

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

    public static Notice getNoticeObject(JSONObject json_data) throws JSONException {
        Notice notice = new Notice();
        notice.mId = json_data.getString("notice_id");
        notice.mPosted_by = json_data.getString("faculty");
        notice.mTitle = json_data.getString("title");
        notice.mNotice_description = json_data.getString("description");
        notice.mAttachment_link = json_data.getString("file_attached");
        notice.mDate = json_data.getString("created");
        notice.numDate = (notice.mDate).substring(8,10);
        notice.month = (notice.mDate).substring(5,7);
        if(notice.month.equals("01"))
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
        else
        if (notice.month.equals("12"))
            notice.month = "Dec";

        if(notice.mAttachment_link != "null")
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

=======

class Notice {

    private String title;
    private String description;
    private String date;
    private String postedBy;
    private String attachmentUrl;
    private String noticeUrl;
    private String noticeId;
    private boolean attachment;

    public Notice() {
        /*
         Default values of a new notice (which is to be added) can be set here.
         e.g.   postedBy = name of teacher who's logged in, date = current date
                noticeUrl, noticeId etc. (which are not to be set by the user)
         */
    }

    public Notice getBlankObject() {
        Notice obj = new Notice();
        obj.setTitle(null);
        obj.setDescription(null);
        obj.setDate(null);
        obj.setPostedBy(null);
        obj.setAttachmentUrl(null);
        obj.setNoticeUrl(null);
        obj.setNoticeId(null);
        obj.setAttachmentStatus(false);
        return obj;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public void setAttachmentStatus(boolean attachment) {
        this.attachment = attachment;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public boolean hasAttachment() {
        return attachment;
    }
}
>>>>>>> origin/offlinenotices
