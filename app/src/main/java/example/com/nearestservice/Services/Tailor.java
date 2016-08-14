package example.com.nearestservice.Services;

import com.google.android.gms.maps.model.LatLng;

import example.com.nearestservice.R;
import example.com.nearestservice.ServiceCreators.Service;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class Tailor extends RealmObject implements Service{

    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String address;
    private String rating = "0";
    private double latitude;
    private double longitude;
    private String category = "tailor";

    public Tailor( String name, String description, String address, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Tailor() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void saveInDatabase(String name, String description, String address, double latitude, double longitude) {
        Tailor service = new Tailor(name, description, address, latitude, longitude);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults rel = realm.where(Tailor.class).findAll();
        int id = 1;
        if (rel.size() != 0) {
            Tailor serviceFromDB = (Tailor) rel.last();
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
    public UniversalService showYourFullInfo() {
        return new UniversalService(this.name,this.description, this.address, this.category, this.rating,
                this.latitude, this.longitude, R.drawable.markertailor, this.id);
    }
}
