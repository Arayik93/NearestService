package example.com.nearestservice.ServiceCreators;

import example.com.nearestservice.Services.Watchmaker;

/**
 * Created by Abov on 8/14/2016.
 */
public class WatchmakerCreator implements ServiceCreator {
    @Override
    public Service createService() {
        return new Watchmaker();
    }
}
