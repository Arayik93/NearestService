package example.com.nearestservice.Services;

public class UniversalService {
    private String name, description, address, category, rating;
    double latitude, longitude;
    int imageResource, id;

    public UniversalService(String name, String description, String address, String category, String
            rating, double latitude, double longitude, int imageResource, int id) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.category = category;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageResource = imageResource;
        this.id = id;
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

    public String getCategory() {
        return category;
    }

    public String getRating() {
        return rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getId() {
        return id;
    }
}
