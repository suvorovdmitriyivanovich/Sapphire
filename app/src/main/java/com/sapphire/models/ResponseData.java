package com.sapphire.models;

import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.utils.DateOperations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ResponseData {
    private String requestId = "";
    private String url = "";
    private Long timestampRequest = 0l;
    private String ipAddress = "";
    private String userAgentId = "";
    private String httpVerb = "";
    private ArrayList<ErrorMessageData> errorMessages = new ArrayList<ErrorMessageData>();
    private String responseId = "";
    private boolean success = false;
    private Long timestampResponse = 0l;
    private String httpStatusCode = "";
    private String httpStatusMessage = "";
    private JSONArray data = new JSONArray();
    private int dataCount = 0;
    private int totalDataCount = 0;
    private boolean hasData = false;

    public ResponseData() {

    }

    public ResponseData(String data) {
        JSONObject jsonObject;
        if (data.equals("400")) {
            setSuccess(true);
            setData(new JSONArray());
            setDataCount(0);
            return;
        }
        try {
            jsonObject = new JSONObject(data);
            if (!jsonObject.isNull("request")) {
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
            }
            if (!jsonObject.isNull("response")) {
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
                if (!jsonObjectResponse.isNull("data")) {
                    setData(jsonObjectResponse.getJSONArray("data"));
                }
                if (!jsonObjectResponse.isNull("dataCount")) {
                    setDataCount(jsonObjectResponse.getInt("dataCount"));
                }
                if (!jsonObjectResponse.isNull("totalDataCount")) {
                    setTotalDataCount(jsonObjectResponse.getInt("totalDataCount"));
                }
                if (!jsonObjectResponse.isNull("hasData")) {
                    setHasData(jsonObjectResponse.getBoolean("hasData"));
                }
            } else if (!jsonObject.isNull("Message")) {
                setHttpStatusMessage(jsonObject.getString("Message"));
            } else {
                setHttpStatusMessage(Sapphire.getInstance().getResources().getString(R.string.unknown_error));
            }
        } catch (Exception e) {
            e.printStackTrace();
            setHttpStatusMessage(data);
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
        this.timestampRequest = DateOperations.getDate(timestampRequest);
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

    public ArrayList<ErrorMessageData> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(JSONArray errorMessages) {
        ArrayList<ErrorMessageData> errorMessageDatas = new ArrayList<ErrorMessageData>();
        for (int y=0; y < errorMessages.length(); y++) {
            try {
                errorMessageDatas.add(new ErrorMessageData(errorMessages.getJSONObject(y)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.errorMessages = errorMessageDatas;
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
        this.timestampResponse = DateOperations.getDate(timestampResponse);
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

    public int getDataCount() {
        return dataCount;
    }

    public void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }

    public int getTotalDataCount() {
        return totalDataCount;
    }

    public void setTotalDataCount(int totalDataCount) {
        this.totalDataCount = totalDataCount;
    }

    public boolean getHasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }
}