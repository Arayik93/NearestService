package example.com.nearestservice.ServiceCreators;

import example.com.nearestservice.Services.Pharmacy;
import example.com.nearestservice.Services.Photo;

/**
 * Created by Abov on 8/14/2016.
 */
public class PhotoCreator implements ServiceCreator {
    @Override
    public Service createService() {
        return new Photo();
    }
}
