package com.hackncs.click;

public class Endpoints {


    Endpoints endObj = new Endpoints();
    public static final String host_url="http://54.157.21.6:8085/";
    public static final String ig_geturl = host_url+"api/v1/notices/";
    public static final String notice_by_pk = host_url+"api/notices/notice_by_pk/";
    public static final String delete_starred_notice = host_url+"api/notices/delete_starred_notice/";
    public static final String add_starred_notice = host_url+"api/notices/add_starred_notice/";
    public static final String media = host_url+"media/";
    public static final String notice_list = host_url+"api/notices/notice_list/";
    public static final String faculty_profile_data = host_url+"api/profiles/faculty_profile_data/";
    public static final String student_profile_data = host_url+"api/profiles/student_profile_data/";
    public static final String get_starred_notice_list = host_url+"api/notices/get_starred_notice_list/";
    public static final String login = host_url+"api/profiles/login/";
    public static final String create_notice=host_url+"api/notices/";



}
