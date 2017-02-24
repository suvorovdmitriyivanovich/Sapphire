package com.sapphire.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.activities.template.TemplateActivity;
import com.sapphire.adapters.FilesAdapter;
import com.sapphire.api.FileDeleteAction;
import com.sapphire.api.FilesAction;
import com.sapphire.api.GetFileAction;
import com.sapphire.logic.FileData;
import java.util.ArrayList;

public class FilesActivity extends BaseActivity implements FilesAdapter.OnRootClickListener,
                                                           FilesAdapter.OnDownloadClickListener,
                                                           FilesAdapter.OnDeleteClickListener,
                                                           FilesAction.RequestFiles,
                                                           FileDeleteAction.RequestFileDelete,
                                                           GetFileAction.RequestFile{
    private ArrayList<FileData> fileDatas;
    private FilesAdapter adapter;
    ProgressDialog pd;
    private RecyclerView fileslist;
    private Dialog dialog_confirm;
    private TextView tittle_message;
    private Button button_cancel_save;
    private Button button_send_save;
    private int currentPosition = 0;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_files);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        if (id == null) {
            id = "";
        }
        if (!id.equals("")) {
            TextView text_header = (TextView) findViewById(R.id.text_header);
            text_header.setText(intent.getStringExtra("name") + " " + getResources().getString(R.string.text_files));
        }

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

                new FileDeleteAction(FilesActivity.this, fileDatas.get(currentPosition).getId()).execute();
            }
        });

        View back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilesActivity.this, TemplateActivity.class);
                startActivity(intent);
            }
        });

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        fileslist = (RecyclerView) findViewById(R.id.fileslist);
        fileslist.setNestedScrollingEnabled(false);
        fileslist.setLayoutManager(new LinearLayoutManager(FilesActivity.this));

        adapter = new FilesAdapter(this);
        fileslist.setAdapter(adapter);
    }

    @Override
    public void onRootClick(int position) {
        //Intent intent = new Intent(PoliciesActivity.this, PdfActivity.class);
        //startActivity(intent);
    }

    @Override
    public void onDownloadClick(int position) {
        pd.show();
        new GetFileAction(FilesActivity.this, fileDatas.get(currentPosition).getId(), fileDatas.get(currentPosition).getName()).execute();
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
    public void onRequestFiles(String result, ArrayList<FileData> fileDatas) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            this.fileDatas = fileDatas;
            adapter.setData(fileDatas);
        }
    }

    @Override
    public void onRequestFileDelete(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            new FilesAction(FilesActivity.this).execute();
        }
    }

    @Override
    public void onRequestFile(String result, String file) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        pd.show();
        new FilesAction(FilesActivity.this).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
