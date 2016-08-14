package example.com.nearestservice.Services;

import com.google.android.gms.maps.model.LatLng;

import example.com.nearestservice.Activities.MainActivity;
import example.com.nearestservice.R;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class FastFood implements RealmModel, Service {

    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String address;
    private String rating;
    private double latitude;
    private double longitude;
    private int imageResource;
    private int categoryId;

    public FastFood(){}

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public void saveInDatabase(String name, String description, String address, double latitude, double longitude) {
        FastFood service = new FastFood();
        service.setName(name);
        service.setDescription(description);
        service.setAddress(address);
        service.setLatitude(latitude);
        service.setLongitude(longitude);
        service.setRating("0");
        service.setImageResource(R.drawable.markerfastfood);
        service.setCategoryId(MainActivity.FAST_FOOD_INDEX);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults rel = realm.where(FastFood.class).findAll();
        int id = 1;
        if (rel.size() != 0) {
            FastFood serviceFromDB = (FastFood) rel.last();
            id = serviceFromDB.getId() + 1;
        }
        service.setId(id);
        realm.copyToRealm(service);
        realm.commitTransaction();
    }


    @Override
    public double distanceFromUser(LatLng usersPosition) {
        return Math.sqrt((usersPosition.latitude - this.latitude) * (usersPosition.latitude - this.latitude) +
                (usersPosition.longitude - this.longitude) * (usersPosition.longitude - this.longitude));
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(this.latitude, this.longitude);
    }

    @Override
    public Object getInfo(String s) {
        switch (s){
            case "id":
                return this.getId();
            case "name":
                return this.getName();
            case "description":
                return this.getDescription();
            case "address":
                return this.getAddress();
            case "rating":
                return this.getRating();
            case "latitude":
                return this.getLatitude();
            case "longitude":
                return this.getLongitude();
            case "imageResource":
                return this.getImageResource();
            case "categoryId":
                return this.getCategoryId();
            default:
                return null;
        }
    }


}



