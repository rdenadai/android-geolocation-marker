package br.org.rdenadai;

import android.app.Activity;
import android.os.Bundle;
import org.apache.cordova.*;

public class AndroidGeolocationMarkerActivity extends DroidGap {
	private GetNativeLocation gps;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init();
        
        gps = new GetNativeLocation(this, appView);
        gps.doInit();
        appView.addJavascriptInterface(gps, "NativeGPS");
        
        super.loadUrl("file:///android_asset/www/index.html");
    }
    
    
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	gps.destroy();
    }
}