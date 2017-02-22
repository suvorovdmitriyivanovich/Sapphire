package com.sapphire.activities.organizationStructure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sapphire.R;
import com.sapphire.activities.BaseActivity;
import com.sapphire.adapters.organizationStructure.OrganizationStructureListAdapter;
import com.sapphire.logic.Environment;
import com.sapphire.logic.OrganizationStructureData;
import com.sapphire.logic.UserInfo;

public class OrganizationStructureListActivity extends BaseActivity implements OrganizationStructureListAdapter.OnRootClickListener,
        OrganizationStructureListAdapter.OnAddClickListener, OrganizationStructureListAdapter.OnEditClickListener, OrganizationStructureListAdapter.OnDeleteClickListener {

    private OrganizationStructureData organizationStructureData;
    private OrganizationStructureListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_organization_structure_list);

        init();
    }

    private void init() {
        Intent intent = getIntent();
        String id = intent.getStringExtra(Environment.ID);
        if(id != null && !id.isEmpty()) {
            organizationStructureData = UserInfo.getUserInfo().getOrganizationStructureDataById(id);
        }
        else {
            organizationStructureData = new OrganizationStructureData();
        }

        // title name
        TextView text_header = (TextView) findViewById(R.id.text_header);
        text_header.setText(organizationStructureData.getName());

        // back
        View back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        RecyclerView organizations = (RecyclerView) findViewById(R.id.list);
        organizations.setNestedScrollingEnabled(false);
        organizations.setLayoutManager(new LinearLayoutManager(OrganizationStructureListActivity.this));
        adapter = new OrganizationStructureListAdapter(OrganizationStructureListActivity.this);
        organizations.setAdapter(adapter);

        adapter.setData(organizationStructureData.getSubOrganizationStructures());
    }

    @Override
    public void onRootClick(int position) {
        if(adapter.getDataItem(position).getSubOrganizationStructures().isEmpty()) {
            return;
        }
        Intent intent = new Intent(OrganizationStructureListActivity.this, OrganizationStructureListActivity.class);
        intent.putExtra(Environment.ID, adapter.getDataItem(position).getId());
        startActivity(intent);
    }

    @Override
    public void onAddClick(int position) {
        Intent intent = new Intent(OrganizationStructureListActivity.this, AddOrganizationStructureActivity.class);
        intent.putExtra(Environment.ID, adapter.getDataItem(position).getId());
        startActivity(intent);
    }

    @Override
    public void onEditClick(int position) {
        Intent intent = new Intent(OrganizationStructureListActivity.this, AddOrganizationStructureActivity.class);
        intent.putExtra(Environment.ID, adapter.getDataItem(position).getId());
        intent.putExtra(Environment.EDIT, true);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        adapter.removeDataItem(position);
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
