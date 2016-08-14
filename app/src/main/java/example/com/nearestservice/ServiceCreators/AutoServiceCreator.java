package example.com.nearestservice.ServiceCreators;

import android.util.Log;

import example.com.nearestservice.Services.AutoService;

/**
 * Created by Abov on 8/14/2016.
 */
public class AutoServiceCreator implements ServiceCreator {

    @Override
    public Service createService() {
        Log.e("abov", "AutoCreator");

        return new AutoService();
    }
}