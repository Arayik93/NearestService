package example.com.nearestservice.Activities;
//7

import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import example.com.nearestservice.DialogBoxes.MarkersDialogBox;
import example.com.nearestservice.DialogBoxes.GPS_And_WiFi_Dialog_Box;
import example.com.nearestservice.Info.Constants;
import example.com.nearestservice.R;
import example.com.nearestservice.Services.FavoriteService;
import example.com.nearestservice.Services.Service;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback {





    int a = 100;
int b = 100;





    private double userLatitude;
    private double userLongitude;
    private boolean flag = true;
    private int lastSearchedService = 0;
    int s;

    private Realm realm;
    private GoogleMap mMap;

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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapLocationActivity.class);
                startActivity(i);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //TODO action bar
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
        int id = item.getItemId();

        if (id == R.id.nav_autoservice) {
            readServicesFromDB(Constants.AUTO_SERVICE_INDEX);
        } else if (id == R.id.nav_beautySalon) {
            readServicesFromDB(Constants.BEAUTY_SALON_INDEX);

        } else if (id == R.id.nav_fastFood) {
            readServicesFromDB(Constants.FAST_FOOD_INDEX);

        } else if (id == R.id.nav_pharmacy) {
            readServicesFromDB(Constants.PHARMACY_INDEX);

        } else if (id == R.id.nav_Photo) {
            readServicesFromDB(Constants.PHOTO_INDEX);

        } else if (id == R.id.nav_Shop) {
            readServicesFromDB(Constants.SHOP_INDEX);

        } else if (id == R.id.nav_Tailor) {
            readServicesFromDB(Constants.TAILOR_INDEX);

        } else if (id == R.id.nav_watchmaker) {
            readServicesFromDB(Constants.WATCHMAKER_INDEX);

        } else if (id == R.id.nav_send) {
            readFavoriteServicesFromDB();

            //TODO avelacnel favoritnern
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

        if (mMap == null)
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
                    userLatitude = arg0.getLatitude();
                    userLongitude = arg0.getLongitude();

                    if (flag) {

                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);

                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);
                        flag = false;
                        if(lastSearchedService != 0){
                            readServicesFromDB(lastSearchedService);
                        }
                    }
                }
            });

        }

    }

    private void readServicesFromDB(int serviceIndex) {
        lastSearchedService = serviceIndex;
        //TODO kam chshti gps-n miacac a te che , karoxa hin tvyalnern pahac lini 0 0 (iharke ete usern kyanqum gone mi angam 0 0 ketum exel a )
        if (userLongitude == 0 && userLatitude == 0) {
            GPS_And_WiFi_Dialog_Box gps_and_wiFi_dialog_box = new GPS_And_WiFi_Dialog_Box(MainActivity.this, "GPS");
            gps_and_wiFi_dialog_box.show();
            return;
        }


        Service service;
        realm.beginTransaction();
        RealmResults realmResults = realm.where(Service.class).equalTo("imageResource", serviceIndex).findAll();

        if (realmResults.isEmpty()) {
            realm.commitTransaction();
            Toast.makeText(MainActivity.this, "Services Fond: 0", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mMap.clear();
            Toast.makeText(MainActivity.this, "Services fond: " + realmResults.size(), Toast.LENGTH_SHORT).show();
        }

        LatLng userLatLang = new LatLng(userLatitude, userLongitude);
        int servicesOutOfRange = 0;
        for (int i = 0; i < realmResults.size(); ++i) {
            service = (Service) realmResults.get(i);
            if (service.distanceFromUser(userLatLang) < Constants.RADIUS) {

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(service.getLatitude(), service.getLongitude()));
                markerOptions.icon(BitmapDescriptorFactory.fromResource(service.getImageResource()));
                Marker mServiceMarker = mMap.addMarker(markerOptions);
                mServiceMarker.setDraggable(false);
                mServiceMarker.setTag(service);

                if (mMap != null) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            Service markerInfo = (Service) marker.getTag();
                            LatLng userLatLang = new LatLng(userLatitude, userLongitude);

                            MarkersDialogBox markersDialogBox = new MarkersDialogBox(userLatLang,
                                    markerInfo, MainActivity.this);
                            markersDialogBox.show();

                            //TODO yanm inch  ? :D
                            return false;
                        }
                    });
                }

                } else {
                ++servicesOutOfRange;
            }

        }
        if (servicesOutOfRange > 0)
            Toast.makeText(MainActivity.this, "Out of range services: " + servicesOutOfRange , Toast.LENGTH_SHORT).show();

        realm.commitTransaction();
    }


    private void readFavoriteServicesFromDB() {
        //TODO kam chshti gps-n miacac a te che , karoxa hin tvyalnern pahac lini 0 0 (iharke ete usern kyanqum gone mi angam 0 0 ketum exel a )
        if (userLongitude == 0 && userLatitude == 0) {
            GPS_And_WiFi_Dialog_Box gps_and_wiFi_dialog_box = new GPS_And_WiFi_Dialog_Box(MainActivity.this, "GPS");
            gps_and_wiFi_dialog_box.show();
            return;
        }


        FavoriteService service;
        realm.beginTransaction();
        RealmResults realmResults = realm.where(FavoriteService.class).findAll();

        if (realmResults.isEmpty()) {
            realm.commitTransaction();
            Toast.makeText(MainActivity.this, "Services Fond: 0", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mMap.clear();
            Toast.makeText(MainActivity.this, "Services fond: " + realmResults.size(), Toast.LENGTH_SHORT).show();
        }

        LatLng userLatLang = new LatLng(userLatitude, userLongitude);
        int servicesOutOfRange = 0;
        for (int i = 0; i < realmResults.size(); ++i) {
            service = (FavoriteService) realmResults.get(i);
            if (service.distanceFromUser(userLatLang) < Constants.RADIUS) {

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(service.getLatitude(), service.getLongitude()));
                markerOptions.icon(BitmapDescriptorFactory.fromResource(service.getImageResource()));
                Marker mServiceMarker = mMap.addMarker(markerOptions);
                mServiceMarker.setDraggable(false);
                mServiceMarker.setTag(service);

                if (mMap != null) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            Service markerInfo = (Service) marker.getTag();
                            LatLng userLatLang = new LatLng(userLatitude, userLongitude);

                            MarkersDialogBox markersDialogBox = new MarkersDialogBox(userLatLang,
                                    markerInfo, MainActivity.this);
                            markersDialogBox.show();

                            //TODO yanm inch  ? :D
                            return false;
                        }
                    });
                }

            } else {
                ++servicesOutOfRange;
            }

        }
        if (servicesOutOfRange > 0)
            Toast.makeText(MainActivity.this, "Out of range services: " + servicesOutOfRange , Toast.LENGTH_SHORT).show();

        realm.commitTransaction();
    }

}


