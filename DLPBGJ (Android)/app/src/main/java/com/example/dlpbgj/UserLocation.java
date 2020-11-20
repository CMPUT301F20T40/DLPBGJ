//UserLovcation
package com.example.dlpbgj;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class UserLocation extends FragmentActivity implements OnMapReadyCallback {
    LocationManager LocM;
    LocationListener LocL;
    LatLng UserLongLat;
    GoogleMap.OnMapClickListener Dropoffpt;
    private GoogleMap mMap;
    private Geocoder geocoder;
    String temp;
    //System.out.print(temp);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
        temp = (String)getIntent().getSerializableExtra("Address");
        System.out.print(temp);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        askLocationPermision();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    if(addressList.size() > 0){
                        Address tempAddress = addressList.get(0);
                        String tempAddressString = tempAddress.getAddressLine(0);
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(tempAddressString)
                                .draggable(true)
                        );
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) { }
            @Override
            public void onMarkerDrag(Marker marker) { }
            @Override
            public void onMarkerDragEnd(Marker marker) {
            LatLng templatlng = marker.getPosition();
                try {
                    List<Address> addressList = geocoder.getFromLocation(templatlng.latitude,templatlng.longitude,1);
                    if(addressList.size() > 0){
                        Address tempAddress = addressList.get(0);
                        String tempAddressString = tempAddress.getAddressLine(0);
                        marker.setTitle(tempAddressString);
                        /*mMap.addMarker(new MarkerOptions()
                                .position(templatlng)
                                .title(tempAddressString)
                                .draggable(true)
                        );*/
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void askLocationPermision() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    if(temp!=NULL) {
                        List<Address> geoaddress = geocoder.getFromLocationName(temp, 1);
                        if (geoaddress.size() > 0) {
                            Address address = geoaddress.get(0);
                            LatLng newLoc = new LatLng(address.getLatitude(), address.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(new LatLng(address.getLatitude(), address.getLongitude()))
                                    .title(address.getLocality());
                            mMap.addMarker(markerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLoc, 16));
                        }
                    }
                    else {
                        LocM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, LocL);
                        Location UserLastLocation = LocM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        UserLongLat = new LatLng(UserLastLocation.getLatitude(), UserLastLocation.getLongitude());
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(UserLongLat).title("Your Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(UserLongLat));
                        SupportMapFragment NewLocation = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) { }
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) { }
        }).check();
    }
}