package com.sapphire.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.adapters.AdressAdapter;
import com.sapphire.api.GetProfilesAction;
import com.sapphire.logic.ProfileData;

public class ProfileActivity extends BaseActivity implements AdressAdapter.OnRootClickListener,
                                                               GetProfilesAction.RequestProfiles,
                                                               GetProfilesAction.RequestProfilesData{
    ProgressDialog pd;
    private ProfileData profileData;
    private ListView adresslist;
    private AdressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        View close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
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

        adresslist = (ListView) findViewById(R.id.adresslist);
        adapter = new AdressAdapter(this);
        adresslist.setAdapter(adapter);

        pd.show();
        new GetProfilesAction(ProfileActivity.this).execute();
    }

    @Override
    public void onRequestProfiles(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestProfilesData(ProfileData profileData) {
        this.profileData = profileData;

        pd.hide();
    }

    @Override
    public void onRootClick(int position) {

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
