package com.sapphire.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.api.GetCourseFileAction;
import com.sapphire.logic.CoursesData;
import com.sapphire.logic.UserInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventCalendarActivity extends BaseActivity implements GetCourseFileAction.RequestCourses,
                                                                   GetCourseFileAction.RequestCoursesData{
    private WebView webView;
    private ProgressDialog pd;
    private boolean needClose = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_calendar);

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

        //pd.show();
        //new GetCourseFileAction(EventCalendarActivity.this, courseId).execute();

        pd.show();
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());

        Map<String, String> extraHeaders = new HashMap<String, String>();
        //SharedPreferences sPref = getSharedPreferences("GlobalPreferences", MODE_PRIVATE);
        //extraHeaders.put("x-yauth", sPref.getString("AUTHTOKEN",""));
        extraHeaders.put("x-yauth", UserInfo.getUserInfo().getAuthToken());
        webView.loadUrl("http://portal.dealerpilothr.com/me/my-calendar",extraHeaders);

        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.hide();
            }
        }, 15000);
        */
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pd.hide();
        }
    }

    @Override
    public void onRequestCourses(String result) {
        pd.hide();
        if (!result.equals("OK")) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestCoursesData(ArrayList<CoursesData> coursesDatas) {
        File sdPath = new File(Sapphire.getInstance().getFilesDir() + "/temp/temp.html");
        //File f = new File(android.os.Environment.getExternalStorageDirectory() + "/Download/temp.html");
        //webView.loadUrl("file://"+sdPath.getAbsolutePath());

        //File f = new File(android.os.Environment.getExternalStorageDirectory() + "/Download/index.html");
        //webView.loadUrl("file://"+f.getAbsolutePath());
        webView.loadUrl("http://portal.dealerpilothr.com/api/CourseFile/Get/d10fdc39-182e-cb17-b3b1-8b967cffca91");

        pd.hide();
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
        super.onDestroy();
    }
}
