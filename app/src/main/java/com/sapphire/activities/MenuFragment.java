package com.sapphire.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.db.DBHelper;
import com.sapphire.logic.UserInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuFragment extends Fragment {
    private UserInfo userInfo;
    private View rootView;

    public MenuFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.left_menu, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        final DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerLayout);

        userInfo = UserInfo.getUserInfo();
        TextView name = (TextView) rootView.findViewById(R.id.name);

        String username = userInfo.getUserName();
        if (username.equals("")) {
            SharedPreferences sPref = getActivity().getSharedPreferences("GlobalPreferences", getActivity().MODE_PRIVATE);
            username = sPref.getString("USER", "");
            username = "Суворов Дмитрий";
            userInfo.setUserName(username);
        }

        ImageView ico = (ImageView) rootView.findViewById(R.id.ico);
        File f = new File(getActivity().getApplicationContext().getFilesDir().getAbsolutePath() + "/user.png");
        if (f.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
            ico.setImageBitmap(bitmap);
        } else {
            ico.setImageResource(R.drawable.user);
        }

        ArrayList<HashMap<String, Object>> mNameColor = new ArrayList<>();
        //mNameColor = pullTasks.GetNameColor(username);
        //if (mNameColor.size() == 1) {
            //name.setText(mNameColor.get(0).get("name").toString());
            name.setText(userInfo.getUserName());
            String color = "blue";
            try {
                color = mNameColor.get(0).get("color").toString();
            } catch (Exception er) {
                er.printStackTrace();
            }

            View round_circle = rootView.findViewById(R.id.round_circle);

            if(color.equals("blue")) {
                round_circle.setBackgroundResource(R.drawable.shape_list_ico_blue);
            }
        //}

        View settings = rootView.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                if (getActivity().getClass() != MainActivity.class) {
                    getActivity().finish();
                }
                //Intent intent = new Intent(getActivity(), SettingsActivity.class);
                //startActivity(intent);
            }
        });

        View header_root = rootView.findViewById(R.id.header_root);
        header_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                if (getActivity().getClass() != MainActivity.class) {
                    getActivity().finish();
                }
                //Intent intent = new Intent(getActivity(), SettingsActivity.class);
                //startActivity(intent);
            }
        });

        View log_out = rootView.findViewById(R.id.log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();

                SharedPreferences sPref = getActivity().getSharedPreferences("GlobalPreferences", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();

                ed.putBoolean("USEREXIT", true);
                ed.apply();

                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        View main_root = rootView.findViewById(R.id.main_root);
        main_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                if (getActivity().getClass() != MainActivity.class) {
                    getActivity().finish();
                }
            }
        });

        View messages_root = rootView.findViewById(R.id.messages_root);
        messages_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                if (getActivity().getClass() != MainActivity.class) {
                    getActivity().finish();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

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
    }
}
