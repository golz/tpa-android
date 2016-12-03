package com.michelle.goldwin.tpamobile.googlemaps;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michelle.goldwin.tpamobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleMapsFragment extends Fragment{

    /**
     * TODO: API Google Maps
     */
    public GoogleMapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_google_maps, container, false);
    }

}
