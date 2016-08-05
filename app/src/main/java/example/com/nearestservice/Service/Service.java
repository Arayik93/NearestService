package example.com.nearestservice.Service;

import android.support.annotation.Nullable;

import io.realm.RealmObject;

/**
 * Created by Abov on 8/5/2016.
 */
public class Service extends RealmObject {

    @Nullable
    private int id;
    private String mName;
    private String mCategory;
    private String mDescription;
    private String mAddress;
    private int mRating;


    public Service() {
    }


    public Service(int id, String name, String category, String description, String address, int rating) {
        this.id = id;
        mName = name;
        mCategory = category;
        mDescription = description;
        mAddress = address;
        mRating = rating;
    }
}
