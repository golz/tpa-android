package com.michelle.goldwin.tpamobile.googlemaps;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
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
import com.michelle.goldwin.tpamobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleMapsFragment extends Fragment implements LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    private Button btnGym;

    private MapView mapView;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;

    private double latitude;
    private double longitude;
    private final int PROXIMITY_RADIUS = 10000;
    private Location lastLocation;
    private Marker currentLocationMarker;
    private LocationRequest locationRequest;

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
        if(!CheckGooglePlayServices())
            Toast.makeText(getContext(), "No Google Services", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), "Google Services available", Toast.LENGTH_SHORT).show();
        /* END CHECKING */

        /* BEGIN INITIALIZE */
        btnGym = (Button) rootView.findViewById(R.id.btnGym);
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
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

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
                        googleMapGetPlace("university");
                    }
                });
                /* END ACTION */
            }
        });
        /* END GOOGLE MAPS */
        return rootView;
    }
    private void googleMapGetPlace(String place)
    {
        googleMap.clear();
        String url = getUrl(latitude,longitude,place);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = googleMap;
        DataTransfer[1] = url;
        GetNearbyPlacesData getNearbyPlaceData = new GetNearbyPlacesData();
        getNearbyPlaceData.execute(DataTransfer);
        Toast.makeText(getContext(), "Success get nearby " + place + " within : " + latitude + "," + longitude, Toast.LENGTH_LONG).show();
    }
    private String getUrl(double latitude,double longitude,String place)
    {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" +latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + place);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyBVDZt8YsMPqOTRP5PgIVMtnoS2R9L5pWY");
        Toast.makeText(getContext(), googlePlacesUrl.toString(), Toast.LENGTH_SHORT).show();
        return googlePlacesUrl.toString();
    }

    /* IGNORE ALL METHODS THAN PROVIDED BELOW */

    /**
     * Animate the camera when location is changed
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        //Remove Old Marker
        lastLocation = location;

        if(currentLocationMarker != null)
            currentLocationMarker.remove();

        /* IF LOCATION IN CHANGED, DONT FORGET TO SET LATITUDE AND LONGITUDE COORDINATES*/
        latitude    = location.getLatitude();
        longitude   = location.getLatitude();
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currentLocationMarker = googleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,20);
        googleMap.animateCamera(cameraUpdate);

        if(googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
            Toast.makeText(getContext(), "Location Changed", Toast.LENGTH_SHORT).show();
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
