package example.com.nearestservice;

import android.support.annotation.Nullable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Abov on 8/5/2016.
 */
public class Service extends RealmObject {

    private String Name;
    private String Category;
    private String Description;
    private String Address;
    private int Rating;
    @PrimaryKey
    private int id;



    public Service() {
    }


    public Service(String name, String category, String description, String address, int rating, int id) {
        Name = name;
        Category = category;
        Description = description;
        Address = address;
        Rating = rating;
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
