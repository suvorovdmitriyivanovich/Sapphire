package com.sapphire.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.adapters.LanguagesAdapter;
import com.sapphire.api.AuthenticationsDeleteAction;
import com.sapphire.api.TemplateDeleteAction;
import com.sapphire.logic.LanguageData;
import java.util.ArrayList;

public class RightFragment extends Fragment implements LanguagesAdapter.OnRootClickListener {
    private View rootView;
    private DrawerLayout drawerLayout;
    private ArrayList<LanguageData> languageDatas;
    private LanguagesAdapter adapter;
    private SharedPreferences sPref;
    private SharedPreferences.Editor ed;

    public RightFragment(){}

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

        TextView name = (TextView) rootView.findViewById(R.id.name);
        name.setText(sPref.getString("USER",""));

        View settings = rootView.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                //if (getActivity().getClass() != MainActivity.class) {
                //    getActivity().finish();
                //}
                //Intent intent = new Intent(getActivity(), PdfActivity.class);
                //startActivity(intent);
            }
        });

        View log_out = rootView.findViewById(R.id.log_out);
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
