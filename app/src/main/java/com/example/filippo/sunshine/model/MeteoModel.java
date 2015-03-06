package com.example.filippo.sunshine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filippo on 06/03/2015.
 */
public class MeteoModel {

    List<MeteoInfo> meteoInfos = null;

    public MeteoModel() {
        meteoInfos = new ArrayList<MeteoInfo>();
    }

    public synchronized  void clear() {
        meteoInfos.clear();
    }

    public synchronized void addMeteoInfo(MeteoInfo meteoInfo) {
        meteoInfos.add(meteoInfo);
    }

    public synchronized List getMeteoInfos() {
        return meteoInfos;
    }


}
