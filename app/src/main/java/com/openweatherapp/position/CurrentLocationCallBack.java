package com.openweatherapp.position;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.openweatherapp.event.PositionListener;

import java.util.List;

public class CurrentLocationCallBack  extends LocationCallback{

    private static String TAG = CurrentLocationCallBack.class.getCanonicalName();

    private static final String PACKAGE_NAME = CurrentLocationCallBack.class.getCanonicalName();

    public static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private PositionListener positionListener;

    public static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";
    private Context context;
    private Location oldLocation;

    public CurrentLocationCallBack() {
        super();
    }

    public CurrentLocationCallBack(Context context, PositionListener positionListener) {
        this.positionListener = positionListener;
        this.context = context;
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);
        List<Location> locationList = locationResult.getLocations();
        Location location = locationList.get(locationList.size() - 1);

        if(isBetterLocation(getOldLocation(), location)) {
            Intent intent = new Intent(ACTION_BROADCAST);
            intent.putExtra(EXTRA_LOCATION, location);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
        setOldLocation(location);
    }

    @Override
    public void onLocationAvailability(LocationAvailability locationAvailability) {
        super.onLocationAvailability(locationAvailability);
    }

    void doWorkWithNewLocation(Location location) {
    }

    static final int TIME_DIFFERENCE_THRESHOLD = 1 * 60 * 1000;

    boolean isBetterLocation(Location oldLocation, Location newLocation) {
        // If there is no old location, of course the new location is better.
        if(oldLocation == null) {
            return true;
        }

        boolean isNewer = newLocation.getTime() > oldLocation.getTime();

        boolean isMoreAccurate = newLocation.getAccuracy() < oldLocation.getAccuracy();
        if(isMoreAccurate && isNewer) {
            return true;
        } else if(isMoreAccurate && !isNewer) {
            long timeDifference = newLocation.getTime() - oldLocation.getTime();
            return timeDifference > -TIME_DIFFERENCE_THRESHOLD;
        }

        return false;
    }

    public Location getOldLocation() {
        return oldLocation;
    }

    public void setOldLocation(Location oldLocation) {
        this.oldLocation = oldLocation;
    }
}
