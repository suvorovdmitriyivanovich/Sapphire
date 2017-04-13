package com.sapphire.activities.organizationStructure;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.BaseActivity;
import com.sapphire.adapters.organizationStructure.OrganizationStructureListAdapter;
import com.sapphire.api.OrganizationStructureDeleteAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.models.OrganizationStructureData;
import com.sapphire.logic.UserInfo;

public class OrganizationStructureListActivity extends BaseActivity implements OrganizationStructureListAdapter.OnRootClickListener,
                                                                               OrganizationStructureListAdapter.OnAddClickListener,
                                                                               OrganizationStructureListAdapter.OnEditClickListener,
                                                                               OrganizationStructureListAdapter.OnDeleteClickListener,
                                                                               OrganizationStructureDeleteAction.RequestOrganizationStructureDelete,
                                                                               UpdateAction.RequestUpdate{

    private OrganizationStructureData organizationStructureData;
    private OrganizationStructureListAdapter adapter;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private int currentPosition = 0;
    private ProgressDialog pd;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_organization_structure_list);

        Intent intent = getIntent();
        String id = intent.getStringExtra(Environment.ID);
        if(id != null && !id.isEmpty()) {
            organizationStructureData = UserInfo.getUserInfo().getOrganizationStructureDataById(id);
        }
        else {
            organizationStructureData = new OrganizationStructureData();
        }

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

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

                new OrganizationStructureDeleteAction(OrganizationStructureListActivity.this, organizationStructureData.getSubOrganizationStructures().get(currentPosition).getId()).execute();
            }
        });

        // title name
        TextView text_header = (TextView) findViewById(R.id.text_header);
        text_header.setText(organizationStructureData.getName());

        // back
        View back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView organizations = (RecyclerView) findViewById(R.id.list);
        organizations.setNestedScrollingEnabled(false);
        organizations.setLayoutManager(new LinearLayoutManager(OrganizationStructureListActivity.this));
        adapter = new OrganizationStructureListAdapter(OrganizationStructureListActivity.this);
        organizations.setAdapter(adapter);

        adapter.setData(organizationStructureData.getSubOrganizationStructures());

        // создаем BroadcastReceiver
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                final String putreqwest = intent.getStringExtra(Environment.PARAM_TASK);

                if (putreqwest.equals("updatebottom")) {
                    UpdateBottom();
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(Environment.BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(OrganizationStructureListActivity.this);
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

    @Override
    public void onRootClick(int position) {
        if(adapter.getDataItem(position).getSubOrganizationStructures().isEmpty()) {
            return;
        }
        Intent intent = new Intent(OrganizationStructureListActivity.this, OrganizationStructureListActivity.class);
        intent.putExtra(Environment.ID, adapter.getDataItem(position).getId());
        startActivity(intent);
    }

    @Override
    public void onAddClick(int position) {
        Intent intent = new Intent(OrganizationStructureListActivity.this, OrganizationStructureItemActivity.class);
        intent.putExtra("parrentId", adapter.getDataItem(position).getId());
        startActivity(intent);
    }

    @Override
    public void onEditClick(int position) {
        Intent intent = new Intent(OrganizationStructureListActivity.this, OrganizationStructureItemActivity.class);
        intent.putExtra(Environment.ID, adapter.getDataItem(position).getId());
        intent.putExtra("parrentId", organizationStructureData.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        //organizationStructureData.getSubOrganizationStructures().get(position).getId();
        //adapter.removeDataItem(position);

        currentPosition = position;

        tittle_message.setText(getResources().getString(R.string.text_confirm_delete));
        button_cancel_save.setText(getResources().getString(R.string.text_cancel));
        button_send_save.setText(getResources().getString(R.string.text_delete));
        dialog_confirm.show();
    }

    @Override
    public void onRequestOrganizationStructureDelete(String result) {
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
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            adapter.removeDataItem(currentPosition);
        }
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
                    Sapphire.getInstance().sendBroadcast(intExit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        } else {
            Sapphire.getInstance().setNeedUpdate(NetRequests.getNetRequests().isOnline(false));
            UpdateBottom();
            pd.hide();
        }
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
