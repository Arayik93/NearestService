package example.com.nearestservice.ServiceCreators;

import example.com.nearestservice.Services.Shop;

/**
 * Created by Abov on 8/14/2016.
 */
public class ShopCreator implements ServiceCreator {
    @Override
    public Service createService() {
        return new Shop();
    }
}
