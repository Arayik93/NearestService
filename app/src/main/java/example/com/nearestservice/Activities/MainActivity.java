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
import android.util.Log;
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
import example.com.nearestservice.Services.Watchmaker;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        AddServiceFragment.OnFragmentInteractionListener {

    public static final String CATEGORY_TYPE = "CategoryType";

    public static Realm realm;

    private GoogleMap mMap;
    //private LatLng sydney;
    private LinearLayout mapFragmentlinearLayout;
    //private FrameLayout addFragmentLinearLayout;
    private Fragment addServiceFragment;
    private android.support.v7.widget.Toolbar tb;

    List a;
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


        mapFragmentlinearLayout = (LinearLayout) findViewById(R.id.layout_for_map_Fragment);
        //addFragmentLinearLayout = (FrameLayout) findViewById(R.id.frameLayout_for_addServiceFragment);
        addServiceFragment = new AddServiceFragment();
        tb = (Toolbar) findViewById(R.id.toolbar);
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

            //startActivity(new Intent(MainActivity.this, AddServiceActivity.class));

            //tb.setVisibility(View.GONE);
            //changeMapToAdd();
            // Handle the camera action
        } else if (id == R.id.nav_watchmaker) {


            writeInDB();
            readServicesFromDB(Watchmaker.class);

            setServicesPositions(1);

        } else if (id == R.id.nav_beautySalon) {
            Intent i = new Intent(MainActivity.this, MapLocationActivity.class);
            i.putExtra(CATEGORY_TYPE, "varsavir");
            startActivity(i);

        } else if (id == R.id.nav_fastFood) {
            writeInDB();
            readServicesFromDB(Watchmaker.class);
            Toast.makeText(MainActivity.this, ""+a.get(0).toString(), Toast.LENGTH_LONG).show();

           // setServicesPositions(a, "a");


        } else if (id == R.id.nav_autoservice) {



        } else if (id == R.id.nav_send) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        // mMap = googleMap;


        // LatLng sydney = new LatLng(-33.867, 151.206);


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


                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub

                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);

                        mMap.moveCamera(center);

                        if (flag) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude()))
                                    .title("aa").draggable(true).snippet("betpo"));
                            mMap.animateCamera(zoom);
                            flag = false;
                        }
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                Toast.makeText(MainActivity.this, "Alibaba", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });

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

    private void changeMapToAdd() {
        mapFragmentlinearLayout.setVisibility(View.GONE);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout_for_addServiceFragment, addServiceFragment);
        fragmentTransaction.commit();
    }

    private void writeInDB() {

        Watchmaker u;

        realm.beginTransaction();
        int id = 1;
for(int i = 0; i < 2; ++i ) {
    RealmResults rel = realm.where(Watchmaker.class).findAll();


    if (rel.size() != 0) {
        Watchmaker us = (Watchmaker) rel.last();
        id = us.getId() + 1;
    }

    u = new Watchmaker();
    u.setName("kanfe");
    u.setId(id);
    u.setDescription("asa");
    u.setLatitude(0 + i*2);
    u.setLatitude(0+i*2);
    u.setRating(0);


    //a = rel;


    //realm.copyToRealmOrUpdate(u);//esi karanq nuyn id-n tanq update anenq
    realm.copyToRealm(u);
}
        realm.commitTransaction();
    }

    private void readServicesFromDB(Class c) {

        if (!(c.getSuperclass().equals(RealmObject.class))) {
            Log.e("TODO", "c must extends from Realm Object");
        } else {
            realm.beginTransaction();

            RealmResults rel = realm.where(c).findAll();
            //a = rel;
            a = new ArrayList<Watchmaker>();
            a.addAll(rel);

            realm.commitTransaction();

        }

    }

    private void setServicesPositions(int i) {


        String name, description;
        double rating, latitude, longitude;
        float icon;




            latitude = 0;
            longitude = 0;
            rating = 0;
            name = "";
            description = "";
           // icon = BitmapDescriptorFactory.HUE_GREEN;

            Log.e("abov", "verev em");
        //for (Watchmaker c : a) {

        for(int z = 0; z < a.size(); z++) {

            Watchmaker watchmaker = (Watchmaker) a.get(z);
            //Watchmaker watchmaker = (Watchmaker) c.cast(Watchmaker.class);
            // if (c.getClass().equals(Watchmaker.class)) {
            Log.e("abov", "estex em");
            // Watchmaker watchmaker = (Watchmaker) c.cast(Watchmaker.class);
            name = watchmaker.getName();
            description = watchmaker.getDescription();
            rating = watchmaker.getRating();
            latitude = watchmaker.getLatitude();
            longitude = watchmaker.getLongitude();
            //icon = R.drawable.ic_watchmaker_24px;
            //}
            // }
            Log.e("abov","bb");

            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            Marker mServiceMarker = mMap.addMarker(markerOptions);
            mServiceMarker.setDraggable(false);
        }





        }


        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



    private void changeAdToMap() {
        tb.setVisibility(View.VISIBLE);
        mapFragmentlinearLayout.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(addServiceFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void cancelButonOnAddFragmentPressed() {
        changeAdToMap();
    }

    @Override
    public void addButonOnAddFragmentPressed() {
        changeAdToMap();
    }
}
