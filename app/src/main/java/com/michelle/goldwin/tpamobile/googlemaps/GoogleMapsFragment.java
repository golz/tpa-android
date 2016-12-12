package com.michelle.goldwin.tpamobile.googlemaps;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.michelle.goldwin.tpamobile.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleMapsFragment extends Fragment implements LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    /* Define Final */
    private final int PROXIMITY_RADIUS = 10000;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private Button btnGym;

    /* Map and API Cleint*/
    private MapView mapView;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;

    /* Coordinates */
    private double latitude;
    private double longitude;

    /* Location Variable*/
    private Location lastLocation;
    private Marker currentLocationMarker;
    private LocationRequest locationRequest;

    /* Target Points */
    private String searchWhat = "gym";
    private Marker selectedMarker;

    /* PolyLine */
    private ArrayList<LatLng> arrayPoints = null;
    private PolylineOptions polylineOptions;
    private Polyline line;

    public GoogleMapsFragment() {}

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getContext());
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result,
                        0).show();
            }
            return false;
        }
        return true;
    }
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_google_maps, container, false);

        /* BEGIN CHECKING */
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkLocationPermission();

        if(!CheckGooglePlayServices())
            Toast.makeText(getContext(), "No Google Services", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), "Google Services available", Toast.LENGTH_SHORT).show();
        /* END CHECKING */

        /* BEGIN INITIALIZE */
        btnGym = (Button) rootView.findViewById(R.id.btnGym);
        arrayPoints = new ArrayList<>();
        /* END INITIALIZE */

        /* BEGIN GOOGLE MAPS */
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap gMap) {
                googleMap = gMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                /* Check LOCATION_ACCESS from user's phone is allowed */
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);

                /* BEGIN ACTION */
                btnGym.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnGym.setText("Scan for Nearby Gym");
                        googleMapGetPlace(searchWhat);
                    }
                });
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        googleGetDirection(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()),marker.getPosition());
                        selectedMarker = marker;
                        selectedMarker.showInfoWindow();
                        btnGym.setText(selectedMarker.getTitle().toString());
                        return true;
                    }
                });

                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        btnGym.setText("Scan for Nearby Gym");
                        googleMap.clear();
                        arrayPoints.clear();
                    }
                });
                /* END ACTION */
            }
        });
        /* END GOOGLE MAPS */
        return rootView;
    }
    /* BEGIN GOOGLE MAPS NEARBY PLACE */
    private void googleMapGetPlace(String place)
    {
        googleMap.clear();
        arrayPoints.clear();

        String url = getUrl(latitude,longitude,place);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = googleMap;
        DataTransfer[1] = url;
        GetNearbyPlacesData getNearbyPlaceData = new GetNearbyPlacesData();
        getNearbyPlaceData.execute(DataTransfer);
    }
    private String getUrl(double latitude,double longitude,String place)
    {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + (double)latitude + "," + (double)longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + place);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyBVDZt8YsMPqOTRP5PgIVMtnoS2R9L5pWY");
        return googlePlacesUrl.toString();
    }
    /* END GOOGLE MAPS NEARBY PLACE */

    /*
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * l o l * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    */


    /* BEGIN GOOGLE MAPS POLYLINE */
    private LatLng startLatLng;
    private LatLng endLatLng;
    private void googleGetDirection(LatLng origin, LatLng destination)
    {
        googleMapGetPlace(searchWhat);

        String url = getPoly(origin,destination);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = googleMap;
        DataTransfer[1] = url;
        GetPolylinePlacesData  getPolylinePlacesData = new GetPolylinePlacesData();
        getPolylinePlacesData.execute(DataTransfer);

        startLatLng = origin;
        endLatLng   = destination;
    }
    private String getPoly(LatLng origin,LatLng destination)
    {
        StringBuilder googlePlacesUrl = new StringBuilder("http://maps.googleapis.com/maps/api/directions/json?");
        googlePlacesUrl.append("origin=" + origin.latitude + "," + origin.longitude);
        googlePlacesUrl.append("&destination=" + destination.latitude + "," + destination.longitude);
        googlePlacesUrl.append("&sensor=false");
        return googlePlacesUrl.toString();
    }
    private class GetPolylinePlacesData extends AsyncTask<Object, String, String> {
        private ProgressDialog progressDialog;
        private String googlePlacesData;
        private GoogleMap googleMap;
        private String url;

        GetPolylinePlacesData() {
        }

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
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Fetching routes...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if (result != null) {
                drawPath(result);
            }
        }
        public void drawPath(String result) {
            //googleMap.addMarker(new MarkerOptions().position(startLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            googleMap.addMarker(new MarkerOptions().position(endLatLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            try {
                final JSONObject json = new JSONObject(result);
                JSONArray routeArray = json.getJSONArray("routes");
                JSONObject routes = routeArray.getJSONObject(0);
                JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                String encodedString = overviewPolylines.getString("points");
                List<LatLng> list = decodePoly(encodedString);
                //Repeater
                PolylineOptions options = new PolylineOptions().width(5).color(getContext().getResources().getColor(R.color.colorPrimary));
                for (int z = 0; z < list.size(); z++) {
                    LatLng point = list.get(z);
                    options.add(point);
                }
                line = googleMap.addPolyline(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private List<LatLng> decodePoly(String encoded) {
            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
    }
    /* END GOOGLE MAPS POLYLINE */

    /* IGNORE ALL METHODS THAT PROVIDED BELOW */
    @Override
    public void onLocationChanged(Location location) {
        //Remove Old Marker
        lastLocation = location;

        if(currentLocationMarker != null)
            currentLocationMarker.remove();

        /* IF LOCATION IN CHANGED, DONT FORGET TO SET LATITUDE AND LONGITUDE COORDINATES*/
        latitude    = (double)location.getLatitude();
        longitude   = (double)location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
//        currentLocationMarker = googleMap.addMarker(markerOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        if(googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
            //Toast.makeText(getContext(), "Location Changed", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
       //Toast.makeText(getContext(), "Connected", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConnectionSuspended(int i) {}
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}
