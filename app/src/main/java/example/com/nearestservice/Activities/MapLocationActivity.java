
package example.com.nearestservice.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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

import example.com.nearestservice.Fragments.AddServiceFragment;
import example.com.nearestservice.R;
import example.com.nearestservice.Services.AutoService;
import example.com.nearestservice.Services.BeautySalon;
import example.com.nearestservice.Services.FastFood;
import example.com.nearestservice.Services.Pharmacy;
import example.com.nearestservice.Services.Photo;
import example.com.nearestservice.Services.Shop;
import example.com.nearestservice.Services.Tailor;
import example.com.nearestservice.Services.Watchmaker;
import io.realm.Realm;
import io.realm.RealmResults;


public class MapLocationActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        AddServiceFragment.OnFragmentInteractionListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;

    private LatLng selectedPosition;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    private OnActivityInteractionListener mOnActivityInteractionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.for_suport);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        Fragment addServiceFragment = new AddServiceFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout_for_addServiceFragment, addServiceFragment);
        fragmentTransaction.commit();

        mOnActivityInteractionListener = (OnActivityInteractionListener) mapFrag;//this


    }

    @Override
    public void onPause() {
        super.onPause();

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
            //ToDO AHAHAccccccccccccccccccccccccccccccccccccccccccccccccc
            //mGoogleMap.setMyLocationEnabled(true);
        }
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //mGoogleMap.clear();
                if(mCurrLocationMarker != null)
                mCurrLocationMarker.setPosition(latLng);

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

                selectedPosition = marker.getPosition();
            }
        });
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
        mLocationRequest = new LocationRequest();
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
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        markerOptions.draggable(true);
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

       // selectedPosition = mCurrLocationMarker.getPosition();


        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));



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
                                           String permissions[], int[] grantResults) {
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
                        //ToDO AHAHAccccccccccccccccccccccccccccccccccccccccccccccccc

                        //mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void cancelButtonOnAddFragmentPressed() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        selectedPosition = mCurrLocationMarker.getPosition();

        try {
            addresses = geocoder.getFromLocation(selectedPosition.latitude, selectedPosition.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            addresses = new ArrayList<>();
            Log.e("abov", "exepiti");
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        //String postalCode = addresses.get(0).getPostalCode();
        //String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        Toast.makeText(MapLocationActivity.this, "adress "+address+" city "+city+" state "+state+" country "
                +country, Toast.LENGTH_LONG).show();
        Log.e("abov","adress"+address+"city"+city+"state"+state+"country"+country);
        //finish();

    }

    @Override
    public void addButtonOnAddFragmentPressed(int serviceIndex, String name, String address, String description) {
        selectedPosition = mCurrLocationMarker.getPosition();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults rel;
        int id = 1;
        switch (serviceIndex) {
            case MainActivity.AUTOSERVICE_INDEX:
                //    public AutoService(int id, String name, String description, String address, double rating, double latitude, double longitude) {

                AutoService autoService = new AutoService( name, description, address, selectedPosition.latitude, selectedPosition.longitude);
                rel = realm.where(AutoService.class).findAll();
                if (rel.size() != 0) {
                    AutoService autoServiceFromDB = (AutoService) rel.last();
                    id = autoServiceFromDB.getId() + 1;
                }
                autoService.setId(id);
                realm.copyToRealm(autoService);
                break;
            case MainActivity.BEAUTYSALON_INDEX:
                BeautySalon beautySalon = new BeautySalon( name, description,address, selectedPosition.latitude, selectedPosition.longitude);
                rel = realm.where(BeautySalon.class).findAll();
                if (rel.size() != 0) {
                    BeautySalon beautySalonFromDB = (BeautySalon) rel.last();
                    id = beautySalonFromDB.getId() + 1;
                }
                beautySalon.setId(id);
                realm.copyToRealm(beautySalon);
                break;
            case MainActivity.FASTFOOD_INDEX:
                FastFood fastFood = new FastFood(name, description,address, selectedPosition.latitude, selectedPosition.longitude);
                rel = realm.where(FastFood.class).findAll();
                if (rel.size() != 0) {
                    FastFood fastFoodFromDB = (FastFood) rel.last();
                    id = fastFoodFromDB.getId() + 1;
                }
                fastFood.setId(id);
                realm.copyToRealm(fastFood);
                break;
            case MainActivity.PHARMACY_INDEX:
                Pharmacy pharmacy = new Pharmacy(name, description,address, selectedPosition.latitude, selectedPosition.longitude);
                rel = realm.where(Pharmacy.class).findAll();
                if (rel.size() != 0) {
                    Pharmacy pharmacyFromDB = (Pharmacy) rel.last();
                    id = pharmacyFromDB.getId() + 1;
                }
                pharmacy.setId(id);
                realm.copyToRealm(pharmacy);
                break;
            case MainActivity.PHOTO_INDEX:
                Photo photo = new Photo(name, description,address, selectedPosition.latitude, selectedPosition.longitude);
                rel = realm.where(Photo.class).findAll();
                if (rel.size() != 0) {
                    Photo photoFromDB = (Photo) rel.last();
                    id = photoFromDB.getId() + 1;
                }
                photo.setId(id);
                realm.copyToRealm(photo);
                break;
            case MainActivity.SHOP_INDEX:
                Shop shop = new Shop(name, description,address, selectedPosition.latitude, selectedPosition.longitude);
                rel = realm.where(Shop.class).findAll();
                if (rel.size() != 0) {
                    Shop shopFromDB = (Shop) rel.last();
                    id = shopFromDB.getId() + 1;
                }
                shop.setId(id);
                realm.copyToRealm(shop);
                break;
            case MainActivity.TAILOR_INDEX:
                Tailor tailor = new Tailor(name, description,address, selectedPosition.latitude, selectedPosition.longitude);
                rel = realm.where(Tailor.class).findAll();
                if (rel.size() != 0) {
                    Tailor tailorFromDB = (Tailor) rel.last();
                    id = tailorFromDB.getId() + 1;
                }
                tailor.setId(id);
                realm.copyToRealm(tailor);
                break;
            case MainActivity.WATCHMAKER_INDEX:
                Watchmaker watchmaker = new Watchmaker(name, description,address, selectedPosition.latitude, selectedPosition.longitude);
                rel = realm.where(Watchmaker.class).findAll();
                if (rel.size() != 0) {
                    Watchmaker watchmakerFromDB = (Watchmaker) rel.last();
                    id = watchmakerFromDB.getId() + 1;
                }
                watchmaker.setId(id);
                realm.copyToRealm(watchmaker);
                break;
            default:
                Toast.makeText(MapLocationActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                break;
        }
        realm.commitTransaction();
        finish();

    }

    public interface OnActivityInteractionListener {
        void addressDetected(String address);
    }


}


