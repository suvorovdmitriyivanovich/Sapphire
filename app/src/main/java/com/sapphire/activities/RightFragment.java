package com.sapphire.activities;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.course.CoursesActivity;
import com.sapphire.adapters.LanguagesAdapter;
import com.sapphire.api.AuthenticationsDeleteAction;
import com.sapphire.api.PunchesAddAction;
import com.sapphire.models.LanguageData;
import com.sapphire.logic.UserInfo;
import java.io.File;
import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class RightFragment extends Fragment implements LanguagesAdapter.OnRootClickListener{
    private View rootView;
    private DrawerLayout drawerLayout;
    private ArrayList<LanguageData> languageDatas;
    private LanguagesAdapter adapter;
    private SharedPreferences sPref;
    private SharedPreferences.Editor ed;
    private ProgressDialog pd;

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

        sPref = Sapphire.getInstance().getSharedPreferences("GlobalPreferences", Sapphire.getInstance().MODE_PRIVATE);
        ed = sPref.edit();

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
        File f = new File(Sapphire.getInstance().getFilesDir().getAbsolutePath() + "/user.png");
        if (f.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
            ico.setImageBitmap(bitmap);
        } else {
            ico.setImageResource(R.drawable.user);
        }

        TextView name = (TextView) rootView.findViewById(R.id.name);
        String userName = UserInfo.getUserInfo().getProfile().getFullName();
        if (userName.equals("")) {
            userName = sPref.getString("USER","");
        }
        name.setText(userName);

        Typeface typeFace = Typeface.createFromAsset(Sapphire.getInstance().getAssets(),"fonts/fontawesome-webfont.ttf");

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

                new PunchesAddAction(getActivity(), "punchOut").execute();
            }
        });

        View punchout = rootView.findViewById(R.id.punchout);
        punchout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "punchOut").execute();
            }
        });

        View lunchout = rootView.findViewById(R.id.lunchout);
        lunchout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "lunchOut").execute();
            }
        });

        View lunchin = rootView.findViewById(R.id.lunchin);
        lunchin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "lunchIn").execute();
            }
        });

        View breakout = rootView.findViewById(R.id.breakout);
        breakout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "breakOut").execute();
            }
        });

        View breakin = rootView.findViewById(R.id.breakin);
        breakin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "breakIn").execute();
            }
        });

        View statusout = rootView.findViewById(R.id.statusout);
        statusout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "statusOut").execute();
            }
        });

        View statusin = rootView.findViewById(R.id.statusin);
        statusin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new PunchesAddAction(getActivity(), "statusIn").execute();
            }
        });

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