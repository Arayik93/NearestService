package example.com.nearestservice.ServiceCreators;

import example.com.nearestservice.Services.BeautySalon;

/**
 * Created by Abov on 8/14/2016.
 */
public class BeautySalonCreator implements ServiceCreator {

    @Override
    public Service createService() {
        return new BeautySalon();
    }
}
