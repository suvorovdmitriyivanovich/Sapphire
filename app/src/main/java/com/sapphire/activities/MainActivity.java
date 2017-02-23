package com.sapphire.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.Sapphire;
import com.sapphire.R;
import com.sapphire.logic.UserInfo;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends BaseActivity {
    private static long back_pressed;
    private UserInfo userInfo;
    SharedPreferences sPref;
    SharedPreferences.Editor ed;
    public final static String PARAM_TASK = "task";
    public final static String BROADCAST_ACTION = "com.sapphire.activities.MainActivity";
    BroadcastReceiver br;
    private ViewGroup rootLayout;
    private TextView text_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userInfo = UserInfo.getUserInfo();

        sPref = getSharedPreferences("GlobalPreferences", MODE_PRIVATE);
        ed = sPref.edit();

        setContentView(R.layout.activity_main);

        rootLayout = (ViewGroup) findViewById(R.id.rootLayout);
        text_header = (TextView) findViewById(R.id.text_header);

        View menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        View exit = findViewById(R.id.delete);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        // создаем BroadcastReceiver
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                final String putreqwest = intent.getStringExtra(PARAM_TASK);

                if (putreqwest.equals("updateleftmenu")) {
                    try {
                        Fragment fragment = new MenuFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.nav_left, fragment).commit();
                    } catch (Exception e) {}
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);

        //new UpdateAction(MainActivity.this).execute();

        String addDateString = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        addDateString = format.format(date);

        /*
        MessageData messageData = new MessageData();
        messageData.setMessage("Вход в приложение " + addDateString);
        DBHelper.getInstance(Sapphire.getInstance()).addMessage(messageData);
        */
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            Fragment fragment = new MenuFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_left, fragment).commit();

            Fragment fragmentRight = new RightFragment();
            fragmentManager.beginTransaction().replace(R.id.nav_right, fragmentRight).commit();
        } catch (Exception e) {}
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();

            Sapphire.exit(this);
        } else
            Toast.makeText(getBaseContext(), R.string.text_again_exit,
                    Toast.LENGTH_LONG).show();
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
