package com.example.filippo.sunshine;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MeteoActivity extends Activity implements MeteoFragment.OnFragmentInteractionListener {

    protected String[] generateData(int num) {

        String[] base = { "Oggi - Bello",
                "Domani - Bellino",
                "Martedi - Orrendo",
                "Mercoledì - Carino"};

        String[] result = new String[num];
        for (int i =0; i < num; i++)
            result[i] = base[i % base.length];

        return result;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteo);

        ListView listview = (ListView)findViewById(R.id.meteo_list_view);

       String[] data = generateData(15);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.meteo_list_item,R.id.meteo_item_row,data);
        listview.setAdapter(arrayAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meteo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
