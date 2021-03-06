package com.dealerpilothr.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dealerpilothr.adapters.BulletinsAdapter;
import com.dealerpilothr.logic.NetRequests;
import com.dealerpilothr.models.BulletinData;
import com.dealerpilothr.ui.OpenFileDialog;
import com.dealerpilothr.R;
import com.dealerpilothr.Dealerpilothr;
import com.dealerpilothr.api.BulletinsAction;
import com.dealerpilothr.api.GetFileAction;
import com.dealerpilothr.api.UpdateAction;
import com.dealerpilothr.logic.Environment;

import java.io.File;
import java.util.ArrayList;

public class BulletinActivity extends BaseActivity implements BulletinsAdapter.OnRootBulletinsClickListener,
                                                              BulletinsAdapter.OnAttachmentBulletinsClickListener,
                                                              BulletinsAction.RequestBulletins,
                                                              GetFileAction.RequestFile,
                                                              UpdateAction.RequestUpdate{
    private ProgressDialog pd;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;
    private ArrayList<BulletinData> datas;
    private BulletinsAdapter adapter;
    private RecyclerView list;
    private View text_no;
    private DrawerLayout drawerLayout;
    private OpenFileDialog fileDialogBuilder;
    private Dialog fileDialog;
    private String file = "";
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bulletin);

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

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        list = (RecyclerView) findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(BulletinActivity.this));

        adapter = new BulletinsAdapter(this);
        list.setAdapter(adapter);

        text_no = findViewById(R.id.text_no);

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
                    new BulletinsAction(BulletinActivity.this).execute();
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(Environment.BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);

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

                Uri uri = Uri.fromFile(new File(file.toLowerCase()));
                //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(uri));
                String mType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                intent.setDataAndType(uri, mType);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.text_error_open),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(BulletinActivity.this);
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

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
        if (datas == null || datas.size() == 0) {
            text_no.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            list.setVisibility(View.VISIBLE);
            text_no.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRootBulletinsClick(int position) {

    }

    @Override
    public void onAttachmentBulletinsClick(int position) {
        final BulletinData data = datas.get(position);

        fileDialogBuilder = new OpenFileDialog(this, 2, ".qqq\\.qqq")
                //.setFilter(".qqq\\.qqq")
                .setAccessDeniedMessage(getResources().getString(R.string.text_access_denied))
                //.setFolderIcon(ContextCompat.getDrawable(this, R.drawable.folder))
                //.setFileIcon(ContextCompat.getDrawable(this, R.drawable.file))
                .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                    @Override
                    public void OnSelectedFile(String folder) {
                        pd.show();
                        new GetFileAction(BulletinActivity.this, data.getFileId(), "", folder, "").execute();
                    }
                });
        fileDialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //dialog.cancel();
                    fileDialogBuilder.onBackPressed();
                    return true;
                }
                return false;
            }
        });
        fileDialog = fileDialogBuilder.create();
        fileDialogBuilder.setDialog(fileDialog);
        fileDialog.show();
    }

    @Override
    public void onRequestFile(String result, String file) {
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
        } else {
            this.file = file;
            tittle_message.setText(getResources().getString(R.string.text_open_file));
            button_cancel_save.setText(getResources().getString(R.string.text_no));
            button_send_save.setText(getResources().getString(R.string.text_open));
            dialog_confirm.show();
        }
    }

    @Override
    public void onRequestBulletins(String result, ArrayList<BulletinData> datas) {
        if (!result.equals("OK")) {
            updateVisibility();
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
            this.datas = datas;
            adapter.setData(datas);

            updateVisibility();

            pd.hide();
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

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
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

        pd.show();
        new BulletinsAction(BulletinActivity.this).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}
