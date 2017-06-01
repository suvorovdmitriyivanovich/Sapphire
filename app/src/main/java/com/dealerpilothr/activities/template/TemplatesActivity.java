package com.dealerpilothr.activities.template;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.activities.MenuFragment;
import com.dealerpilothr.adapters.TemplatesAdapter;
import com.dealerpilothr.api.TemplateDeleteAction;
import com.dealerpilothr.api.TemplatesAction;
import com.dealerpilothr.api.UpdateAction;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.R;
import com.dealerpilothr.activities.BaseActivity;
import com.dealerpilothr.activities.RightFragment;
import com.dealerpilothr.adapters.SpinTypesAdapter;
import com.dealerpilothr.api.PunchesAddAction;
import com.dealerpilothr.logic.Environment;
import com.dealerpilothr.logic.UserInfo;
import com.dealerpilothr.models.TemplateData;
import java.util.ArrayList;

public class TemplatesActivity extends BaseActivity implements TemplatesAdapter.OnRootClickListener,
                                                               TemplatesAdapter.OnOpenClickListener,
                                                               TemplatesAdapter.OnDeleteClickListener,
        TemplatesAction.RequestTemplates,
                                                               TemplatesAction.RequestTemplatesData,
        TemplateDeleteAction.RequestTemplateDelete,
        UpdateAction.RequestUpdate,
                                                               PunchesAddAction.RequestPunchesAdd{

    private BroadcastReceiver br;
    private ArrayList<TemplateData> templatesDatas;
    private TemplatesAdapter adapter;
    private ProgressDialog pd;
    private RecyclerView templateslist;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private int currentPosition = 0;
    private View text_no;
    private Spinner spinnerType;
    private ArrayList<String> types;
    private SpinTypesAdapter adapterType;
    private boolean clickSpinner = false;
    private EditText type;
    private String typeId = "";
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private DrawerLayout drawerLayout;
    private boolean editMeetings = false;
    private boolean editWorkplace = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_templates);

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

                new TemplateDeleteAction(TemplatesActivity.this, templatesDatas.get(currentPosition).getTemplateId(), typeId).execute();
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

        final View add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!typeId.equals("")) {
                    Intent intent = new Intent(TemplatesActivity.this, TemplateActivity.class);
                    intent.putExtra("typeId", typeId);
                    startActivity(intent);
                }
            }
        });

        // создаем BroadcastReceiver
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                final String putreqwest = intent.getStringExtra(Environment.PARAM_TASK);

                if (putreqwest.equals("updateleftmenu")) {
                    try {
                        Fragment fragment = new MenuFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.nav_left, fragment).commit();
                    } catch (Exception e) {}
                } else if (putreqwest.equals("updaterightmenu")) {
                    try {
                        Fragment fragmentRight = new RightFragment(pd);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.nav_right, fragmentRight).commit();
                    } catch (Exception e) {}
                } else if (putreqwest.equals("updatebottom")) {
                    UpdateBottom();
                } else if (putreqwest.equals("update")) {
                    pd.show();
                    new TemplatesAction(TemplatesActivity.this, typeId).execute();
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(Environment.BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        templateslist = (RecyclerView) findViewById(R.id.templateslist);
        templateslist.setNestedScrollingEnabled(false);
        templateslist.setLayoutManager(new LinearLayoutManager(TemplatesActivity.this));

        UserInfo userInfo = UserInfo.getUserInfo();
        boolean viewMeetings = false;
        boolean viewWorkplace = false;
        String securityModeMeetings = userInfo.getGlobalAppRoleAppSecurities().getSecurityMode("/configuration/health-and-safety/meetings-topic-templates", "");
        if (securityModeMeetings.equals("fullAccess")) {
            editMeetings = true;
        } else if (securityModeMeetings.equals("viewOnly")) {
            viewMeetings = true;
        }

        String securityModeWorkplace = userInfo.getGlobalAppRoleAppSecurities().getSecurityMode("/configuration/health-and-safety/workplace-investigation-templates", "");
        if (securityModeWorkplace.equals("fullAccess")) {
            editWorkplace = true;
        } else if (securityModeWorkplace.equals("viewOnly")) {
            viewWorkplace = true;
        }

        text_no = findViewById(R.id.text_no);
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        type = (EditText) findViewById(R.id.type);
        if (editWorkplace || viewWorkplace) {
            typeId = getResources().getString(R.string.text_workplace_templates);
            adapter = new TemplatesAdapter(this, editWorkplace);

            if (!editWorkplace) {
                add.setVisibility(View.GONE);
            }
        } else if (editMeetings || viewMeetings) {
            typeId = getResources().getString(R.string.text_meetings_templates);
            adapter = new TemplatesAdapter(this, editMeetings);

            if (!editMeetings) {
                add.setVisibility(View.GONE);
            }
        }
        type.setText(typeId);

        templateslist.setAdapter(adapter);

        types = new ArrayList<>();
        if (editWorkplace || viewWorkplace) {
            types.add(getResources().getString(R.string.text_workplace_templates));
        }
        if (editMeetings || viewMeetings) {
            types.add(getResources().getString(R.string.text_meetings_templates));
        }

        adapterType = new SpinTypesAdapter(this, R.layout.spinner_list_item_black);
        spinnerType.setAdapter(adapterType);
        adapterType.setValues(types);
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSpinner = true;
                spinnerType.performClick();
            }
        });
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!clickSpinner) {
                    return;
                }
                type.setText(types.get(position));
                typeId = types.get(position);
                clickSpinner = false;

                if (typeId.equals(getResources().getString(R.string.text_workplace_templates))) {
                    adapter = new TemplatesAdapter(TemplatesActivity.this, editWorkplace);

                    if (!editWorkplace) {
                        add.setVisibility(View.GONE);
                    } else {
                        add.setVisibility(View.VISIBLE);
                    }
                } else if (typeId.equals(getResources().getString(R.string.text_meetings_templates))) {
                    adapter = new TemplatesAdapter(TemplatesActivity.this, editMeetings);

                    if (!editMeetings) {
                        add.setVisibility(View.GONE);
                    } else {
                        add.setVisibility(View.VISIBLE);
                    }
                }
                templateslist.setAdapter(adapter);

                pd.show();
                new TemplatesAction(TemplatesActivity.this, typeId).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(TemplatesActivity.this);
            }
        });

        UpdateBottom();
    }

    private void UpdateBottom() {
        if (Dealerpilothr.getInstance().getNeedUpdate()) {
            par_nointernet_group.height = GetPixelFromDips(56);
        } else {
            par_nointernet_group.height = 0;
        }
        nointernet_group.setLayoutParams(par_nointernet_group);
        nointernet_group.requestLayout();
    }

    public void updateVisibility() {
        if (templatesDatas == null || templatesDatas.size() == 0) {
            text_no.setVisibility(View.VISIBLE);
            templateslist.setVisibility(View.GONE);
        } else {
            templateslist.setVisibility(View.VISIBLE);
            text_no.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRootClick(int position) {
        Intent intent = new Intent(TemplatesActivity.this, TemplateActivity.class);
        TemplateData templateData = templatesDatas.get(position);
        intent.putExtra("readonly", true);
        intent.putExtra("name", templateData.getName());
        intent.putExtra("description", templateData.getDescription());
        intent.putExtra("templateId", templateData.getTemplateId());
        intent.putExtra("typeId", typeId);
        startActivity(intent);
    }

    @Override
    public void onOpenClick(int position) {
        Intent intent = new Intent(TemplatesActivity.this, TemplateActivity.class);
        TemplateData templateData = templatesDatas.get(position);
        intent.putExtra("name", templateData.getName());
        intent.putExtra("description", templateData.getDescription());
        intent.putExtra("templateId", templateData.getTemplateId());
        intent.putExtra("typeId", typeId);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        currentPosition = position;

        tittle_message.setText(getResources().getString(R.string.text_confirm_delete));
        button_cancel_save.setText(getResources().getString(R.string.text_cancel));
        button_send_save.setText(getResources().getString(R.string.text_delete));
        dialog_confirm.show();
    }

    @Override
    public void onRequestTemplates(String result) {
        updateVisibility();
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        }
    }

    @Override
    public void onRequestTemplateDelete(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            new TemplatesAction(TemplatesActivity.this, typeId).execute();
        }
    }

    @Override
    public void onRequestTemplatesData(ArrayList<TemplateData> templatesDatas, String type) {
        this.templatesDatas = templatesDatas;
        adapter.setData(templatesDatas);

        updateVisibility();

        pd.hide();
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    public void onRequestUpdate(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            Dealerpilothr.getInstance().setNeedUpdate(NetRequests.getNetRequests().isOnline(false));
            UpdateBottom();
            pd.hide();
        }
    }

    @Override
    public void onRequestPunchesAdd(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(Dealerpilothr.getInstance(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                //Intent intent = new Intent(this, LoginActivity.class);
                //startActivity(intent);
                Intent intExit = new Intent(Environment.BROADCAST_ACTION);
                try {
                    intExit.putExtra(Environment.PARAM_TASK, "unauthorized");
                    Dealerpilothr.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            drawerLayout.closeDrawers();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            Fragment fragment = new MenuFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_left, fragment).commit();

            Fragment fragmentRight = new RightFragment(pd);
            fragmentManager.beginTransaction().replace(R.id.nav_right, fragmentRight).commit();
        } catch (Exception e) {}

        if (!typeId.equals("")) {
            pd.show();
            new TemplatesAction(TemplatesActivity.this, typeId).execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
