package br.org.rdenadai;

import org.apache.cordova.DroidGap;

import android.content.Context;
import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * This class has been build using the method isBetterLocation and isSameProvider build by Google!
 * @author rdenadai
 */
public class GetNativeLocation implements LocationListener {
    private WebView mAppView;
    private DroidGap mGap;
    private double lat=0, lng=0;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private Location currentBestLocation = null;
    private LocationManager lm = null;

    public GetNativeLocation(DroidGap gap, WebView view) {
        mAppView = view;
        mGap = gap;
    }

    /** Determines whether one Location reading is better than the current Location fix
      * @param location  The new Location that you want to evaluate
      * @param currentBestLocation  The current Location fix, to which you want to compare the new one
      */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
        // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
          return provider2 == null;
        }
        return provider1.equals(provider2);
    }
    
    public void onLocationChanged(Location location) {
    	if(isBetterLocation(location, currentBestLocation)) {
    		// TODO Auto-generated method stub
    		lat = location.getLatitude();
			lng = location.getLongitude();
			currentBestLocation = location;
			Toast.makeText( mGap.getApplicationContext(), "lat:"+lat+" | long"+lng, Toast.LENGTH_SHORT).show();
    	}
    }

    private void getLocation() {
        lm =  (LocationManager) mGap.getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        } else {
        	Toast.makeText( mGap.getApplicationContext(), "Erro ao encontrar localização GPS", Toast.LENGTH_SHORT).show();
        }
        
        if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
    		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
    	} else {
        	Toast.makeText( mGap.getApplicationContext(), "Erro ao encontrar localização network", Toast.LENGTH_SHORT).show();
    	}
    }

    public void doInit() {
        getLocation();
    }

    public void destroy() {
    	lm.removeUpdates(this);
    }
    
    public double getLat(){ return this.lat;}
    public double getLong() { return this.lng; }
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub  
    	Toast.makeText( mGap.getApplicationContext(), "Provider disabled!", Toast.LENGTH_SHORT).show();
    }

    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub 
    	Toast.makeText( mGap.getApplicationContext(), "Provider enabled!", Toast.LENGTH_SHORT).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}