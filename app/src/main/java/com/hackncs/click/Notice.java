package com.hackncs.click;


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
