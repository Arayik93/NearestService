package example.com.nearestservice.Services;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Taron on 06.08.2016.
 */
public class Shop extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private int rating;
    private double latitude;
    private double longitude;
    private String category = "shop";

    public Shop(int id, String name, String description, int rating, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Shop() {
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
