package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.ContactData;
import com.sapphire.logic.Environment;
import com.sapphire.logic.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.ResponseData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class GetContactsAction extends AsyncTask{

    public interface RequestContacts {
        public void onRequestContacts(String result);
    }

    public interface RequestContactsData {
        public void onRequestContactsData(ArrayList<ContactData> adressDatas);
    }

    public interface RequestContactsMe {
        public void onRequestContactsMe(String result, ArrayList<ContactData> emergencyDatas, ArrayList<ContactData> familyDatas);
    }

    private Context mContext;
    private ArrayList<ContactData> adressDatas = new ArrayList<ContactData>();
    private ArrayList<ContactData> emergencyDatas = new ArrayList<ContactData>();
    private ArrayList<ContactData> familyDatas = new ArrayList<ContactData>();
    private boolean me = false;

    public GetContactsAction(Context context, boolean me) {
        this.mContext = context;
        this.me = me;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        UserInfo userInfo = UserInfo.getUserInfo();

        String filter = "";
        if (me) {
            filter = "?$filter=(ContactTypeId%20eq%20guid'"+Environment.EmergencyContactType+"'%20or%20ContactTypeId%20eq%20guid'"+Environment.FamilyContactType+"')%20and%20Profiles/any(profile:%20profile/ProfileId%20eq%20guid'"+userInfo.getProfile().getProfileId()+"')";
        } else {
            filter = "?$filter=Profiles/any(profile:%20profile/ProfileId%20eq%20guid'"+userInfo.getProfile().getProfileId()+"')";
        }

        String urlstring = Environment.SERVER + Environment.ContactsURL + filter;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", userInfo.getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            for (int y=0; y < data.length(); y++) {
                try {
                    ContactData contactData = new ContactData(data.getJSONObject(y));
                    if (me) {
                        if (contactData.getContactType().getContactTypeId().equals(Environment.EmergencyContactType)) {
                            emergencyDatas.add(contactData);
                        } else if (contactData.getContactType().getContactTypeId().equals(Environment.FamilyContactType)) {
                            familyDatas.add(contactData);
                        }
                    } else {
                        if (!contactData.getAddress().getAddress().equals("")) {
                            adressDatas.add(contactData);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            result = "OK";
        } else {
            ArrayList<ErrorMessageData> errorMessageDatas = responseData.getErrorMessages();
            if (errorMessageDatas == null || errorMessageDatas.size() == 0) {
                result = responseData.getHttpStatusMessage();
            } else {
                for (int y=0; y < errorMessageDatas.size(); y++) {
                    if (!result.equals("")) {
                        result = result + ". ";
                    }
                    result = errorMessageDatas.get(y).getName();
                }
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        String resultData = (String) o;
        if(mContext!=null) {
            if (me) {
                ((RequestContactsMe) mContext).onRequestContactsMe(resultData, emergencyDatas, familyDatas);
            } else if (resultData.equals("OK")) {
                ((RequestContactsData) mContext).onRequestContactsData(adressDatas);
            } else {
                ((RequestContacts) mContext).onRequestContacts(resultData);
            }
        }
    }
}
