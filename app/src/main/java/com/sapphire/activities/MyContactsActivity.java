package com.sapphire.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.adapters.AdressAdapter;
import com.sapphire.api.GetContactsAction;
import com.sapphire.logic.ContactData;
import java.util.ArrayList;

public class MyContactsActivity extends BaseActivity implements GetContactsAction.RequestContactsMe{
    private ProgressDialog pd;
    private RecyclerView emergencylist;
    private AdressAdapter adapterEmergency;
    private RecyclerView familylist;
    private AdressAdapter adapterFamily;
    private View emergency_group;
    private View family_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_contacts);

        View back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pd.show();
                //needClose = true;
                //new PolicyLogAction(CoursActivity.this, id, Environment.PolicyStatusAcknowledged).execute();
                finish();
            }
        });

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        emergency_group = findViewById(R.id.emergency_group);
        family_group = findViewById(R.id.family_group);

        emergencylist = (RecyclerView) findViewById(R.id.emergencylist);
        adapterEmergency = new AdressAdapter(this, true);
        emergencylist.setAdapter(adapterEmergency);
        emergencylist.setLayoutManager(new LinearLayoutManager(this));

        familylist = (RecyclerView) findViewById(R.id.familylist);
        adapterFamily = new AdressAdapter(this, true);
        familylist.setAdapter(adapterFamily);
        familylist.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRequestContactsMe(String result, ArrayList<ContactData> emergencyDatas, ArrayList<ContactData> familyDatas) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            if (emergencyDatas == null || emergencyDatas.size() == 0) {
                emergency_group.setVisibility(View.GONE);
            } else {
                adapterEmergency.setData(emergencyDatas);
                emergency_group.setVisibility(View.VISIBLE);
            }
            if (familyDatas == null || familyDatas.size() == 0) {
                family_group.setVisibility(View.GONE);
            } else {
                adapterFamily.setData(familyDatas);
                family_group.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        pd.show();
        new GetContactsAction(MyContactsActivity.this, true).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
