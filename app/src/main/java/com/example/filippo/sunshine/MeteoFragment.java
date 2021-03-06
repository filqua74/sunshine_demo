package com.example.filippo.sunshine;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.filippo.sunshine.adapter.MeteoListAdapter;
import com.example.filippo.sunshine.helper.MeteoIconsHelper;
import com.example.filippo.sunshine.helper.MeteoImageAsyncTask;
import com.example.filippo.sunshine.model.MeteoInfo;
import com.example.filippo.sunshine.model.MeteoModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeteoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MeteoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeteoFragment extends Fragment implements MeteoAsyncTask.IApiAccessResponse,
                                                       MeteoImageAsyncTask.IApiAccessResponse {

    MeteoModel model = null;
    ListView listView = null;
    MeteoListAdapter adapter = null;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
    */
    // TODO: Rename and change types and number of parameters
    public static MeteoFragment newInstance(String param1, String param2) {
        MeteoFragment fragment = new MeteoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MeteoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        model = new MeteoModel();

        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meteo_fragment, container,false);
        listView = (ListView)view.findViewById(R.id.meteo_list_view);

        adapter = new MeteoListAdapter(this.getActivity(), model);
        //adapter.setNotifyOnChange(true);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*String message = "Clicked an item..." + position;
                Toast toast = Toast.makeText(MeteoFragment.this.getActivity(),
                                             message,
                                             Toast.LENGTH_SHORT);
                toast.show();*/
                TextView tv = (TextView)view.findViewById(R.id.meteo_item_row);
                String selected = tv.getText().toString();

                Intent detailIntent = new Intent(getActivity(), MeteoDetailActivity.class);
                detailIntent.putExtra(MeteoDetailActivity.EXTRA_MESSAGE, selected);
                getActivity().startActivity(detailIntent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.meteo_fragment_menu,menu);
    }

    protected void updateWeather() {
        MeteoAsyncTask myAsync = new MeteoAsyncTask(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        String location = sp.getString(getString(R.string.pref_location_key),null);
        if (location!=null) {
            myAsync.execute(location);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                //Log.i("sunshine","Premuto refresh");
                this.updateWeather();
                return true;
            case R.id.action_map:
                this.openPreferredLocationInMap();
                return true;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void postResult(Object[] asyncresult) {
        String key = (String)asyncresult[0];
        Bitmap bm = (Bitmap)asyncresult[1];
        MeteoIconsHelper.putMeteoImage(key,bm);
        adapter.notifyDataSetChanged();
    }


    private void openPreferredLocationInMap() {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = sharedPrefs.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));

        // Using the URI scheme for showing a location found on a map.  This super-handy
        // intent can is detailed in the "Common Intents" page of Android's developer site:
        // http://developer.android.com/guide/components/intents-common.html#Maps
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("sunshine", "Couldn't call " + location + ", no receiving apps installed!");
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(MeteoInfo[] values);
    }

    public void postResult(String result) {
        try {
            Log.i("sunshine","Called the delegate");

            MeteoInfo[] items = MeteoJsonParser.getWeatherDataFromJson(this, result,7);

            model.clear();
            for (MeteoInfo s : items ) {
                Log.i("sunshine","Added " + s.toString());
                model.addMeteoInfo(s);
                if (MeteoIconsHelper.getMeteoImage(s.getIcon())==null) {
                    // Try to retrieve icons
                    Log.i("sunshine","Tentativo di recuperare " + s.getIcon());
                    MeteoImageAsyncTask myAsync = new MeteoImageAsyncTask(this);
                    myAsync.execute(s.getIcon());
                }
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            Log.e("sunshine",e.getMessage());
        }
    }

}
