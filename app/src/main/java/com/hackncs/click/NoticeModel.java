package com.hackncs.click;

import java.io.File;

public class NoticeModel {

    String faculty;
    String title;
    String description;
    File file_attached;
    String category;
    String visible_for_students;
    String visible_for_hod;
    String visible_for_faculty;
    String visible_for_management;
    String visible_for_others;
    String course_branch_year;
    String created;
    String modified;

    public NoticeModel(String faculty,
            String title,
            String description,
            File file_attached,
            String category,
            String visible_for_students,
            String visible_for_hod,
            String visible_for_faculty,
            String visible_for_management,
            String visible_for_others,
            String course_branch_year,
            String created,
            String modified)
    {
        this.category=category;
        this.faculty=faculty;
        this.title=title;
        this.file_attached=file_attached;
        this.description=description;
        this.visible_for_faculty=visible_for_faculty;
        this.visible_for_hod= visible_for_hod;
        this.visible_for_management=visible_for_management;
        this.visible_for_students=visible_for_students;
        this.visible_for_others=visible_for_others;
        this.course_branch_year=course_branch_year;
        this.created=created;
        this.modified=modified;

    }

    public NoticeModel()
    {

    }

    public File getFile_attached() {
        return file_attached;
    }

    public String getCategory() {
        return category;
    }

    public String getCourse_branch_year() {
        return course_branch_year;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated() {
        return created;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getTitle() {
        return title;
    }

    public String getVisible_for_faculty() {
        return visible_for_faculty;
    }

    public String getModified() {
        return modified;
    }

    public String getVisible_for_hod() {
        return visible_for_hod;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVisible_for_management() {
        return visible_for_management;
    }

    public String getVisible_for_others() {
        return visible_for_others;
    }

    public String getVisible_for_students() {
        return visible_for_students;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCourse_branch_year(String course_branch_year) {
        this.course_branch_year = course_branch_year;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setFile_attached(File file_attached) {
        this.file_attached = file_attached;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public void setVisible_for_faculty(String visible_for_faculty) {
        this.visible_for_faculty = visible_for_faculty;
    }

    public void setVisible_for_hod(String visible_for_hod) {
        this.visible_for_hod = visible_for_hod;
    }

    public void setVisible_for_management(String visible_for_management) {
        this.visible_for_management = visible_for_management;
    }

    public void setVisible_for_others(String visible_for_others) {
        this.visible_for_others = visible_for_others;
    }

    public void setVisible_for_students(String visible_for_students) {
        this.visible_for_students = visible_for_students;
    }
}
