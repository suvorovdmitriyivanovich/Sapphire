package com.sapphire.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.adapters.AdressAdapter;
import com.sapphire.api.GetContactsAction;
import com.sapphire.api.GetProfilesAction;
import com.sapphire.logic.ContactData;
import com.sapphire.logic.ProfileData;
import java.util.ArrayList;

public class ProfileActivity extends BaseActivity implements AdressAdapter.OnRootClickListener,
                                                             AdressAdapter.OnOpenClickListener,
                                                             GetProfilesAction.RequestProfiles,
                                                             GetProfilesAction.RequestProfilesData,
                                                             GetContactsAction.RequestContacts,
                                                             GetContactsAction.RequestContactsData{
    ProgressDialog pd;
    private TextView contact;
    private TextView additional;
    private TextView employee;
    private TextView payroll;
    private TextView work_additional;
    private RecyclerView adresslist;
    private AdressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

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

        View add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        contact = (TextView) findViewById(R.id.contact);
        additional = (TextView) findViewById(R.id.additional);
        employee = (TextView) findViewById(R.id.employee);
        payroll = (TextView) findViewById(R.id.payroll);
        work_additional = (TextView) findViewById(R.id.work_additional);

        adresslist = (RecyclerView) findViewById(R.id.adresslist);
        adapter = new AdressAdapter(this);
        adresslist.setAdapter(adapter);
        adresslist.setLayoutManager(new LinearLayoutManager(this));

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
        contact.setText(profileData.getContact().getAddress());

        String additionalStr = "";
        additionalStr = additionalStr + getResources().getString(R.string.text_birthday) + ": " + profileData.getBirthdayString();
        additionalStr = additionalStr + "<br>" + getResources().getString(R.string.text_sinnumber) + ": " + profileData.getSINNumber();
        additionalStr = additionalStr + "<br>" + getResources().getString(R.string.text_driver_license) + ": " + profileData.getDriverLicenseNumber();
        additionalStr = additionalStr + "<br>" + getResources().getString(R.string.text_expire_date) + ": " + profileData.getDriverLicenseNumberExpireString();
        additional.setText(Html.fromHtml(additionalStr));

        String employeeStr = "";
        employeeStr = employeeStr + getResources().getString(R.string.text_hire_date) + ": " + profileData.getHireDateString();
        employeeStr = employeeStr + "<br>" + getResources().getString(R.string.text_probation) + ": " + profileData.getProbationEndDateString();
        employeeStr = employeeStr + "<br>" + getResources().getString(R.string.text_termination) + ": " + profileData.getTerminationDateString();
        employeeStr = employeeStr + "<br>" + getResources().getString(R.string.text_hire_type) + ": " + profileData.getHireType();
        employee.setText(Html.fromHtml(employeeStr));

        payroll.setText(profileData.getPayrollInformation());

        String additionalworkStr = "";
        additionalworkStr = additionalworkStr + getResources().getString(R.string.text_work_permit) + ": " + profileData.getWorkPermitNumber();
        additionalworkStr = additionalworkStr + "<br>" + getResources().getString(R.string.text_tech_license) + ": " + profileData.getTechLicenseNumber();
        additionalworkStr = additionalworkStr + "<br>" + getResources().getString(R.string.text_vsr_number) + ": " + profileData.getVSRNumber();
        additionalworkStr = additionalworkStr + "<br>" + getResources().getString(R.string.text_driver_license_expire) + ": " + profileData.getDriverLicenseNumberExpireString();
        additionalworkStr = additionalworkStr + "<br>" + getResources().getString(R.string.text_tech_license_expire) + ": " + profileData.getTechLicenseNumberExpireString();
        additionalworkStr = additionalworkStr + "<br>" + getResources().getString(R.string.text_vsr_expire) + ": " + profileData.getVSRNumberExpireString();
        work_additional.setText(Html.fromHtml(additionalworkStr));

        new GetContactsAction(ProfileActivity.this).execute();
        //pd.hide();
    }

    @Override
    public void onRequestContacts(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestContactsData(ArrayList<ContactData> adressDatas) {
        adapter.setData(adressDatas);

        pd.hide();
    }

    @Override
    public void onRootClick(int position) {

    }

    @Override
    public void onOpenClick(int position) {

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
