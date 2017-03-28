package com.sapphire.activities.template;

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
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.BaseActivity;
import com.sapphire.activities.MenuFragment;
import com.sapphire.activities.RightFragment;
import com.sapphire.adapters.SpinTypesAdapter;
import com.sapphire.adapters.TemplatesAdapter;
import com.sapphire.api.TemplateDeleteAction;
import com.sapphire.api.TemplatesAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.TemplateData;
import java.util.ArrayList;

public class TemplatesActivity extends BaseActivity implements TemplatesAdapter.OnRootClickListener,
                                                               TemplatesAdapter.OnOpenClickListener,
                                                               TemplatesAdapter.OnDeleteClickListener,
                                                               TemplatesAction.RequestTemplates,
                                                               TemplatesAction.RequestTemplatesData,
                                                               TemplateDeleteAction.RequestTemplateDelete,
                                                               UpdateAction.RequestUpdate{
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
    private String typeId;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;

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

        View add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TemplatesActivity.this, TemplateActivity.class);
                intent.putExtra("typeId", typeId);
                startActivity(intent);
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
                } else if (putreqwest.equals("updatebottom")) {
                    UpdateBottom();
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

        templateslist = (RecyclerView) findViewById(R.id.templateslist);
        templateslist.setNestedScrollingEnabled(false);
        templateslist.setLayoutManager(new LinearLayoutManager(TemplatesActivity.this));

        adapter = new TemplatesAdapter(this);
        templateslist.setAdapter(adapter);

        text_no = findViewById(R.id.text_no);
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        type = (EditText) findViewById(R.id.type);
        typeId = getResources().getString(R.string.text_workplace_templates);
        type.setText(typeId);

        types = new ArrayList<>();
        types.add(getResources().getString(R.string.text_workplace_templates));
        types.add(getResources().getString(R.string.text_meetings_templates));

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
        if (Sapphire.getInstance().getNeedUpdate()) {
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
        //Intent intent = new Intent(PoliciesActivity.this, PdfActivity.class);
        //startActivity(intent);
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
        }
    }

    @Override
    public void onRequestTemplateDelete(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
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
        } else {
            Sapphire.getInstance().setNeedUpdate(NetRequests.getNetRequests().isOnline(false));
            UpdateBottom();
            pd.hide();
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
        new TemplatesAction(TemplatesActivity.this, typeId).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
