package com.dealerpilothr.models;

import org.json.JSONException;
import org.json.JSONObject;

public class NoteData {
    private String noteId = "";
    private String text = "";

    public NoteData() {

    }

    public NoteData(JSONObject data) {
        try {
            if (!data.isNull("NoteId")) {
                setNoteId(data.getString("NoteId"));
            }
            if (!data.isNull("Text")) {
                setText(data.getString("Text"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}