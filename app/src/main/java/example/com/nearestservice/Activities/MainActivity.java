package example.com.nearestservice.Activities;

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
import example.com.nearestservice.R;
import example.com.nearestservice.Services.Service;
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

    public static final int AUTO_SERVICE_INDEX = 0;
    public static final int BEAUTY_SALON_INDEX = 1;
    public static final int FAST_FOOD_INDEX = 2;
    public static final int PHARMACY_INDEX = 3;
    public static final int PHOTO_INDEX = 4;
    public static final int SHOP_INDEX = 5;
    public static final int TAILOR_INDEX = 6;
    public static final int WATCHMAKER_INDEX = 7;
    public static final int ZOOM_LEVEL = 14;

    private static final float RADIUS = 0.01f;

    private double userLatitude;
    private double userLongitude;
    private boolean flag = true;
    private int lastSearchedService = -1;


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
            readServicesFromDB(AUTO_SERVICE_INDEX);
        } else if (id == R.id.nav_beautySalon) {
            readServicesFromDB(BEAUTY_SALON_INDEX);

        } else if (id == R.id.nav_fastFood) {
            readServicesFromDB(FAST_FOOD_INDEX);

        } else if (id == R.id.nav_pharmacy) {
            readServicesFromDB(PHARMACY_INDEX);

        } else if (id == R.id.nav_Photo) {
            readServicesFromDB(PHOTO_INDEX);

        } else if (id == R.id.nav_Shop) {
            readServicesFromDB(SHOP_INDEX);

        } else if (id == R.id.nav_Tailor) {
            readServicesFromDB(TAILOR_INDEX);

        } else if (id == R.id.nav_watchmaker) {
            readServicesFromDB(WATCHMAKER_INDEX);

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
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(ZOOM_LEVEL);

                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);
                        flag = false;
                        if(lastSearchedService > 0){
                            readServicesFromDB(lastSearchedService);
                        }
                    }
                }
            });

            //TODO QNNAKREL sra linel chlnelu harc@
            // mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        /*mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                //Context context, String name, String address, String description, int imageResource, float rating, Activity activity)
                MarkersDialogBox cdd = new MarkersDialogBox( "aname", "address", "descrip",
                        R.mipmap.ic_launcher, 1.5f, MainActivity.this);
                cdd.show();

            }
        });*/
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

        Service service = serviceTypeDetector(serviceIndex);
        realm.beginTransaction();
        RealmResults realmResults = realm.where((Class) service.getClass()).findAll();

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
            if (service.distanceFromUser(userLatLang) < RADIUS) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(service.getPosition());
                markerOptions.icon(BitmapDescriptorFactory.fromResource(
                        (int)service.getInfo("imageResource")));
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

                            MarkersDialogBox markersDialogBox = new MarkersDialogBox(
                                    marker.getPosition(), userLatLang,
                                    (String)markerInfo.getInfo("name"),
                                    (String)markerInfo.getInfo("address"),
                                    (String)markerInfo.getInfo("description"),
                                    (int)markerInfo.getInfo("imageResource"),
                                    Float.valueOf((String)markerInfo.getInfo("rating")),
                                    MainActivity.this,
                                    markerInfo.distanceFromUser(userLatLang));
                            markersDialogBox.show();

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

    public static Service serviceTypeDetector(int i) {
        switch (i) {
            case MainActivity.AUTO_SERVICE_INDEX:
                return new AutoService();
            case MainActivity.BEAUTY_SALON_INDEX:
                return new BeautySalon();
            case MainActivity.FAST_FOOD_INDEX:
                return new FastFood();
            case MainActivity.PHARMACY_INDEX:
                return new Pharmacy();
            case MainActivity.PHOTO_INDEX:
                return new Photo();
            case MainActivity.SHOP_INDEX:
                return new Shop();
            case MainActivity.TAILOR_INDEX:
                return new Tailor();
            case MainActivity.WATCHMAKER_INDEX:
                return new Watchmaker();
            default:
                return new AutoService();
        }
    }


    /*class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
           myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            //TODO avelacnel te ani metr e heru amenamotik servisi infowindown bacel avtomat kam et ge direction@ kani avtomat

            LinearLayout markersLinearLayout = (LinearLayout) myContentsView.findViewById(R.id.markersLayout);
            markersLinearLayout.setBackgroundColor(getResources().getColor(R.color.forMarkersLayot));

            TotalServiceInfo mTotalServiceInfo = (TotalServiceInfo) marker.getTag();
            TextView markerName = ((TextView) myContentsView.findViewById(R.id.name));
            markerName.setText(mTotalServiceInfo.getName());
            TextView markerDescription = ((TextView) myContentsView.findViewById(R.id.description));
            markerDescription.setText(mTotalServiceInfo.getDescription());
            TextView markerAddress = ((TextView) myContentsView.findViewById(R.id.address));
            markerAddress.setText(mTotalServiceInfo.getAddress());
            RatingBar markerRatingBar = ((RatingBar) myContentsView.findViewById(R.id.ratingBar));

            LayerDrawable stars = (LayerDrawable) markerRatingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            markerRatingBar.setRating(4.5f);
            //markerRatingBar.setRating(Float.valueOf(mTotalServiceInfo.getRating()));
            //ImageView markersImageView = (ImageView) myContentsView.findViewById(R.id.markersLayoutPhoto);

            return myContentsView;
        }
    }
*/



    /*private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/

}


