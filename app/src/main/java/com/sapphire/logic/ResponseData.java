package com.sapphire.logic;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResponseData {
    private String requestId = "";
    private String url = "";
    private Long timestampRequest = 0l;
    private String ipAddress = "";
    private String userAgentId = "";
    private String httpVerb = "";
    private JSONArray errorMessages;
    private String responseId = "";
    private boolean success = false;
    private Long timestampResponse = 0l;
    private String httpStatusCode = "";
    private String httpStatusMessage = "";
    private String errorMessage;

    public ResponseData() {

    }

    public ResponseData(String data) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(data);
            JSONObject jsonObjectRequest = jsonObject.getJSONObject("request");
            if (!jsonObjectRequest.isNull("requestId")) {
                setRequestId(jsonObjectRequest.getString("requestId"));
            }
            if (!jsonObjectRequest.isNull("url")) {
                setUrl(jsonObjectRequest.getString("url"));
            }
            if (!jsonObjectRequest.isNull("timestamp")) {
                setTimestampRequest(jsonObjectRequest.getString("timestamp"));
            }
            if (!jsonObjectRequest.isNull("ipAddress")) {
                setIpAddress(jsonObjectRequest.getString("ipAddress"));
            }
            if (!jsonObjectRequest.isNull("userAgentId")) {
                setUserAgentId(jsonObjectRequest.getString("userAgentId"));
            }
            if (!jsonObjectRequest.isNull("httpVerb")) {
                setHttpVerb(jsonObjectRequest.getString("httpVerb"));
            }

            JSONObject jsonObjectResponse = jsonObject.getJSONObject("response");
            if (!jsonObjectResponse.isNull("errorMessages")) {
                setErrorMessages(jsonObjectResponse.getJSONArray("errorMessages"));
            }
            if (!jsonObjectResponse.isNull("responseId")) {
                setResponseId(jsonObjectResponse.getString("responseId"));
            }
            if (!jsonObjectResponse.isNull("success")) {
                setSuccess(jsonObjectResponse.getBoolean("success"));
            }
            if (!jsonObjectResponse.isNull("timestamp")) {
                setTimestampResponse(jsonObjectResponse.getString("timestamp"));
            }
            if (!jsonObjectResponse.isNull("httpStatusCode")) {
                setHttpStatusCode(jsonObjectResponse.getString("httpStatusCode"));
            }
            if (!jsonObjectResponse.isNull("httpStatusMessage")) {
                setHttpStatusMessage(jsonObjectResponse.getString("httpStatusMessage"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTimestampRequest() {
        return timestampRequest;
    }

    public void setTimestampRequest(String timestampRequest) {
        Long timestampRequestLong = 0l;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date newdate = format.parse(timestampRequest);
            timestampRequestLong = newdate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.timestampRequest = timestampRequestLong;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgentId() {
        return userAgentId;
    }

    public void setUserAgentId(String userAgentId) {
        this.userAgentId = userAgentId;
    }

    public String getHttpVerb() {
        return httpVerb;
    }

    public void setHttpVerb(String httpVerb) {
        this.httpVerb = httpVerb;
    }

    public JSONArray getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(JSONArray errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getTimestampResponse() {
        return timestampResponse;
    }

    public void setTimestampResponse(String timestampResponse) {
        this.timestampResponse = 0l;
    }

    public String getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(String httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getHttpStatusMessage() {
        return httpStatusMessage;
    }

    public void setHttpStatusMessage(String httpStatusMessage) {
        this.httpStatusMessage = httpStatusMessage;
    }
}