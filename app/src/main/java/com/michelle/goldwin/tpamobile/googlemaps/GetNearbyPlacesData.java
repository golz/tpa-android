package com.michelle.goldwin.tpamobile.googlemaps;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gd16-1 on 12/8/2016.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String>{

    private String googlePlacesData;
    private GoogleMap googleMap;
    private String url;

    @Override
    protected String doInBackground(Object... objects) {
        try {
            /* BEGIN INITIALIZE */
            //Log.d("GetNearbyPlacesData","doInBackground Entered");
            googleMap   = (GoogleMap) objects[0];
            url         = (String) objects[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            /* END INITIALIZE */
        }catch (Exception e){

        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> listNearbyPlaces;
        DataParser dataParser = new DataParser();
        listNearbyPlaces = dataParser.parse(s);
        showNearbyPlaces(listNearbyPlaces);
    }

    private void showNearbyPlaces(List<HashMap<String,String>> listNearbyPlaces) {
        for(int i=0;i<listNearbyPlaces.size();i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String,String> googlePlace = listNearbyPlaces.get(i);
            double lat      = Double.parseDouble(googlePlace.get("lat"));
            double lng      = Double.parseDouble(googlePlace.get("lng"));
            String placeName= googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");

            LatLng latLng   = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);

            googleMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }
}
