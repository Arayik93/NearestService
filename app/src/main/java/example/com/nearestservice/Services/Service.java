package example.com.nearestservice.Services;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Abov on 8/16/2016.
 */
@RealmClass
public class Service implements RealmModel {
    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String address;
    private float rating;
    private double latitude;
    private double longitude;
    private int imageResource;

    public Service(){}

    public Service(int id, String name, String description, String address, float rating,
                   double latitude, double longitude, int imageResource) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageResource = imageResource;
    }

    //special canstructor for realm
    public Service(String name,  String address, String description, double latitude,
                   double longitude, int imageResource) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.rating = 0f;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageResource = imageResource;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }


    public double distanceFromUser(LatLng usersPosition) {
        return Math.sqrt((usersPosition.latitude - this.latitude) * (usersPosition.latitude - this.latitude) +
                (usersPosition.longitude - this.longitude) * (usersPosition.longitude - this.longitude));
    }
}
