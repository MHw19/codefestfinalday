package com.lk.codefist2021;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;


import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import static android.content.Context.LOCATION_SERVICE;

public class GPSHelper implements LocationListener {

    private static final String TAG = "GPSHelper";
    MapsFragment mapFragment;

    public GPSHelper(MapsFragment mapFragment) {
        this.mapFragment = mapFragment;
    }

    public Location getCurrentLocationListner(Context mContext){
        int MIN_TIME_BW_UPDATES = 200;
        int MIN_DISTANCE_CHANGE_FOR_UPDATES = 3;
        Location loc = null;
        Double latitude, longitude;

        LocationManager locationManager = (LocationManager) mContext
                .getSystemService(LOCATION_SERVICE);

        // getting GPS status
        Boolean checkGPS = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        Boolean checkNetwork = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!checkGPS && !checkNetwork) {
            Toast.makeText(mContext, "No Service Provider Available", Toast.LENGTH_SHORT).show();
        } else {
            //this.canGetLocation = true;
            // First get location from Network Provider
            if (checkNetwork) {
                Toast.makeText(mContext, "Network", Toast.LENGTH_SHORT).show();
                try {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    }

                    if (loc != null) {
                        Log.d(TAG, "getCurrentLocation: " + loc.getLatitude() + ", " + loc.getLongitude());
                        return loc;
                    }
                } catch (SecurityException e) {

                }
            }
        }
        // if GPS Enabled get lat/long using GPS Services
        if (checkGPS) {
            Toast.makeText(mContext, "GPS", Toast.LENGTH_SHORT).show();
            if (loc == null) {
                try {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) {
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    }
                } catch (SecurityException e) {

                }
            }
        }
        Location locErr = null;
        return locErr;
    }




    @Override
    public void onLocationChanged(Location location) {

     if(mapFragment!=null){
 //   Toast.makeText(mapFragment.getActivity(), "Location Changed :"+location.getLatitude()+" "+
    //    location.getLongitude(), Toast.LENGTH_SHORT).show();
    if(mapFragment.currentmarker!=null){

              // LatLng currentpostion=new LatLng(location.getLatitude(),location.getLongitude());

              mapFragment.currentmarker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));

               // mapFragment.ridermarker.setPosition(currentpostion);

               // mapFragment.ridermarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.cartopsmall));
                //mapFragment.ridermarker.setRotation(location.getBearing());
                // mapFragment.currentGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(currentpostion));

             if(mapFragment.getcurrntJobfromHomeActivity() !=null){

                 mapFragment.getcurrntJobfromHomeActivity().update("currentcustomer_lat",location.getLatitude(),"currentcustomer_lon",location.getLongitude());

             }



//

    }


}
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


    private BitmapDescriptor getBitmapDesc(FragmentActivity activity, int ic_tracking) {
        Drawable LAYER_1 = ContextCompat.getDrawable(activity,ic_tracking);
        LAYER_1.setBounds(0, 0, LAYER_1.getIntrinsicWidth(), LAYER_1.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(LAYER_1.getIntrinsicWidth(), LAYER_1.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        LAYER_1.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



}