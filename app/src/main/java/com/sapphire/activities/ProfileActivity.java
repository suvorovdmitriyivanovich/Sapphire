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
    private ProgressDialog pd;
    private TextView contact;
    private TextView additional;
    private TextView employee;
    private TextView payroll;
    private TextView work_additional;
    private TextView custom;
    private RecyclerView adresslist;
    private AdressAdapter adapter;
    private View personal_group;

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
        custom = (TextView) findViewById(R.id.custom);
        personal_group = findViewById(R.id.personal_group);

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
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestProfilesData(ProfileData profileData) {
        String detail = "";
        //if (!profileData.getContact().getPhone1().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + "<b>" + getResources().getString(R.string.text_home_phone) + "</b>: " + profileData.getContact().getPhone1();
        //}
        //if (!profileData.getContact().getPhone2().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + "<b>" + getResources().getString(R.string.text_cell_phone) + "</b>: " + profileData.getContact().getPhone2();
        //}
        //if (!profileData.getContact().getEmail1().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + "<b>" + getResources().getString(R.string.text_business_email) + "</b>: " + profileData.getContact().getEmail1();
        //}
        //if (!profileData.getContact().getEmail2().equals("")) {
            if (!detail.equals("")) {
                detail = detail + "<br>";
            }
            detail = detail + "<b>" + getResources().getString(R.string.text_personal_email) + "</b>: " + profileData.getContact().getEmail2();
        //}
        contact.setText(Html.fromHtml(detail));

        String additionalStr = "";
        additionalStr = additionalStr + "<b>" + getResources().getString(R.string.text_birthday) + "</b>: " + profileData.getBirthdayString();
        additionalStr = additionalStr + "<br><b>" + getResources().getString(R.string.text_sinnumber) + "</b>: " + profileData.getSINNumber();
        additionalStr = additionalStr + "<br><b>" + getResources().getString(R.string.text_driver_license) + "</b>: " + profileData.getDriverLicenseNumber();
        additionalStr = additionalStr + "<br><b>" + getResources().getString(R.string.text_expire_date) + "</b>: " + profileData.getDriverLicenseNumberExpireString();
        additional.setText(Html.fromHtml(additionalStr));

        String employeeStr = "";
        employeeStr = employeeStr + "<b>" + getResources().getString(R.string.text_hire_date) + "</b>: " + profileData.getHireDateString();
        employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_probation) + "</b>: " + profileData.getProbationEndDateString();
        employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_hire_type) + "</b>: " + profileData.getHireType();
        employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_manager) + "</b>: " + "";
        employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_secondary_manager) + "</b>: " + "";
        employeeStr = employeeStr + "<br><b>" + getResources().getString(R.string.text_termination) + "</b>: " + profileData.getTerminationDateString();
        employee.setText(Html.fromHtml(employeeStr));

        String payrollStr = "";
        payrollStr = payrollStr + "<b>" + getResources().getString(R.string.text_number) + "</b>: " + profileData.getPayrollInformation();
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_punch_number) + "</b>: " + "";
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_pay_frequency) + "</b>: " + "";
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_hours_per_day) + "</b>: " + "";
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_salary) + "</b>: " + "";
        payrollStr = payrollStr + "<br><b>" + getResources().getString(R.string.text_hourly_rate) + "</b>: " + "";
        payroll.setText(Html.fromHtml(payrollStr));

        String additionalworkStr = "";
        additionalworkStr = additionalworkStr + "<b>" + getResources().getString(R.string.text_work_permit) + "</b>: " + profileData.getWorkPermitNumber();
        additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_driver_license_expire) + "</b>: " + profileData.getDriverLicenseNumberExpireString();
        additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_tech_license) + "</b>: " + profileData.getTechLicenseNumber();
        additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_tech_license_expire) + "</b>: " + profileData.getTechLicenseNumberExpireString();
        additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_vsr_number) + "</b>: " + profileData.getVSRNumber();
        additionalworkStr = additionalworkStr + "<br><b>" + getResources().getString(R.string.text_vsr_expire) + "</b>: " + profileData.getVSRNumberExpireString();
        work_additional.setText(Html.fromHtml(additionalworkStr));

        String customStr = "";
        customStr = customStr + "<b>" + getResources().getString(R.string.text_custom1) + "</b>: " + profileData.getCustomField1();
        customStr = customStr + "<br><b>" + getResources().getString(R.string.text_custom2) + "</b>: " + profileData.getCustomField1();
        custom.setText(Html.fromHtml(customStr));

        new GetContactsAction(ProfileActivity.this).execute();
        //pd.hide();
    }

    @Override
    public void onRequestContacts(String result) {
        pd.hide();
        personal_group.setVisibility(View.GONE);
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestContactsData(ArrayList<ContactData> adressDatas) {
        adapter.setData(adressDatas);
        if (adressDatas.size() == 0) {
            personal_group.setVisibility(View.GONE);
        } else {
            personal_group.setVisibility(View.VISIBLE);
        }

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
