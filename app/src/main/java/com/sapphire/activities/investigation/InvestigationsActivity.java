package com.sapphire.activities.investigation;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.activities.BaseActivity;
import com.sapphire.activities.FilesActivity;
import com.sapphire.activities.MenuFragment;
import com.sapphire.activities.RightFragment;
import com.sapphire.adapters.InvestigationsAdapter;
import com.sapphire.api.InvestigationsAction;
import com.sapphire.api.InvestigationDeleteAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.InvestigationData;
import com.sapphire.logic.UserInfo;
import java.util.ArrayList;

public class InvestigationsActivity extends BaseActivity implements InvestigationsAdapter.OnRootInvestigationsClickListener,
                                                                    InvestigationsAdapter.OnOpenInvestigationsClickListener,
                                                                    InvestigationsAdapter.OnDeleteInvestigationsClickListener,
                                                                    InvestigationsAdapter.OnFilesInvestigationsClickListener,
                                                                    InvestigationsAction.RequestInvestigations,
                                                                    InvestigationDeleteAction.RequestInvestigationDelete{
    public final static String PARAM_TASK = "task";
    public final static String BROADCAST_ACTION = "com.sapphire.activities.investigation.Investigations";
    private BroadcastReceiver br;
    private ArrayList<InvestigationData> investigationDatas;
    private InvestigationsAdapter adapter;
    private ProgressDialog pd;
    private RecyclerView list;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private int currentPosition = 0;
    private View text_no;
    private boolean me = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_investigations);

        Intent intent = getIntent();
        me = intent.getBooleanExtra("me", false);

        AlertDialog.Builder adb_save = new AlertDialog.Builder(this);
        adb_save.setCancelable(true);
        LinearLayout view_save = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.dialog_save, null);
        adb_save.setView(view_save);
        tittle_message = (TextView) view_save.findViewById(R.id.tittle);
        button_cancel_save = (Button) view_save.findViewById(R.id.button_cancel);
        button_send_save = (Button) view_save.findViewById(R.id.button_send);
        dialog_confirm = adb_save.create();

        button_cancel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();
            }
        });

        button_send_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();

                pd.show();

                new InvestigationDeleteAction(InvestigationsActivity.this, investigationDatas.get(currentPosition).getInvestigationId()).execute();
            }
        });

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

        View add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvestigationsActivity.this, InvestigationActivity.class);
                intent.putExtra("me", me);
                startActivity(intent);
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

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        list = (RecyclerView) findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(InvestigationsActivity.this));

        adapter = new InvestigationsAdapter(this, false);
        list.setAdapter(adapter);

        text_no = findViewById(R.id.text_no);

        updateVisibility();
    }

    public void updateVisibility() {
        if (investigationDatas == null || investigationDatas.size() == 0) {
            text_no.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            list.setVisibility(View.VISIBLE);
            text_no.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRootInvestigationsClick(int position) {
        //Intent intent = new Intent(PoliciesActivity.this, PdfActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onOpenInvestigationsClick(int position) {
        Intent intent = new Intent(InvestigationsActivity.this, InvestigationActivity.class);
        InvestigationData investigationData = investigationDatas.get(position);
        intent.putExtra("name", investigationData.getName());
        intent.putExtra("description", investigationData.getDescription());
        intent.putExtra("date", investigationData.getDate());
        intent.putExtra("id", investigationData.getInvestigationId());
        intent.putExtra("me", me);
        startActivity(intent);
    }

    @Override
    public void onDeleteInvestigationsClick(int position) {
        currentPosition = position;

        tittle_message.setText(getResources().getString(R.string.text_confirm_delete));
        button_cancel_save.setText(getResources().getString(R.string.text_cancel));
        button_send_save.setText(getResources().getString(R.string.text_delete));
        dialog_confirm.show();
    }

    @Override
    public void onFilesInvestigationsClick(int position) {
        Intent intent = new Intent(InvestigationsActivity.this, FilesActivity.class);
        InvestigationData investigationData = investigationDatas.get(position);
        intent.putExtra("name", investigationData.getName());
        intent.putExtra("id", investigationData.getInvestigationId());
        intent.putExtra("url", Environment.InvestigationsFilesURL);
        intent.putExtra("nameField", "InvestigationId");
        UserInfo.getUserInfo().setFileDatas(investigationData.getFiles());
        startActivity(intent);
    }

    @Override
    public void onRequestInvestigations(String result, ArrayList<InvestigationData> investigationDatas) {
        if (!result.equals("OK")) {
            updateVisibility();
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            this.investigationDatas = investigationDatas;
            adapter.setData(investigationDatas);

            updateVisibility();

            pd.hide();
        }
    }

    @Override
    public void onRequestInvestigationDelete(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            new InvestigationsAction(InvestigationsActivity.this, me).execute();
        }
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

        pd.show();
        new InvestigationsAction(InvestigationsActivity.this, me).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
