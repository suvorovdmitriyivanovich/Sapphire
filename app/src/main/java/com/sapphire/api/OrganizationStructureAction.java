package com.sapphire.api;

import android.content.Context;
import android.os.AsyncTask;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.logic.Environment;
import com.sapphire.models.ErrorMessageData;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.OrganizationData;
import com.sapphire.models.OrganizationStructureData;
import com.sapphire.models.ResponseData;
import com.sapphire.logic.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class OrganizationStructureAction extends AsyncTask{

    public interface RequestOrganizationStructures {
        public void onRequestOrganizationStructure(String result);
    }

    public interface RequestOrganizationStructuresData {
        public void onRequestOrganizationStructuresData(ArrayList<OrganizationStructureData> organizationStructureDatas);
    }

    private Context mContext;
    private ArrayList<OrganizationStructureData> organizationStructureDatas;

    public OrganizationStructureAction(Context context) {
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!NetRequests.getNetRequests().isOnline(true)) {
            return Sapphire.getInstance().getResources().getString(R.string.text_need_internet);
        }

        String filter = "";
        ArrayList<OrganizationData> organizationDatas = UserInfo.getUserInfo().getOrganizations();
        for (OrganizationData item: organizationDatas) {
            if (filter.equals("")) {
                filter = "?$filter=";
            } else {
                filter = filter + "%20or%20";
            }
            filter = filter + "OrganizationId%20eq%20guid'"+item.getOrganizationId()+"'";
        }

        String urlstring = Environment.SERVER + Environment.OrganizationsOrganizationStructureURL + filter;

        ResponseData responseData = new ResponseData(NetRequests.getNetRequests().SendRequestCommon(urlstring,"",0,true,"GET", UserInfo.getUserInfo().getAuthToken()));

        String result = "";

        if (responseData.getSuccess()) {
            JSONArray data = responseData.getData();
            organizationStructureDatas = new ArrayList<OrganizationStructureData>();
            for (int y=0; y < data.length(); y++) {
                try {
                    if (!data.getJSONObject(y).isNull("ParentId")) {
                        continue;
                    }
                    organizationStructureDatas.add(new OrganizationStructureData(data.getJSONObject(y)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (int y=0; y < data.length(); y++) {
                try {
                    if (data.getJSONObject(y).isNull("ParentId")) {
                        continue;
                    }
                    OrganizationStructureData organizationStructureData = new OrganizationStructureData(data.getJSONObject(y));
                    for (int z=0; z < organizationStructureDatas.size(); z++) {
                        if (organizationStructureDatas.get(z).getId().equals(organizationStructureData.getParentId())) {
                            organizationStructureDatas.get(z).getSubOrganizationStructures().add(organizationStructureData);
                            break;
                        } else {
                            OrganizationStructureData parentOrganizationStructureData = findObjectOrganizationStructureData(organizationStructureDatas.get(z), organizationStructureData.getParentId());
                            if (parentOrganizationStructureData != null) {
                                parentOrganizationStructureData.getSubOrganizationStructures().add(organizationStructureData);
                                break;
                            }
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

    private OrganizationStructureData findObjectOrganizationStructureData(OrganizationStructureData data, String parentId) {
        for (OrganizationStructureData item: data.getSubOrganizationStructures()) {
            if (item.getId().equals(parentId)) {
                return item;
            //} else {
            //    return findObjectOrganizationStructureData(item, parentId);
            }
        }
        for (OrganizationStructureData item: data.getSubOrganizationStructures()) {
            return findObjectOrganizationStructureData(item, parentId);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        String resultData = (String) o;
        if(mContext!=null) {
            if (resultData.equals("OK")) {
                ((RequestOrganizationStructuresData) mContext).onRequestOrganizationStructuresData(organizationStructureDatas);
            } else {
                ((RequestOrganizationStructures) mContext).onRequestOrganizationStructure(resultData);
            }
        }
    }
}
