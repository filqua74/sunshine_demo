package com.example.filippo.sunshine.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.filippo.sunshine.helper.MeteoIconsHelper;
import com.example.filippo.sunshine.R;
import com.example.filippo.sunshine.model.MeteoInfo;
import com.example.filippo.sunshine.model.MeteoModel;

/**
 * Created by filippo on 06/03/2015.
 */
public class MeteoListAdapter extends BaseAdapter {

    MeteoModel myModel;
    Context context;

    public MeteoListAdapter(Context context, MeteoModel model) {
        this.myModel = model;
        this.context = context;
    }

    @Override
    public int getCount() {
        return myModel.getMeteoInfos().size();
    }

    @Override
    public Object getItem(int position) {
        return myModel.getMeteoInfos().get(position);
    }

    @Override
    public long getItemId(int position) {
        return myModel.getMeteoInfos().get(position).hashCode();
    }

    @Override
    public View getView(int position, View v, ViewGroup vg) {
        {
            if (v==null)
            {
                v= LayoutInflater.from(context).inflate(R.layout.meteo_list_item, null);
            }
            MeteoInfo mi=(MeteoInfo) getItem(position);
            TextView txt=(TextView) v.findViewById(R.id.meteo_item_row);
            txt.setText(mi.toString());
            ImageView img = (ImageView) v.findViewById(R.id.meteo_image_row);
            Bitmap bm = MeteoIconsHelper.getMeteoImage(mi.getIcon());
            if (bm==null) {
                img.setImageResource(R.mipmap.ic_launcher);
            } else {
                img.setImageBitmap(MeteoIconsHelper.getMeteoImage(mi.getIcon()));
            }
            return v;
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
