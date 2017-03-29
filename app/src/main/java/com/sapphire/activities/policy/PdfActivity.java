package com.sapphire.activities.policy;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.activities.BaseActivity;
import com.sapphire.activities.LoginActivity;
import com.sapphire.api.GetFileAction;
import com.sapphire.api.PolicyLogAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class PdfActivity extends BaseActivity implements PolicyLogAction.RequestPolicyLog,
                                                         GetFileAction.RequestFile,
                                                         OnPageChangeListener,
                                                         OnLoadCompleteListener,
                                                         UpdateAction.RequestUpdate{
    private Long count = 0l;
    private TextView time;
    private boolean needbreak = false;
    private View button_acknowledged;
    private String fileId;
    private String id;
    private ProgressDialog pd;
    private boolean needClose = false;
    private PDFView pdfView;
    private boolean acknowledged = false;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pdf);

        View close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        pdfView = (PDFView) findViewById(R.id.pdfview);
        //pdfView.fromUri(Uri)
        //pdfView.fromFile(File)
        //pdfView.fromBytes(byte[])
        //pdfView.fromStream(InputStream)
        //pdfView.fromSource(DocumentSource)
        /*
        pdfView.fromAsset(String)
                .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .onDraw(onDrawListener)
                .onLoad(onLoadCompleteListener)
                .onPageChange(onPageChangeListener)
                .onPageScroll(onPageScrollListener)
                .onError(onErrorListener)
                .enableAnnotationRendering(false)
                .password(null)
                .scrollHandle(null)
                .load();
        */

        TextView text_header = (TextView) findViewById(R.id.text_header);

        Intent intent = getIntent();
        text_header.setText(intent.getStringExtra("name"));
        count = Long.valueOf(intent.getIntExtra("duration", 0));
        //count = 0l;
        fileId = intent.getStringExtra("fileId");
        id = intent.getStringExtra("id");
        acknowledged = intent.getBooleanExtra("acknowledged", false);

        if (acknowledged) {
            View bottom_group = findViewById(R.id.bottom_group);
            ViewGroup.LayoutParams par_bottom_group = bottom_group.getLayoutParams();
            par_bottom_group.height = 0;
            bottom_group.setLayoutParams(par_bottom_group);
            bottom_group.requestLayout();
        } else {
            button_acknowledged = findViewById(R.id.button_acknowledged);
            button_acknowledged.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pd.show();
                    new PolicyLogAction(PdfActivity.this, id, Environment.PolicyStatusAcknowledged).execute();
                }
            });

            time = (TextView) findViewById(R.id.time);
            /*
            if (count <= 0) {
                time.setVisibility(View.GONE);
                button_acknowledged.setEnabled(true);
            } else {
                time.setText(getTime(count));
                new CountTask().execute();
            }
            */
        }

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

        pd.show();
        new GetFileAction(PdfActivity.this, fileId, "temp.pdf", getFilesDir().getAbsolutePath()).execute();

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(PdfActivity.this);
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
    public void onPageChanged(int page, int pageCount) {
        int i = 0;
        if (i == 0) {
            i = 1;
        }
    }

    @Override
    public void loadComplete(int nbPages) {
        pd.hide();
        if (!acknowledged) {
            if (count <= 0) {
                time.setVisibility(View.GONE);
                button_acknowledged.setEnabled(true);
            } else {
                time.setText(getTime(count));
                new CountTask().execute();
            }
        }
    }

    @Override
    public void onRequestFile(String result, String file) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            pdfView.fromFile(new File(file))
                    .defaultPage(0)
                    .onPageChange(this)
                    .enableAnnotationRendering(true)
                    .onLoad(this)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .load();
        }
    }

    @Override
    public void onRequestPolicyLog(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
        if (needClose) {
            finish();
        }
    }

    private class CountTask extends AsyncTask<String, Void, String> {
        public CountTask() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            if (needbreak) {
                return null;
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String rezult) {
            callBackFromTsk(rezult);
        }
    }

    public void callBackFromTsk(String rezult) {
        if (needbreak) {
            return;
        }

        count = count - 1;
        time.setText(getTime(count));

        if (count > 0) {
            new CountTask().execute();
        } else {
            time.setVisibility(View.GONE);
            button_acknowledged.setEnabled(true);
        }
    }

    private String getTime(Long seconds) {
        Long minutes = seconds / 60;
        Long hours = minutes / 60;
        Long minutest = minutes - (hours * 60);
        Long secondst = seconds - (minutes * 60);

        String rez = "";
        if (hours > 9) {
            rez = ">9";
        } else {
            rez = String.valueOf(hours);
        }
        rez = rez + ":";
        if (String.valueOf(minutest).length() == 1) {
            rez = rez + "0" + String.valueOf(minutest);
        } else {
            rez = rez + String.valueOf(minutest);
        }
        rez = rez + ":";
        if (String.valueOf(secondst).length() == 1) {
            rez = rez + "0" + String.valueOf(secondst);
        } else {
            rez = rez + String.valueOf(secondst);
        }

        return rez;
    }

    private void back() {
        pd.show();
        needClose = true;
        new PolicyLogAction(PdfActivity.this, id, Environment.PolicyStatusStarted).execute();
    }

    @Override
    public void onRequestUpdate(String result) {
        if (!result.equals("OK")) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            if (result.equals(getResources().getString(R.string.text_unauthorized))) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
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
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        needbreak = true;
        super.onDestroy();
        unregisterReceiver(br);
    }
}