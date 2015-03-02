package com.example.filippo.sunshine;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by filippo on 02/03/2015.
 */
public class MeteoAsyncTask extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... urls) {
        URL url = null;
        HttpURLConnection conn = null;

        try {
            url = new URL(urls[0]);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ( (line=reader.readLine())!=null) {
                Log.i("sunshine", "Letto: " + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("sunshine",e.getMessage());
        } finally {
            if (conn!=null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("sunshine",e.getMessage());
                }
            }
        }
        return null;
    }
}
