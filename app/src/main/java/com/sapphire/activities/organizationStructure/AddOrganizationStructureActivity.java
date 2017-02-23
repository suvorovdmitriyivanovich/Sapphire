package com.sapphire.activities.organizationStructure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.sapphire.R;
import com.sapphire.activities.BaseActivity;
import com.sapphire.logic.Environment;
import com.sapphire.logic.OrganizationStructureData;
import com.sapphire.logic.UserInfo;

public class AddOrganizationStructureActivity extends BaseActivity {

    OrganizationStructureData organizationStructureData;

    EditText name;
    EditText description;
    CheckBox isCurrent;
    CheckBox isPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_organization_structure);

        Intent intent = getIntent();
        String id = intent.getStringExtra(Environment.ID);
        boolean edit = intent.getBooleanExtra(Environment.EDIT, false);
        if(id != null && !id.isEmpty()) {
            organizationStructureData = UserInfo.getUserInfo().getOrganizationStructureDataById(id);
        }
        else {
            organizationStructureData = new OrganizationStructureData();
        }

        TextView text_header = (TextView) findViewById(R.id.text_header);
        text_header.setText(organizationStructureData.getName());

        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        isCurrent = (CheckBox) findViewById(R.id.current);
        isPosition = (CheckBox) findViewById(R.id.position);
        isPosition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isCurrent.setVisibility(View.GONE);
                } else {
                    isCurrent.setVisibility(View.VISIBLE);
                }
            }
        });

        if(edit) {
            name.setText(organizationStructureData.getName());
            description.setText(organizationStructureData.getDescription());
            isPosition.setChecked(organizationStructureData.getIsPosition());
            isCurrent.setChecked(organizationStructureData.getIsCurrent());

            if (organizationStructureData.getIsPosition()) {
                isCurrent.setVisibility(View.GONE);
            }
        }

        View back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        View ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO save item and quit
                saveItem();
                onBackPressed();
            }
        });
    }

    private void saveItem() {

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
