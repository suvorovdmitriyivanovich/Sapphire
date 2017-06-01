package com.dealerpilothr.models;

import org.json.JSONException;
import org.json.JSONObject;

public class QuizScoreData {
    private String score = "";
    private boolean isPassed = false;
    private String message = "";

    public QuizScoreData() {

    }

    public QuizScoreData(JSONObject data) {
        try {
            if (!data.isNull("Score")) {
                setScore(data.getString("Score"));
            }
            if (!data.isNull("IsPassed")) {
                setIsPassed(data.getBoolean("IsPassed"));
            }
            if (!data.isNull("Message")) {
                setMessage(data.getString("Message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public boolean getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(boolean isPassed) {
        this.isPassed = isPassed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}