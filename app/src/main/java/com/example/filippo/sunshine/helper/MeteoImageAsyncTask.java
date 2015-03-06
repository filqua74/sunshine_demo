package com.example.filippo.sunshine.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by filippo on 02/03/2015.
 */
public class MeteoImageAsyncTask extends AsyncTask<String,Void,Object[]> {

    final String IMAGE_BASE_URL = "http://openweathermap.org/img/w/";

    IApiAccessResponse delegate=null;

    public interface IApiAccessResponse {
        void postResult(Object[] asyncresult);
    }

    public MeteoImageAsyncTask(IApiAccessResponse delegate) {
        super();
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(Object[] result) {
        if(delegate!=null)
        {
            delegate.postResult(result);
        }
        else
        {
            Log.e("ApiAccess", "You have not assigned IApiAccessResponse delegate");
        }
    }


    // Creates Bitmap from InputStream and returns it
    private Bitmap downloadImage(InputStream stream) {
        Bitmap bitmap = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
            stream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected Object[] doInBackground(String... imageIds) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream stream = null;
        Object[] result = null;

        try {
            Uri myUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                                                 .appendPath(imageIds[0]+".png")
                                                 .build();
            Log.i("sunshine","Image uri=" + myUri.toString());
            url = new URL(myUri.toString());
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }

            result = new Object[] {imageIds[0],downloadImage(stream)};


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
