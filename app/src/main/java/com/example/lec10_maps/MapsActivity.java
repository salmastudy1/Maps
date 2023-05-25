package com.example.lec10_maps;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.lec10_maps.databinding.ActivityMapsBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
           GoogleApiClient.OnConnectionFailedListener,
           LocationListener,
           GoogleMap.OnMapClickListener, ResultCallback<Status> {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private GeofencingClient geofencingClient;
    Marker ASU_Arch, ASU_Comp,ASU_Mech;

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest = new LocationRequest();
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;

    public MapsActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        geofencingClient = LocationServices.getGeofencingClient(this);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override

    public void onPause(){
        super.onPause();

        //stop loaction updates when activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);

        }
    }
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Architecture = new LatLng(30.064182843213935, 31.280700003775685);
        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(Architecture);
        markerOptions1.title("Architecture Building Location");
        ASU_Arch = mMap.addMarker(markerOptions1);

        LatLng Computer = new LatLng(30.06537721049468, 31.278608823333737);
        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(Computer);
        markerOptions2.title("Computer Building Location");
        ASU_Arch = mMap.addMarker(markerOptions2);

        LatLng Mechatronics = new LatLng(30.06319426355498, 31.278903921063197);
        MarkerOptions markerOptions3 = new MarkerOptions();
        markerOptions3.position(Mechatronics);
        markerOptions3.title("Mechatronics Building Location");
        ASU_Arch = mMap.addMarker(markerOptions3);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(Mechatronics));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(Architecture.latitude, Architecture.longitude), new LatLng(Mechatronics.latitude, Mechatronics.longitude))
                .width(5)
                .color(Color.RED));

        Polyline line2 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(Mechatronics.latitude, Mechatronics.longitude), new LatLng(Computer.latitude, Computer.longitude))
                .width(5)
                .color(Color.RED));

        Polyline line3 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(Architecture.latitude, Architecture.longitude), new LatLng(Computer.latitude, Computer.longitude))
                .width(5)
                .color(Color.RED));

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){

                //LOCATION PERMISSION ALREADY GRANTED

                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else{
                //request location permission
                checkLocationPermission();

            }


        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }


    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //THE LAST LOCATION IN THE LIST IS THE NEWEST
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location:" + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                // move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

            }
            Log.d("currentloc", mCurrLocationMarker.getPosition().toString());
            float[] distance = new float[1];
            Location.distanceBetween(mCurrLocationMarker.getPosition().latitude, mCurrLocationMarker.getPosition().longitude, ASU_Comp.getPosition().latitude, ASU_Comp.getPosition().longitude, distance);
            if (distance[0] < 100) {
                ASU_Comp = mGoogleMap.addMarker(new MarkerOptions().position(ASU_Comp.getPosition()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else {
                ASU_Comp = mGoogleMap.addMarker(new MarkerOptions().position(ASU_Comp.getPosition()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
            Location.distanceBetween(mCurrLocationMarker.getPosition().latitude, mCurrLocationMarker.getPosition().longitude, ASU_Arch.getPosition().latitude, ASU_Arch.getPosition().longitude, distance);
            if (distance[0] < 100) {
                ASU_Arch = mGoogleMap.addMarker(new MarkerOptions().position(ASU_Arch.getPosition()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else {
                ASU_Arch = mGoogleMap.addMarker(new MarkerOptions().position(ASU_Arch.getPosition()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
            Location.distanceBetween(mCurrLocationMarker.getPosition().latitude, mCurrLocationMarker.getPosition().longitude, ASU_Mech.getPosition().latitude, ASU_Mech.getPosition().longitude, distance);
            if (distance[0] < 100) {
                ASU_Mech = mGoogleMap.addMarker(new MarkerOptions().position(ASU_Mech.getPosition()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else {
                ASU_Mech = mGoogleMap.addMarker(new MarkerOptions().position(ASU_Mech.getPosition()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }

        }


    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            //should we show an explanation
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the location permission, please accept to ues location functionality")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            }else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);



            }
        }
    }

    public void onRequestsPermissionResult(int requestCode,
                                           String permissions[], int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION:{
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);

                    }
                }else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();

                }
                return;

            }
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }



    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
    @Override
    public void onResult(@NonNull Status status) {

    }
    private void changemarkertogreen(Marker ASU_marker){
        ASU_marker=mMap.addMarker(
                new MarkerOptions()
                        .position(ASU_marker.getPosition())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

    }
    private void changemarkertored(Marker ASU_marker){
        ASU_marker=mMap.addMarker(
                new MarkerOptions()
                        .position(ASU_marker.getPosition())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

    }
}