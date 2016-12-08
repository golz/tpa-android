package com.michelle.goldwin.tpamobile.googlemaps;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gd16-1 on 12/8/2016.
 */

public class DataParser {

    public List<HashMap<String,String>> parse(String jsonData){

        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject  = new JSONObject((String)jsonData);
            jsonArray   = jsonObject.getJSONArray("results");
        }catch (JSONException e){
            e.printStackTrace();
        }

        return getPlaces(jsonArray);
    }

    /* Place */
    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray){
        int placesCount = jsonArray.length();
        List<HashMap<String,String>> listPlaces = new ArrayList<>();
        HashMap<String,String> placeMap = null;

        for(int i=0;i<placesCount;i++)
        {
            try{
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                listPlaces.add(placeMap);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return listPlaces;
    }
    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");
            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
            //Log.d("getPlace","Putting Place");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap;
    }

    /* Poly */
}
