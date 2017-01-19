package com.sapphire.logic;

import org.json.JSONObject;

public class ResponseData {
    private String requestId = "";
    private String url = "";
    private Long timestamp = 0l;
    private String ipAddress = "";
    private String userAgentId = "";
    private String httpVerb = "";

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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = 0l;
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
}