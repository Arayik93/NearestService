package example.com.nearestservice.ServiceCreators;

import com.google.android.gms.maps.model.LatLng;

import example.com.nearestservice.Services.UniversalService;

public interface Service {
    void saveInDatabase(String name, String description, String address, double latitude, double longitude);
    double distanceFromUser(LatLng usersPosition);
    UniversalService showYourFullInfo();
}
