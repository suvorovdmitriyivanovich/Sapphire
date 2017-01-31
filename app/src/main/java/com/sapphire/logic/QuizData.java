package com.sapphire.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuizData {
    private String courseFileId = "";
    private String name = "";
    private String description = "";
    private String duration;
    private boolean isDisabled = false;
    private CourseData course;
    private String quizId = "";
    private Long dateModified = 0l;
    private String courseFileData = "";
    private boolean isFile = false;
    private String parentId = "";
    private boolean quizEnabled = false;
    private boolean coursePassed = false;
    private Long dateCoursePassed = 0l;
    private boolean isCourseStarted = false;
    private Long dateCourseStarted = 0l;
    private String quizScore = "";
    private int quizStatus = 0;
    private Long quizDateCompleted = 0l;
    private String idQuizCompleted = "";
    private String passingScore = "";
    private ArrayList<QuizData> subCourses = new ArrayList<QuizData>();

    public QuizData() {

    }

    public QuizData(JSONObject data) {
        try {
            if (!data.isNull("CourseFileId")) {
                setCourseFileId(data.getString("CourseFileId"));
            }
            if (!data.isNull("Name")) {
                setName(data.getString("Name"));
            }
            if (!data.isNull("Description")) {
                setDescription(data.getString("Description"));
            }
            if (!data.isNull("Duration")) {
                setDuration(data.getString("Duration"));
            }
            if (!data.isNull("IsDisabled")) {
                setIsDisabled(data.getBoolean("IsDisabled"));
            }
            if (!data.isNull("Course")) {
                setCourse(data.getJSONObject("Course"));
            }
            if (!data.isNull("QuizId")) {
                setQuizId(data.getString("QuizId"));
            }
            if (!data.isNull("DateModified")) {
                setDateModified(data.getString("DateModified"));
            }
            if (!data.isNull("CourseFileData")) {
                setCourseFileData(data.getString("CourseFileData"));
            }
            if (!data.isNull("IsFile")) {
                setIsFile(data.getBoolean("IsFile"));
            }
            if (!data.isNull("ParentId")) {
                setParentId(data.getString("ParentId"));
            }
            if (!data.isNull("QuizEnabled")) {
                setQuizEnabled(data.getBoolean("QuizEnabled"));
            }
            if (!data.isNull("CoursePassed")) {
                setCoursePassed(data.getBoolean("CoursePassed"));
            }
            if (!data.isNull("DateCoursePassed")) {
                setDateCoursePassed(data.getString("DateCoursePassed"));
            }
            if (!data.isNull("IsCourseStarted")) {
                setIsCourseStarted(data.getBoolean("IsCourseStarted"));
            }
            if (!data.isNull("DateCourseStarted")) {
                setDateCourseStarted(data.getString("DateCourseStarted"));
            }
            if (!data.isNull("QuizScore")) {
                setQuizScore(data.getString("QuizScore"));
            }
            if (!data.isNull("QuizStatus")) {
                setQuizStatus(data.getInt("QuizStatus"));
            }
            if (!data.isNull("QuizDateCompleted")) {
                setQuizDateCompleted(data.getString("QuizDateCompleted"));
            }
            if (!data.isNull("IdQuizCompleted")) {
                setIdQuizCompleted(data.getString("IdQuizCompleted"));
            }
            if (!data.isNull("PassingScore")) {
                setPassingScore(data.getString("PassingScore"));
            }
            if (!data.isNull("SubCourses")) {
                setSubCourses(data.getJSONArray("SubCourses"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCourseFileId() {
        return courseFileId;
    }

    public void setCourseFileId(String courseFileId) {
        this.courseFileId = courseFileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public void setCourse(JSONObject course) {
        this.course = new CourseData(course);
    }

    public CourseData getCourse() {
        return course;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuizId() {
        return quizId;
    }

    public Long getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        Long dateModifiedLong = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newdate = format.parse(dateModified);
            dateModifiedLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dateModified = dateModifiedLong;
    }

    public void setCourseFileData(String courseFileData) {
        this.courseFileData = courseFileData;
    }

    public String getCourseFileData() {
        return courseFileData;
    }

    public boolean getIsFile() {
        return isFile;
    }

    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean getQuizEnabled() {
        return quizEnabled;
    }

    public void setQuizEnabled(boolean quizEnabled) {
        this.quizEnabled = quizEnabled;
    }

    public boolean getCoursePassed() {
        return coursePassed;
    }

    public void setCoursePassed(boolean coursePassed) {
        this.coursePassed = coursePassed;
    }

    public Long getDateCoursePassed() {
        return dateCoursePassed;
    }

    public void setDateCoursePassed(String dateCoursePassed) {
        Long dateCoursePassedLong = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newdate = format.parse(dateCoursePassed);
            dateCoursePassedLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dateCoursePassed = dateCoursePassedLong;
    }

    public boolean getIsCourseStarted() {
        return isCourseStarted;
    }

    public void setIsCourseStarted(boolean isCourseStarted) {
        this.isCourseStarted = isCourseStarted;
    }

    public Long getDateCourseStarted() {
        return dateCourseStarted;
    }

    public void setDateCourseStarted(String dateCourseStarted) {
        Long dateCourseStartedLong = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newdate = format.parse(dateCourseStarted);
            dateCourseStartedLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dateCourseStarted = dateCourseStartedLong;
    }

    public String getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(String quizScore) {
        this.quizScore = quizScore;
    }

    public int getQuizStatus() {
        return quizStatus;
    }

    public void setQuizStatus(int quizStatus) {
        this.quizStatus = quizStatus;
    }

    public Long getQuizDateCompleted() {
        return quizDateCompleted;
    }

    public String getQuizDateCompletedString() {
        String quizDateCompletedString = "";
        if (quizDateCompleted != 0l) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date();
            date.setTime(quizDateCompleted);
            quizDateCompletedString = format.format(date);
        }
        return quizDateCompletedString;
    }

    public void setQuizDateCompleted(String quizDateCompleted) {
        Long quizDateCompletedLong = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newdate = format.parse(quizDateCompleted);
            quizDateCompletedLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.quizDateCompleted = quizDateCompletedLong;
    }

    public String getIdQuizCompleted() {
        return idQuizCompleted;
    }

    public void setIdQuizCompleted(String idQuizCompleted) {
        this.idQuizCompleted = idQuizCompleted;
    }

    public String getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(String passingScore) {
        this.passingScore = passingScore;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<QuizData> getSubCourses() {
        return subCourses;
    }

    public void setSubCourses(JSONArray subCourses) {
        ArrayList<QuizData> coursesDatas = new ArrayList<QuizData>();
        for (int y=0; y < subCourses.length(); y++) {
            try {
                coursesDatas.add(new QuizData(subCourses.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.subCourses = coursesDatas;
    }
}