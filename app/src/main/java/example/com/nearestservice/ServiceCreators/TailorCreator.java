package example.com.nearestservice.ServiceCreators;

import example.com.nearestservice.Services.Tailor;

/**
 * Created by Abov on 8/14/2016.
 */
public class TailorCreator implements ServiceCreator {
    @Override
    public Service createService() {
        return new Tailor();
    }
}
