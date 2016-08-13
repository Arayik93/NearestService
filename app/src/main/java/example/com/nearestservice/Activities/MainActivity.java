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

import example.com.nearestservice.DialogBoxes.MarkersDialogBox;
import example.com.nearestservice.DialogBoxes.GPS_And_WiFi_Dialog_Box;
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

    public static final int AUTO_SERVICE_INDEX = 0;
    public static final int BEAUTY_SALON_INDEX = 1;
    public static final int FAST_FOOD_INDEX = 2;
    public static final int PHARMACY_INDEX = 3;
    public static final int PHOTO_INDEX = 4;
    public static final int SHOP_INDEX = 5;
    public static final int TAILOR_INDEX = 6;
    public static final int WATCHMAKER_INDEX = 7;
    public static final int ZOOM_LEVEL = 14;


    private final float RADIUS = 0.01f;

    private double userLatitude;
    private double userLongitude;
    private boolean flag = true;
    private int imageResource;

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
            readServicesFromDB(AutoService.class, AUTO_SERVICE_INDEX);

        } else if (id == R.id.nav_beautySalon) {
            readServicesFromDB(BeautySalon.class, BEAUTY_SALON_INDEX);

        } else if (id == R.id.nav_fastFood) {
            readServicesFromDB(FastFood.class, FAST_FOOD_INDEX);

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

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    TotalServiceInfo markerInfo = (TotalServiceInfo) marker.getTag();
                    LatLng latLng = new LatLng(userLatitude, userLongitude);
                    MarkersDialogBox cdd = new MarkersDialogBox(marker.getPosition(), latLng, markerInfo.getName(), markerInfo.getAddress(), markerInfo.getDescription(),
                            imageResource, Float.valueOf(markerInfo.getRating()), MainActivity.this);
                    cdd.show();


                    return false;
                }
            });


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
                    }
                }
            });

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
            case AUTO_SERVICE_INDEX:
                allServices = new ArrayList<AutoService>();
                break;
            case BEAUTY_SALON_INDEX:
                allServices = new ArrayList<BeautySalon>();
                break;
            case FAST_FOOD_INDEX:
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

        String description, category, name, address;
        double latitude, longitude;
        String rating;
        int id;
        BitmapDescriptor icon;


        latitude = 0;
        longitude = 0;
        rating = "0";
        name = "";
        description = "";
        category = "";


        id = 0;


        final int size = servicesPositions.size();

        switch (serviceIndex) {
            case AUTO_SERVICE_INDEX:
                for (int j = 0; j < size; ++j) {
                    AutoService selectedService = (AutoService) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    address = selectedService.getAddress();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    imageResource = R.drawable.markerautosalon;
                    id = selectedService.getId();
                    drawMarker(name, description, address, rating, latitude, longitude);

                }
                break;
            case BEAUTY_SALON_INDEX:
                for (int j = 0; j < size; ++j) {
                    BeautySalon selectedService = (BeautySalon) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    address = selectedService.getAddress();
                    category = selectedService.getCategory();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    //icon = BitmapDescriptorFactory.fromResource(R.drawable.markerbeautysalon);
                    imageResource = R.drawable.markerbeautysalon;

                    drawMarker(name, description, address, rating, latitude, longitude);


                    id = selectedService.getId();
                }
                break;

            case FAST_FOOD_INDEX:
                for (int j = 0; j < size; ++j) {
                    FastFood selectedService = (FastFood) servicesPositions.get(j);
                    name = selectedService.getName();
                    description = selectedService.getDescription();
                    category = selectedService.getCategory();
                    address = selectedService.getAddress();
                    rating = selectedService.getRating();
                    latitude = selectedService.getLatitude();
                    longitude = selectedService.getLongitude();
                    //icon = BitmapDescriptorFactory.fromResource(R.drawable.markerfastfood);
                    imageResource = R.drawable.markerfastfood;

                    id = selectedService.getId();
                    drawMarker(name, description, address, rating, latitude, longitude);

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
                    //icon = BitmapDescriptorFactory.fromResource(R.drawable.markerpharmacy);
                    imageResource = R.drawable.markerpharmacy;

                    id = selectedService.getId();
                    drawMarker(name, description, address, rating, latitude, longitude);

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
                    //icon = BitmapDescriptorFactory.fromResource(R.drawable.markerphoto);
                    imageResource = R.drawable.markerphoto;

                    id = selectedService.getId();
                    drawMarker(name, description, address, rating, latitude, longitude);

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
                    //icon = BitmapDescriptorFactory.fromResource(R.drawable.markershop);
                    imageResource = R.drawable.markershop;

                    id = selectedService.getId();
                    drawMarker(name, description, address, rating, latitude, longitude);

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
                    //icon = BitmapDescriptorFactory.fromResource(R.drawable.markertailor);
                    imageResource = R.drawable.markertailor;

                    id = selectedService.getId();
                    drawMarker(name, description, address, rating, latitude, longitude);

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
                    //icon = BitmapDescriptorFactory.fromResource(R.drawable.markerwatchmaker);
                    imageResource = R.drawable.markerwatchmaker;

                    id = selectedService.getId();
                    drawMarker(name, description, address, rating, latitude, longitude);

                }
                break;

            default:

                break;
        }

    }

    public void drawMarker(String name, String description, String address, String rating,
                           double latitude, double longitude) {
        if (userLongitude == 0 && userLatitude == 0) {
            GPS_And_WiFi_Dialog_Box gps_and_wiFi_dialog_box = new GPS_And_WiFi_Dialog_Box(MainActivity.this, "GPS");
            gps_and_wiFi_dialog_box.show();
            //displayPromptForEnablingGPS(MainActivity.this);

        } else if (isNearService(userLatitude, userLongitude, latitude, longitude) <= RADIUS) {
            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(imageResource));

            Marker mServiceMarker = mMap.addMarker(markerOptions);
            mServiceMarker.setDraggable(false);

            mServiceMarker.setTag(new TotalServiceInfo(name, description,
                    address, rating, imageResource));


        } else {
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

    public double isNearService(double userLatitude, double userLongitude, double serviceLatitude, double serviceLongitude) {
        return Math.sqrt((userLatitude - serviceLatitude) * (userLatitude - serviceLatitude) +
                (userLongitude - serviceLongitude) * (userLongitude - serviceLongitude));
    }


    class TotalServiceInfo {

        private String name, description, address, rating;
        private int imageResource;

        public TotalServiceInfo(String name, String description, String address, String rating, int imageResource) {
            this.name = name;
            this.description = description;
            this.address = address;
            this.rating = rating;
            this.imageResource = imageResource;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getAddress() {
            return address;
        }

        public String getRating() {
            return rating;
        }

        public int getImageResource() {
            return imageResource;
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

}


