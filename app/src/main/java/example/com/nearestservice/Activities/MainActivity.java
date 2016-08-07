package example.com.nearestservice.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

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
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback {


    public static Realm realm;

    private GoogleMap mMap;


    private double x, y;
    private boolean flag = true;


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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
            readServicesFromDB(AutoService.class, 0);

        } else if (id == R.id.nav_beautySalon) {
            readServicesFromDB(BeautySalon.class, 1);

        } else if (id == R.id.nav_fastFood) {
            readServicesFromDB(FastFood.class, 2);

        } else if (id == R.id.nav_pharmacy) {
            readServicesFromDB(Pharmacy.class, 3);

        } else if (id == R.id.nav_Photo) {
            readServicesFromDB(Photo.class, 4);

        } else if (id == R.id.nav_Shop) {
            readServicesFromDB(Shop.class, 5);

        } else if (id == R.id.nav_Tailor) {
            readServicesFromDB(Tailor.class, 6);

        } else if (id == R.id.nav_watchmaker) {
            readServicesFromDB(Watchmaker.class, 7);

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
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {


                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub

                        if (flag) {

                            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);

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


    private void readServicesFromDB(Class c, int i) {
        realm.beginTransaction();
        RealmResults rel = realm.where(c).findAll();
        List allServices;
        switch (i) {
            case 0:
                allServices = new ArrayList<AutoService>();
                break;
            case 1:
                allServices = new ArrayList<BeautySalon>();
                break;
            case 2:
                allServices = new ArrayList<FastFood>();
                break;
            case 3:
                allServices = new ArrayList<Pharmacy>();
                break;
            case 4:
                allServices = new ArrayList<Photo>();
                break;
            case 5:
                allServices = new ArrayList<Shop>();
                break;
            case 6:
                allServices = new ArrayList<Tailor>();
                break;
            case 7:
                allServices = new ArrayList<Watchmaker>();
                break;
            default:
                allServices = new ArrayList();
                break;
        }

        allServices.addAll(rel);
        realm.commitTransaction();
        setServicesPositions(allServices, i);
    }

    private void setServicesPositions(List servicesPositions, int i) {

        String name, description, category;
        double rating, latitude, longitude;
        float icon;
        int id;

        latitude = 0;
        longitude = 0;
        rating = 0;
        name = "";
        description = "";
        category = "";
        icon = (float) R.drawable.ic_menu_manage;
        id = 0;


        final int size = servicesPositions.size();

        switch (i) {
            case 0:
                for (int j = 0; j < size; ++j) {
                    AutoService selectedService = (AutoService) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = (float) R.drawable.ic_menu_manage;
                    id = selectedService.getId();

                }
                break;
            case 1:
                for (int j = 0; j < size; ++j) {
                    BeautySalon selectedService = (BeautySalon) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = (float) R.drawable.ic_menu_manage;
                    id = selectedService.getId();
                }
                break;

            case 2:
                for (int j = 0; j < size; ++j) {
                    FastFood selectedService = (FastFood) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = (float) R.drawable.ic_menu_manage;
                    id = selectedService.getId();
                }
                break;
            case 3:
                for (int j = 0; j < size; ++j) {
                    Pharmacy selectedService = (Pharmacy) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = (float) R.drawable.ic_menu_manage;
                    id = selectedService.getId();
                }
                break;

            case 4:
                for (int j = 0; j < size; ++j) {
                    Photo selectedService = (Photo) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = (float) R.drawable.ic_menu_manage;
                    id = selectedService.getId();
                }
                break;

            case 5:
                for (int j = 0; j < size; ++j) {
                    Shop selectedService = (Shop) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = (float) R.drawable.ic_menu_manage;
                    id = selectedService.getId();
                }
                break;

            case 6:
                for (int j = 0; j < size; ++j) {
                    Tailor selectedService = (Tailor) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = (float) R.drawable.ic_menu_manage;
                    id = selectedService.getId();
                }
                break;

            case 7:
                for (int j = 0; j < size; ++j) {
                    Watchmaker selectedService = (Watchmaker) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    icon = (float) R.drawable.ic_menu_manage;
                    id = selectedService.getId();
                }
                break;

            default:
                latitude = 0;
                longitude = 0;
                rating = 0;
                name = "";
                description = "";
                category = "";
                icon = (float) R.drawable.ic_menu_manage;
                id = 0;
                break;
        }

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        Marker mServiceMarker = mMap.addMarker(markerOptions);
        mServiceMarker.setDraggable(false);
    }


}


