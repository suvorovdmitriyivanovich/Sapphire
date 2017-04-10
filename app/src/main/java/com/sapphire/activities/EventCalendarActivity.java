package com.sapphire.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.sapphire.R;
import com.sapphire.Sapphire;
import com.sapphire.api.GetCourseFileAction;
import com.sapphire.api.UpdateAction;
import com.sapphire.logic.Environment;
import com.sapphire.logic.NetRequests;
import com.sapphire.logic.UserInfo;
import com.sapphire.models.CoursesData;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventCalendarActivity extends BaseActivity implements GetCourseFileAction.RequestCourses,
                                                                   GetCourseFileAction.RequestCoursesData,
                                                                   UpdateAction.RequestUpdate{
    private WebView webView;
    private ProgressDialog pd;
    private boolean needClose = false;
    private BroadcastReceiver br;
    private View nointernet_group;
    private ViewGroup.LayoutParams par_nointernet_group;

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

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "INTERFACE");
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());

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

        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.hide();
            }
        }, 15000);
        */

        nointernet_group = findViewById(R.id.nointernet_group);
        par_nointernet_group = nointernet_group.getLayoutParams();
        nointernet_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                new UpdateAction(EventCalendarActivity.this);
            }
        });

        UpdateBottom();

        pd.show();
        //Map<String, String> extraHeaders = new HashMap<String, String>();
        //extraHeaders.put("X-YAUTH", UserInfo.getUserInfo().getAuthToken());
        //webView.loadUrl("http://portal.dealerpilothr.com/me/my-calendar",extraHeaders);
        //webView.loadUrl("http://portal.dealerpilothr.com");
        //webView.loadUrl("https://www.google.com.ua");
        //webView.loadUrl("http://portal.dealerpilothr.com/me/my-calendar");
        //Map<String, String> extraHeaders = new HashMap<String, String>();
        //extraHeaders.put("x-yauth", UserInfo.getUserInfo().getAuthToken());
        //webView.loadUrl("http://portal.dealerpilothr.com/mobile/me/my-calendar");

        //CookieManager cookieManager = CookieManager.getInstance();
        //cookieManager.setAcceptCookie(true);
        //
        //List<Cookie> cookies = WSHelper.cookieStore.getCookies();

        /*
        List<Cookie> cookies = WSHelper.cookieStore.getCookies();

        cookieManager.removeAllCookie();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().contains("session")){
                    String cookieString = cookie.getName() + "=" + cookie.getValue() + "; Domain=" + cookie.getDomain();
                    cookieManager.setCookie(cookie.getDomain(), cookieString);
                    Log.d("CookieUrl",cookieString + " ");
                }
            }
        }
        */
        //webView.getSettings().setAppCacheEnabled(true);

        //webView.loadUrl("http://portal.dealerpilothr.com");

        //Map<String, String> extraHeaders = new HashMap<String, String>();
        //extraHeaders.put("x-yauth", UserInfo.getUserInfo().getAuthToken());
        //webView.loadUrl("http://portal.dealerpilothr.com",extraHeaders);

        /*
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSaveFormData(false);
        webView.getSettings().setDomStorageEnabled(true);

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("x-yauth", UserInfo.getUserInfo().getAuthToken());

        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view,String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("http://portal.dealerpilothr.com?auth="+UserInfo.getUserInfo().getAuthToken(), map);
        */

        /*
        webView.getSettings().setSaveFormData(false);
        webView.getSettings().setDomStorageEnabled(true);
        //webView.getSettings().setDatabaseEnabled(true);
        //String databasePath = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //webView.getSettings().setDatabasePath(databasePath);

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        webView.getSettings().setAppCacheEnabled(true);
        cookieManager.setCookie("name", "_ya");
        cookieManager.setCookie("value", UserInfo.getUserInfo().getAuthToken());
        cookieManager.setCookie("domain", "dealerpilothr.com");
        cookieManager.setCookie("path", "/");
        */
        //cookieManager.setCookie("expires", System.currentTimeMillis() + );
        //webView.loadUrl("http://portal.dealerpilothr.com?auth="+UserInfo.getUserInfo().getAuthToken());
        //webView.loadUrl("http://portal.dealerpilothr.com/mobile/me/my-calendar?auth="+UserInfo.getUserInfo().getAuthToken());

        webView.getSettings().setDomStorageEnabled(true);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("x-yauth", UserInfo.getUserInfo().getAuthToken());
        //webView.loadUrl("javascript:localStorage.setItem('accountSession', " + UserInfo.getUserInfo().getAccountSession() + ");");
        webView.loadUrl("http://portal.dealerpilothr.com/mobile/me/my-calendar", map);
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

    private class MyJavaScriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        public void processContent(final String aContent) {
            int i = 0;
            if (i == 0) {
                i = 1;
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //view.loadUrl("javascript:localStorage.setItem('accountSession', " + UserInfo.getUserInfo().getAccountSession() + ");");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");
            pd.hide();
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
        unregisterReceiver(br);
    }
}
