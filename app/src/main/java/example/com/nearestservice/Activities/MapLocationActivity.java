package example.com.nearestservice.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import example.com.nearestservice.models.Service;
import example.com.nearestservice.dialog_boxes.GPS_And_WiFi_Dialog_Box;
import example.com.nearestservice.Fragments.AddServiceFragment;
import example.com.nearestservice.Info.Constants;
import example.com.nearestservice.R;


public class MapLocationActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        AddServiceFragment.OnFragmentInteractionListener {

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private Marker mCurrLocationMarker;

    private Boolean isConnectedToInternet = false;
    private BroadcastReceiver br;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_location_activity);

        Firebase.setAndroidContext(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        createFragment();
        checkForNetworkInfo();
    }


    private void checkForNetworkInfo() {

        if (br == null) {

            br = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {

                    Bundle extras = intent.getExtras();

                    NetworkInfo info = extras
                            .getParcelable("networkInfo");

                    NetworkInfo.State state = null;
                    if (info != null) {
                        state = info.getState();
                    }


                    if (state == NetworkInfo.State.CONNECTED) {
                        //Toast.makeText(getApplicationContext(), "Internet connection is On", Toast.LENGTH_LONG).show();
                        isConnectedToInternet = true;

                    } else {
                        //Toast.makeText(getApplicationContext(), "Internet connection is Off", Toast.LENGTH_LONG).show();
                        isConnectedToInternet = false;
                        GPS_And_WiFi_Dialog_Box gpsAndWiFiDialogBox = new GPS_And_WiFi_Dialog_Box(MapLocationActivity.this, "WIFI");
                        gpsAndWiFiDialogBox.show();
                        /*InternetDialogBox internetDialogBox = new InternetDialogBox(MapLocationActivity.this);
                        internetDialogBox.show();*/
                    }

                }
            };

            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver( br, intentFilter);
        }
    }


    private void createFragment() {

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        Fragment addServiceFragment = new AddServiceFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout_for_addServiceFragment, addServiceFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onPause() {
        super.onPause();

        unregisterReceiver(br);
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);

            }
        } else {
            buildGoogleApiClient();
            //ToDO commented
            //mGoogleMap.setMyLocationEnabled(true);
        }

        LatLng latLng = new LatLng(0, 0);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        markerOptions.draggable(true);
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);


        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mCurrLocationMarker.setPosition(latLng);
                if (isConnectedToInternet) {
                    findAddress();
                }
            }
        });


        mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                mCurrLocationMarker.setPosition(marker.getPosition());
                if (isConnectedToInternet) {
                    findAddress();
                }
            }
        });
    }

    private void findAddress() {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = new ArrayList<>();

        double latitude = mCurrLocationMarker.getPosition().latitude;
        double longitude = mCurrLocationMarker.getPosition().longitude;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (addresses.size() != 0) {
            AddServiceFragment.addressDetected(uniqueChecker(new String[]{addresses.get(0).getAddressLine(0),
                    addresses.get(0).getLocality(), addresses.get(0).getAdminArea(),
                    addresses.get(0).getCountryName()}));
        }


    }

    private String[] nullChecker(String[] strings) {
        for (int i = 0; i < strings.length; ++i) {

            if (strings[0].equals("Unnamed Road")) {
                strings[0] = "";
            }
            if (strings[i] == null) {
                strings[i] = "";

            }
        }
        return strings;
    }

    private String[] uniqueChecker(String[] strings) {
        strings = nullChecker(strings);
        for (int i = 0; i + 1 < strings.length; ++i) {
            if (!(strings[i].equals("")) && strings[i].equals(strings[i + 1])) {
                strings[i] = "";
            }
        }
        return strings;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mCurrLocationMarker.setPosition(latLng);
        if (isConnectedToInternet) {
            findAddress();
        }

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL));

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        //ToDO commented
                        //mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                //ToDO commented
                //return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void cancelButtonOnAddFragmentPressed() {
        finish();
    }

    @Override
    public void addButtonOnAddFragmentPressed(String serviceIndex, String[] params) {

        params = nullChecker(params);
//        serviceIndex = imageResourceGenerator(serviceIndex);
//        Service service = new Service(params[0],params[1], params[2],
//                mCurrLocationMarker.getPosition().latitude,
//                mCurrLocationMarker.getPosition().longitude, serviceIndex);

               Service service = new Service(params[0],
                params[2],params[1],2,mCurrLocationMarker.getPosition().latitude, mCurrLocationMarker.getPosition().longitude, serviceIndex);

//        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        RealmResults rel = realm.where(Service.class).findAll();
//        int id = 1;
//        if (rel.size() != 0) {
//            Service serviceFromDB = (Service) rel.last();
//            id = serviceFromDB.getId() + 1;
//        }
//        service.setId(id);
//        realm.copyToRealm(service);
//        realm.commitTransaction();
        //TODO Nare ete harmar es gtnum avelacra
        Firebase firebase = new Firebase(Constants.FIREBASE_URL).child("services");
        firebase.push().setValue(service);
        //Toast.makeText(MapLocationActivity.this, params[0]+ "barehajox avelacvel a", Toast.LENGTH_SHORT).show();
        finish();

    }

    private int imageResourceGenerator(int serviceIndex) {
        switch (serviceIndex){
            case 0:
                return Constants.AUTO_SERVICE_INDEX;
            case 1:
                return Constants.BEAUTY_SALON_INDEX;
            case 2:
                return Constants.FAST_FOOD_INDEX;
            case 3:
                return Constants.PHARMACY_INDEX;
            case 4:
                return Constants.PHOTO_INDEX;
            case 5:
                return Constants.SHOP_INDEX;
            case 6:
                return Constants.TAILOR_INDEX;
            case 7:
                return Constants.WATCHMAKER_INDEX;
            default:
                throw new RuntimeException("unknown serviceIndex in imageResourceGenerator");
        }
    }


}


