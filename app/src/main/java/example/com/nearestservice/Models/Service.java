package example.com.nearestservice.models;

/**
 * Created by Narehh on 8/14/2016.
 */
public class Service {

    private String name;
    private String description;
    private String address;
    private double rating;
    private double latitude;
    private double longitude;
    private String category;

    public Service() {

    }

    public Service(String name, String description, String address, double rating, double latitude, double longitude, String category) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
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

    public double getRating() {
        return rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCategory() {
        return category;
    }

}
