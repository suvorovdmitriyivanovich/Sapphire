package com.sapphire.activities.organizationStructure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.sapphire.R;
import com.sapphire.activities.BaseActivity;
import com.sapphire.adapters.organizationStructure.OrganizationStructureAdapter;
import com.sapphire.api.OrganizationStructureAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.OrganizationStructureData;
import com.sapphire.logic.UserInfo;

import java.util.ArrayList;

public class OrganizationStructureActivity extends BaseActivity
        implements OrganizationStructureAdapter.OnRootClickListener,
        OrganizationStructureAdapter.OnAddClickListener,
        OrganizationStructureAction.RequestOrganizationStructures,
        OrganizationStructureAction.RequestOrganizationStructuresData {

    private ProgressDialog pd;
    private OrganizationStructureAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_organization_structure);

        init();
    }

    private void init() {
        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        RecyclerView organizations = (RecyclerView) findViewById(R.id.organizations);
        organizations.setNestedScrollingEnabled(false);
        organizations.setLayoutManager(new LinearLayoutManager(OrganizationStructureActivity.this));
        adapter = new OrganizationStructureAdapter(OrganizationStructureActivity.this);
        organizations.setAdapter(adapter);

        new OrganizationStructureAction(OrganizationStructureActivity.this).execute();
    }

    @Override
    public void onRequestOrganizationStructure(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestOrganizationStructuresData(ArrayList<OrganizationStructureData> organizationStructureDatas) {
        UserInfo.getUserInfo().setOrganizationStructureDatas(organizationStructureDatas);
        adapter.setData(organizationStructureDatas);

        pd.hide();
    }

    @Override
    public void onAddClick(int position) {
        Intent intent = new Intent(OrganizationStructureActivity.this, AddOrganizationStructureActivity.class);
        intent.putExtra(Environment.ID, adapter.getDataItem(position).getId());
        startActivity(intent);
    }

    @Override
    public void onRootClick(int position) {
        if(adapter.getDataItem(position).getSubOrganizationStructures().isEmpty()) {
            return;
        }
        Intent intent = new Intent(OrganizationStructureActivity.this, OrganizationStructureListActivity.class);
        intent.putExtra(Environment.ID, adapter.getDataItem(position).getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
