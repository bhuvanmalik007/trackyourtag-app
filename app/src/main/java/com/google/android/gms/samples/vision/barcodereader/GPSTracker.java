package com.google.android.gms.samples.vision.barcodereader;


import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
/**
 * Created by bhuvanmalik on 09/11/15.
 */

public class GPSTracker extends Service implements LocationListener {
    private final Context context;

    public GPSTracker(Context context) {
        this.context = context;
        getLocation();
    }

    boolean isGPSEnabled=false;
    boolean isNetworkEnabled=false;

    boolean canGetLocation=false;
    Location location;
    double latitude,longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES=10;
    private static final long MIN_TIME_BETWEEN_UPDATES=1000*60*1;
    protected LocationManager locationmanager;

    public Location getLocation(){

        try
        {
            locationmanager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled=locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled=locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled && !isNetworkEnabled){

            }
            else{
                this.canGetLocation=true;
                if(isNetworkEnabled) {
                    locationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationmanager != null) {
                        location = locationmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLatitude();
                        }
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }
        return location;
    }

  /*  public void stopUsingGPS(){
        if (locationmanager != null) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationmanager.removeUpdates(GPSTracker.this);
            }
        }

    }   */


    public double getLatitude(){
        if(location!=null){
            latitude=location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if(location!=null){
            longitude=location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation(){
        return this.canGetLocation;
    }



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
