package example.com.nearestservice.ServiceCreators;

import example.com.nearestservice.Services.AutoService;
import example.com.nearestservice.Services.Pharmacy;

/**
 * Created by Abov on 8/14/2016.
 */
public class PharmacyCreator implements ServiceCreator {
    @Override
    public Service createService() {
        return new Pharmacy();
    }
}
