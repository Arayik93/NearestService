package example.com.nearestservice.Services;

import com.google.android.gms.maps.model.LatLng;

public interface Service {
    void saveInDatabase(String name, String description, String address, double latitude, double longitude);
    double distanceFromUser(LatLng usersPosition);
    LatLng getPosition();
    Object getInfo(String s);
}
