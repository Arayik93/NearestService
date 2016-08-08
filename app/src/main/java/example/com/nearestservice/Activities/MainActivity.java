package example.com.nearestservice.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

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
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback {

    private Realm realm;
    private GoogleMap mMap;
    private double userLatitude;
    private double userLongitude;
    private boolean flag = true;
    public static final int AUTOSERVICE_INDEX = 0;
    public static final int BEAUTYSALON_INDEX = 1;
    public static final int FASTFOOD_INDEX = 2;
    public static final int PHARMACY_INDEX = 3;
    public static final int PHOTO_INDEX = 4;
    public static final int SHOP_INDEX = 5;
    public static final int TAILOR_INDEX = 6;
    public static final int WATCHMAKER_INDEX = 7;
    private final float RADIUS = 0.01f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //tb = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapLocationActivity.class);
                startActivity(i);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_autoservice) {
            readServicesFromDB(AutoService.class, AUTOSERVICE_INDEX);

        } else if (id == R.id.nav_beautySalon) {
            readServicesFromDB(BeautySalon.class, BEAUTYSALON_INDEX);

        } else if (id == R.id.nav_fastFood) {
            readServicesFromDB(FastFood.class, FASTFOOD_INDEX);

        } else if (id == R.id.nav_pharmacy) {
            readServicesFromDB(Pharmacy.class, PHARMACY_INDEX);

        } else if (id == R.id.nav_Photo) {
            readServicesFromDB(Photo.class, PHOTO_INDEX);

        } else if (id == R.id.nav_Shop) {
            readServicesFromDB(Shop.class, SHOP_INDEX);

        } else if (id == R.id.nav_Tailor) {
            readServicesFromDB(Tailor.class, TAILOR_INDEX);

        } else if (id == R.id.nav_watchmaker) {
            readServicesFromDB(Watchmaker.class, WATCHMAKER_INDEX);

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_add) {

            Intent i = new Intent(MainActivity.this, MapLocationActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = googleMap;

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(MainActivity.this, "You have to accept to enjoy all app's services!", Toast.LENGTH_LONG).show();
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            }


            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {


                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub
                        userLatitude = arg0.getLatitude();
                        userLongitude = arg0.getLongitude();

                        if (flag) {

                            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);

                            mMap.moveCamera(center);
                            mMap.animateCamera(zoom);
                            flag = false;
                        }


                    }
                });

            }


        }


        // mMap.setMyLocationEnabled(true);
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        /*mMap.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));*/


    }


    private void readServicesFromDB(Class c, int serviceIndex) {
        mMap.clear();
        realm.beginTransaction();
        RealmResults realmResults = realm.where(c).findAll();
        if (realmResults.isEmpty()) {
            realm.commitTransaction();
            Toast.makeText(MainActivity.this, "0 Services Fond", Toast.LENGTH_SHORT).show();
            return;
        }
        List allServices;
        switch (serviceIndex) {
            case AUTOSERVICE_INDEX:
                allServices = new ArrayList<AutoService>();
                break;
            case BEAUTYSALON_INDEX:
                allServices = new ArrayList<BeautySalon>();
                break;
            case FASTFOOD_INDEX:
                allServices = new ArrayList<FastFood>();
                break;
            case PHARMACY_INDEX:
                allServices = new ArrayList<Pharmacy>();
                break;
            case PHOTO_INDEX:
                allServices = new ArrayList<Photo>();
                break;
            case SHOP_INDEX:
                allServices = new ArrayList<Shop>();
                break;
            case TAILOR_INDEX:
                allServices = new ArrayList<Tailor>();
                break;
            case WATCHMAKER_INDEX:
                allServices = new ArrayList<Watchmaker>();
                break;
            default:
                allServices = new ArrayList();
                break;
        }

        allServices.addAll(realmResults);
        Toast.makeText(MainActivity.this, "" + allServices.size() + " Services fond", Toast.LENGTH_SHORT).show();

        realm.commitTransaction();
        setServicesPositions(allServices, serviceIndex);
    }

    private void setServicesPositions(List servicesPositions, int serviceIndex) {

        String  description, category,name,address;
        double rating, latitude, longitude;
        int id;

        BitmapDescriptor icon;

        latitude = 0;
        longitude = 0;
        rating = 0;
        name = "";
        description = "";
        category = "";


        id = 0;


        final int size = servicesPositions.size();

        switch (serviceIndex) {
            case AUTOSERVICE_INDEX:
                for (int j = 0; j < size; ++j) {
                    AutoService selectedService = (AutoService) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    address = selectedService.getAddress();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.markerautosalon);
                    id = selectedService.getId();
                    drawMarker(name,description,address,rating,latitude,longitude,icon);

                }
                break;
            case BEAUTYSALON_INDEX:
                for (int j = 0; j < size; ++j) {
                    BeautySalon selectedService = (BeautySalon) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    address = selectedService.getAddress();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.markerbeautysalon);
                    drawMarker(name,description,address,rating,latitude,longitude,icon);

                    id = selectedService.getId();
                }
                break;

            case FASTFOOD_INDEX:
                for (int j = 0; j < size; ++j) {
                    FastFood selectedService = (FastFood) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    category = selectedService.getCategory();
                    address = selectedService.getAddress();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.markerfastfood);
                    id = selectedService.getId();
                    drawMarker(name,description,address,rating,latitude,longitude,icon);

                }
                break;
            case PHARMACY_INDEX:
                for (int j = 0; j < size; ++j) {
                    Pharmacy selectedService = (Pharmacy) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    address = selectedService.getAddress();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.markerpharmacy);
                    id = selectedService.getId();
                    drawMarker(name,description,address,rating,latitude,longitude,icon);

                }
                break;

            case PHOTO_INDEX:
                for (int j = 0; j < size; ++j) {
                    Photo selectedService = (Photo) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    address = selectedService.getAddress();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.markerphoto);
                    id = selectedService.getId();
                    drawMarker(name,description,address,rating,latitude,longitude,icon);

                }
                break;

            case SHOP_INDEX:
                for (int j = 0; j < size; ++j) {
                    Shop selectedService = (Shop) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    address = selectedService.getAddress();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.markershop);
                    id = selectedService.getId();
                    drawMarker(name,description,address,rating,latitude,longitude,icon);

                }
                break;

            case TAILOR_INDEX:
                for (int j = 0; j < size; ++j) {
                    Tailor selectedService = (Tailor) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    address = selectedService.getAddress();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.markertailor);
                    id = selectedService.getId();
                    drawMarker(name,description,address,rating,latitude,longitude,icon);

                }
                break;

            case WATCHMAKER_INDEX:
                for (int j = 0; j < size; ++j) {
                    Watchmaker selectedService = (Watchmaker) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    address = selectedService.getAddress();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.markerwatchmaker);
                    id = selectedService.getId();
                    drawMarker(name,description,address,rating,latitude,longitude,icon);

                }
                break;

            default:

                break;
        }

        if (name.isEmpty()) {
            name = " ";
        } else if (name.equals("")) {
            name = " ";
        }


    }
    public void drawMarker(String name,String description,String address,double rating,double latitude,double longitude,BitmapDescriptor icon){
        if(userLongitude == 0 && userLatitude == 0){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Dear Player");
            alertDialog.setMessage("Choose Your Soldiers");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();


        }else if(isNearService(userLatitude,userLongitude,latitude,longitude) <= RADIUS){
            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(name+"\n"+address);
            markerOptions.icon(icon);
            Marker mServiceMarker = mMap.addMarker(markerOptions);
            mServiceMarker.setDraggable(false);
        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("bbbbbb Player");
            alertDialog.setMessage("CfgfdgdgdgfdfdfdfSoldiers");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }


    }
    public double isNearService(double userLatitude,double userLongitude,double serviceLatitude,double serviceLongitude){
        return  Math.sqrt((userLatitude-serviceLatitude)*(userLatitude-serviceLatitude) + (userLongitude-userLongitude)*(userLongitude-userLongitude));
    }
//    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.current_position_tennis_ball)
//
//    MarkerOptions markerOptions = new MarkerOptions().position(latLng)
//            .title("Current Location")
//            .snippet("Thinking of finding some thing...")
//            .icon(icon);
//
//    mMarker = googleMap.addMarker(markerOptions);


}


