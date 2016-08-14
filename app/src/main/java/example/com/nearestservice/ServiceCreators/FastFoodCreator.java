package example.com.nearestservice.ServiceCreators;

import example.com.nearestservice.Services.FastFood;

/**
 * Created by Abov on 8/14/2016.
 */
public class FastFoodCreator  implements ServiceCreator {

    @Override
    public Service createService() {
        return new FastFood();
    }
}