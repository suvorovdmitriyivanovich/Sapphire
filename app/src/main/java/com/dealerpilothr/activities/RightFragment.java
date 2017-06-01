package com.dealerpilothr.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.adapters.LanguagesAdapter;
import com.dealerpilothr.adapters.SpinOrganizationsAdapter;
import com.dealerpilothr.api.PunchesAddAction;
import com.dealerpilothr.R;
import com.dealerpilothr.api.AuthenticationsDeleteAction;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.models.LanguageData;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.OrganizationData;
import java.io.File;
import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class RightFragment extends Fragment implements LanguagesAdapter.OnRootClickListener {
    private View rootView;
    private DrawerLayout drawerLayout;
    private ArrayList<LanguageData> languageDatas;
    private LanguagesAdapter adapter;
    private SharedPreferences sPref;
    private SharedPreferences.Editor ed;
    private ProgressDialog pd;
    private EditText organization;
    private Spinner spinnerOrganization;
    private ArrayList<OrganizationData> organizations;
    private SpinOrganizationsAdapter adapterOrganization;
    private boolean clickSpinner = false;
    private UserInfo userInfo;

    @SuppressLint("ValidFragment")
    public RightFragment(ProgressDialog pd){
        this.pd = pd;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.right_menu, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerLayout);

        userInfo = UserInfo.getUserInfo();

        sPref = Dealerpilothr.getInstance().getSharedPreferences("GlobalPreferences", Dealerpilothr.getInstance().MODE_PRIVATE);
        ed = sPref.edit();

        spinnerOrganization = (Spinner) rootView.findViewById(R.id.spinnerOrganization);
        organization = (EditText) rootView.findViewById(R.id.organization);

        languageDatas = new ArrayList<LanguageData>();
        LanguageData languageDataEn = new LanguageData("en");
        if (!sPref.getString("LANGUAGE", "").equals("fr")) {
            languageDataEn.setChecked(true);
        }
        languageDatas.add(languageDataEn);
        LanguageData languageDataFr = new LanguageData("fr");
        if (sPref.getString("LANGUAGE", "").equals("fr")) {
            languageDataFr.setChecked(true);
        }
        languageDatas.add(languageDataFr);

        final ListView languagelist = (ListView) rootView.findViewById(R.id.languagelist);

        adapter = new LanguagesAdapter(this, languageDatas);
        languagelist.setAdapter(adapter);

        ImageView ico = (ImageView) rootView.findViewById(R.id.ico);
        File f = new File(Dealerpilothr.getInstance().getFilesDir().getAbsolutePath() + "/user.png");
        if (f.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
            ico.setImageBitmap(bitmap);
        } else {
            ico.setImageResource(R.drawable.user);
        }

        TextView name = (TextView) rootView.findViewById(R.id.name);
        String userName = userInfo.getProfile().getFullName();
        if (userName.equals("")) {
            userName = sPref.getString("USER","");
        }
        name.setText(userName);

        Typeface typeFace = Typeface.createFromAsset(Dealerpilothr.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");

        TextView logoutIco = (TextView) rootView.findViewById(R.id.logoutIco);
        logoutIco.setTypeface(typeFace);
        logoutIco.setText(Html.fromHtml("&#61579;"));

        View log_out = rootView.findViewById(R.id.logout_group);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();

                new AuthenticationsDeleteAction(null).execute();

                SharedPreferences sPref = getActivity().getSharedPreferences("GlobalPreferences", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();

                ed.putBoolean("USEREXIT", true);
                ed.apply();

                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        View punchin = rootView.findViewById(R.id.punchin);
        punchin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "Punch In", "d71cdd39-a1ce-f2dd-35e3-70e550bed74c").execute();
            }
        });

        View punchout = rootView.findViewById(R.id.punchout);
        punchout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "Punch Out", "d71cdd39-a1ce-f2dd-35e3-70e550bed74c").execute();
            }
        });

        View lunchout = rootView.findViewById(R.id.lunchout);
        lunchout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "Lunch Out", "").execute();
            }
        });

        View lunchin = rootView.findViewById(R.id.lunchin);
        lunchin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "Lunch In", "").execute();
            }
        });

        View breakout = rootView.findViewById(R.id.breakout);
        breakout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "Break Out", "").execute();
            }
        });

        View breakin = rootView.findViewById(R.id.breakin);
        breakin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "Break In", "").execute();
            }
        });

        View statusout = rootView.findViewById(R.id.statusout);
        statusout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "Status Out", "").execute();
            }
        });

        View statusin = rootView.findViewById(R.id.statusin);
        statusin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "Status In", "").execute();
            }
        });

        organizations = new ArrayList<>();
        organizations.addAll(userInfo.getOrganizations());

        if (organizations.size() > 1) {
            adapterOrganization = new SpinOrganizationsAdapter(getActivity(), R.layout.spinner_list_item_black);
            spinnerOrganization.setAdapter(adapterOrganization);
            adapterOrganization.setValues(organizations);
            organization.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickSpinner = true;
                    spinnerOrganization.performClick();
                }
            });
            spinnerOrganization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!clickSpinner) {
                        return;
                    }
                    organization.setText(organizations.get(position).getName());
                    userInfo.setAuthToken(organizations.get(position).getAuthToken());
                    clickSpinner = false;
                    drawerLayout.closeDrawers();
                    Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                    try {
                        intExit.putExtra(Environment.PARAM_TASK, "update");
                        Dealerpilothr.getInstance().sendBroadcast(intExit);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            if (!userInfo.getCurrentOrganization().getOrganizationId().equals("")) {
                int organizationPosition = 0;
                for (int i = 0; i < organizations.size(); i++) {
                    if (organizations.get(i).getOrganizationId().equals(userInfo.getCurrentOrganization().getOrganizationId())) {
                        organizationPosition = i;
                        break;
                    }
                }
                organization.setText(organizations.get(organizationPosition).getName());
                final int finalOrganizationPosition = organizationPosition;
                organization.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        spinnerOrganization.setSelection(finalOrganizationPosition, false);
                    }
                }, 10);
            }
        } else {
            View organization_group = rootView.findViewById(R.id.organization_group);
            organization_group.setVisibility(View.GONE);
        }

        if (userInfo.getMasterOrganization().getOrganizationId().equals("")
           || userInfo.getMasterOrganization().getProfile().getProfileId().equals("")
           || !userInfo.getGlobalAppRoleAppSecurities().hasGlobalAppRoleAppSecurities("d71cdd39-a1ce-4db1-c8ba-88e289d6256a")
           || !userInfo.getGlobalAppRoleAppSecurities().isActiveGlobalAppRoleAppSecurities("d71cdd39-a1ce-4db1-c8ba-88e289d6256a")) {
            punchin.setVisibility(View.GONE);
            punchout.setVisibility(View.GONE);
            lunchout.setVisibility(View.GONE);
            lunchin.setVisibility(View.GONE);
            breakout.setVisibility(View.GONE);
            breakin.setVisibility(View.GONE);
            statusout.setVisibility(View.GONE);
            statusin.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onRootClick(int position) {
        String language = languageDatas.get(position).getName();
        if (!sPref.getString("LANGUAGE","").equals(language)) {
            ed.putString("LANGUAGE", language);
            ed.apply();

            getActivity().recreate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
        int countmessages = DBHelper.getInstance(Sapphire.getInstance()).getMessages(0,-1).size();
        if (countmessages != 0) {
            View round_message = rootView.findViewById(R.id.round_message);
            round_message.setVisibility(View.VISIBLE);

            TextView count_messages = (TextView) rootView.findViewById(R.id.count_messages);
            if (countmessages > 99) {
                count_messages.setText("99+");
            } else {
                count_messages.setText(String.valueOf(countmessages));
            }
        }
        */
    }
}