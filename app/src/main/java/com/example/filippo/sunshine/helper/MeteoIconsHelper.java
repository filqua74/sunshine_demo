package com.example.filippo.sunshine.helper;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Hashtable;

/**
 * Created by filippo on 06/03/2015.
 */
public class MeteoIconsHelper {

    static Hashtable<String,Bitmap> meteoIcons = null;

    static {
        meteoIcons = new Hashtable<String,Bitmap>();
    }

    static public void putMeteoImage(String imageId, Bitmap icon) {
        meteoIcons.put(imageId, icon);
        Log.i("sunshine","Added icon for " + icon);
    }

    static public Bitmap getMeteoImage(String imageId) {
        return meteoIcons.get(imageId);
    }

}
