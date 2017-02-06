package com.sapphire.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.api.GetTemplateAction;
import com.sapphire.logic.TemplateData;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TemplateActivity extends BaseActivity implements GetTemplateAction.RequestTemplate,
                                                                 GetTemplateAction.RequestTemplateData{
    WebView webView;
    private Long count = 0l;
    private TextView time;
    private boolean needbreak = false;
    private View button_acknowledged;
    private String workplaceInspectionTemplateId;
    private String id;
    ProgressDialog pd;
    private boolean needClose = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_template);

        View close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pd.show();
                //needClose = true;
                //new PolicyLogAction(CoursActivity.this, id, Environment.PolicyStatusAcknowledged).execute();
                finish();
            }
        });

        pd = new ProgressDialog(this);
        pd.setTitle(getResources().getString(R.string.text_loading));
        pd.setMessage(getResources().getString(R.string.text_please_wait));
        //диалог который нельзя закрыть пользователем
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        TextView text_header = (TextView) findViewById(R.id.text_header);

        Intent intent = getIntent();
        text_header.setText(intent.getStringExtra("name"));

        workplaceInspectionTemplateId = intent.getStringExtra("workplaceInspectionTemplateId");

        pd.show();
        new GetTemplateAction(TemplateActivity.this, workplaceInspectionTemplateId).execute();

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);

        //count = Long.valueOf(intent.getIntExtra("duration", 0));
        count = 0l;
        //id = intent.getStringExtra("id");

        //File f = new File(android.os.Environment.getExternalStorageDirectory() + "/Download/index.html");
        //webView.loadUrl("file://"+f.getAbsolutePath());
        //webView.setWebViewClient(new HelloWebViewClient());

        /*
        if (intent.getBooleanExtra("acknowledged", false)) {
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
                    //new PolicyLogAction(CoursActivity.this, id, Environment.PolicyStatusStarted).execute();
                }
            });

            time = (TextView) findViewById(R.id.time);
            if (count <= 0) {
                time.setVisibility(View.GONE);
                button_acknowledged.setEnabled(true);
            } else {
                time.setText(getTime(count));
                new CountTask().execute();
            }
        }
        */

        //int i = Integer.parseInt(hex,16);

        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.hide();
            }
        }, 5000);
        */
    }

    @Override
    public void onRequestTemplate(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestTemplateData(ArrayList<TemplateData> templatesDatas) {
        File sdPath = new File(Sapphire.getInstance().getFilesDir() + "/temp/temp.html");
        //File f = new File(android.os.Environment.getExternalStorageDirectory() + "/Download/temp.html");
        //webView.loadUrl("file://"+sdPath.getAbsolutePath());

        //File f = new File(android.os.Environment.getExternalStorageDirectory() + "/Download/index.html");
        //webView.loadUrl("file://"+f.getAbsolutePath());
        webView.loadUrl("http://portal.dealerpilothr.com/api/CourseFile/Get/d10fdc39-182e-cb17-b3b1-8b967cffca91");

        pd.hide();
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

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            //String googleDocs = "https://docs.google.com/viewer?url=http://www.xeroxscanners.com/downloads/Manuals/XMS/PDF_Converter_Pro_Quick_Reference_Guide.RU.pdf";
            //webView.loadUrl(googleDocs + url);

            //String googleDocs = "https://docs.google.com/viewer?url=http://www.xeroxscanners.com/downloads/Manuals/XMS/PDF_Converter_Pro_Quick_Reference_Guide.RU.pdf";
            //view.loadUrl(googleDocs);
            //webView.loadUrl(googleDocs);
            view.loadUrl(url);

            if (url.endsWith(".pdf")
                    || url.equals("https://www.google.com.ua/webhp?output=search&tbm=isch&tbo=u")) {
                //String googleDocs = "https://docs.google.com/viewer?url=";
                //String googleDocs = "https://docs.google.com/viewer?url=http://www.xeroxscanners.com/downloads/Manuals/XMS/PDF_Converter_Pro_Quick_Reference_Guide.RU.pdf";
                //webView.loadUrl(googleDocs);
                //webView.loadUrl(googleDocs + url);
            } else {
                // Load all other urls normally.
                //view.loadUrl(url);
            }

            //pd.hide();
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        needbreak = true;
        super.onDestroy();
    }
}
