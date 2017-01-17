package com.sapphire.logic;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.sapphire.R;
import com.sapphire.Sapphire;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetRequests {

    //---------------------Singleton---------------------------
    private static NetRequests netRequests;
    private NetRequests() {}
    public static NetRequests getNetRequests() {
        if(netRequests == null)
            netRequests = new NetRequests();
        return netRequests;
    }
    //---------------------------------------------------------

    public String SendRequestCommon(String request, int timout, boolean returnerror) {
        String rez = "";

        if (timout == 0) {
            timout = 20000;
        }

        try {
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset","utf-8");
            //conn.setRequestProperty("Authorization", "app-token=nbJyew52");
            //conn.setRequestProperty("app-token","nbJyew52");
            //conn.setRequestProperty("login","suvdima");
            //conn.setRequestProperty("password","123");
            conn.setConnectTimeout(timout);
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                //BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "iso-8859-1"), 8);
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"), 8);
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                rez = response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (!returnerror) {
                rez = e.getMessage();
                if (rez == null) {
                    rez = "";
                }
                if (rez.indexOf("after") != -1) {
                    rez = Sapphire.getInstance().getResources().getString(R.string.text_timeaut);
                }
            }
        }

        return rez;
    }

    public String SendRequestGoogle(String request, int timout) {
        String rez = "";

        if (timout == 0) {
            timout = 20000;
        }

        try {
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setConnectTimeout(timout);
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                rez = "1";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rez;
    }

    public boolean isOnline(boolean checkGoogle) {
        boolean online = false;

        ConnectivityManager cm =
                (ConnectivityManager) Sapphire.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        online  = netInfo != null && netInfo.isConnectedOrConnecting();

        if (online && checkGoogle) {
            if (!SendRequestGoogle("http://google.com/", 10000).equals("1")) {
                online = false;
            }
        }

        return online;
    }
}