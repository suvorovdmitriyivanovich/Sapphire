package com.sapphire.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.api.CourseLogAction;
import com.sapphire.api.GetCourseFileAction;
import com.sapphire.logic.CoursesData;
import com.sapphire.logic.Environment;
import java.util.ArrayList;

public class CourseActivity extends BaseActivity implements GetCourseFileAction.RequestCourses,
                                                                 GetCourseFileAction.RequestCoursesData,
                                                                 CourseLogAction.RequestCourseLog{
    WebView webView;
    private String courseId;
    ProgressDialog pd;
    private boolean sendStart = false;
    private View bottom_group;
    private ViewGroup.LayoutParams par_bottom_group;
    private boolean sendFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cours);

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
        bottom_group = findViewById(R.id.bottom_group);
        par_bottom_group = bottom_group.getLayoutParams();

        View button_next = findViewById(R.id.button_next);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFinish = true;
                pd.show();
                new CourseLogAction(CourseActivity.this, courseId, Environment.AccountCourseFileStatusFinish).execute();
            }
        });

        Intent intent = getIntent();
        text_header.setText(intent.getStringExtra("name"));

        courseId = intent.getStringExtra("courseId");

        pd.show();
        new GetCourseFileAction(CourseActivity.this, courseId).execute();

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "INTERFACE");
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        //webView.loadUrl();

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
    public void onRequestCourseLog(String result) {
        if (!result.equals("OK")) {
            sendStart = false;
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
        } else {
            pd.hide();
            if (sendFinish) {
                finish();
            }
        }
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private class MyJavaScriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void processContent(final String aContent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (aContent.indexOf("**") != -1) {
                        par_bottom_group.height = GetPixelFromDips(56);
                    } else {
                        par_bottom_group.height = 0;
                    }
                    bottom_group.setLayoutParams(par_bottom_group);
                    bottom_group.requestLayout();
                }
            });
            if (aContent.indexOf("Lesson 1 of ") != -1 && !sendStart) {
                sendStart = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.show();
                    }
                });
                new CourseLogAction(CourseActivity.this, courseId, Environment.AccountCourseFileStatus).execute();
            }
        }
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
            view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");
            //view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByClassName('overview__button'));");
            pd.hide();
            /*
            int lastIndex = url.lastIndexOf("/");
            int lastIndex2 = url.lastIndexOf("?");
            if (lastIndex != -1 && lastIndex2 != -1) {
                int numPage = 0;
                try {
                    numPage = Integer.parseInt(url.substring(lastIndex + 1, lastIndex2));
                } catch (Exception e) {}
                if (numPage > 0) {
                    pd.show();
                    new CourseLogAction(CourseActivity.this, courseId, Environment.AccountCourseFileStatus).execute();
                }
            }
            */
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    }

    @Override
    public void onRequestCourses(String result) {
        if (result.indexOf("index.html") == -1) {
            pd.hide();
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();
            return;
        }

        result = result.replaceAll("\"","");
        result = result.replaceAll("\\\\","/");
        result = result.replaceAll("//","/");
        result = result.replaceAll(" ","%20");

        //webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(Environment.SERVERFull + result);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.hide();
            }
        }, 10000);
    }

    @Override
    public void onRequestCoursesData(ArrayList<CoursesData> coursesDatas) {
        //File sdPath = new File(Sapphire.getInstance().getFilesDir() + "/temp/temp.html");
        //File f = new File(android.os.Environment.getExternalStorageDirectory() + "/Download/temp.html");
        //webView.loadUrl("file://"+sdPath.getAbsolutePath());

        //File f = new File(android.os.Environment.getExternalStorageDirectory() + "/Download/index.html");
        //webView.loadUrl("file://"+f.getAbsolutePath());
        //webView.loadUrl("http://portal.dealerpilothr.com/api/CourseFile/Get/d10fdc39-182e-cb17-b3b1-8b967cffca91");
        //
        //pd.hide();
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
