package com.lk.codefist2021;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.lk.codefist2021.directionsLib.FetchURL;
import com.lk.codefist2021.pojo.Mapdistanceobj;
import com.lk.codefist2021.pojo.Maptimeobj;

public class MapsFragment extends Fragment {

    FusedLocationProviderClient fusedLocationProviderClient;

    static final int LOCATION_PERMISSION = 100;

    GoogleMap cgoogleMap;

    public  Marker ridermarker;
    public  Marker currentmarker;
   // public  Marker ridermarker;






    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * link :https://forms.gle/QLrLYmAquwnRhiNHA
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        @Override
        public void onMapReady(GoogleMap googleMap) {


            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsFragment.super.getContext());
            cgoogleMap = googleMap;


            updateCustomerLocation();




//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        }


    };
    private Marker destnationmarker;

//    private void requestPermissions(FragmentActivity activity, String[] strings, int locationPermission) {
//
//        if (locationPermission == LOCATION_PERMISSION) {
//            if (activity.RESULT_OK == FragmentActivity.RESULT_OK) {
//
//
//                updateCustomerLocation();
//            }
//
//
//        }
//
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION) {
            if (permissions.length>0) {


                updateCustomerLocation();
            }


        }


    }

    private void updateCustomerLocation() {

        if (ActivityCompat.checkSelfPermission(MapsFragment.super.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsFragment.super.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // write permission request


            requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION);

            return;
        } else {

           // updateCustomerLocation();


        }


        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {

            private  LatLng customerlocation;
            private  LatLng droplocation;
             private  Polyline polyline;


            @Override
            public void onSuccess(Location location) {



                if(location !=null){

                    Toast.makeText(MapsFragment.super.getContext(),""+location.getAltitude()+""+location.getLatitude(),Toast.LENGTH_LONG).show();





                     customerlocation=new LatLng(location.getLatitude(),location.getLongitude());
                     droplocation=new LatLng(location.getLatitude(),location.getLongitude());


                     BitmapDescriptor iconcurrent=getBitmapDesc(getActivity(),R.drawable.ic_walkto);
                     BitmapDescriptor  icontrack=getBitmapDesc(getActivity(),R.drawable.ic_tracking);



                     MarkerOptions currentposition=new MarkerOptions().draggable(false).position(customerlocation).title("I am Here").icon(iconcurrent);
                     MarkerOptions destinationposition=new MarkerOptions().draggable(true).position(customerlocation).title("I want to go ").icon(icontrack);


                   // cgoogleMap.addMarker(new MarkerOptions().position(customerlocation).title("I am here"));
                 currentmarker = cgoogleMap.addMarker(currentposition);
                  destnationmarker= cgoogleMap.addMarker(destinationposition);

                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user))

                    cgoogleMap.moveCamera(CameraUpdateFactory.newLatLng(customerlocation));
                    cgoogleMap.moveCamera(CameraUpdateFactory.zoomTo(12));



                    cgoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {

                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {

                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {

                            droplocation=marker.getPosition();

                          //  cgoogleMap.addPolyline(new PolylineOptions().add(customerlocation,droplocation));
                            ( (HomeActivity) getActivity()).setLatlng(customerlocation,droplocation);

                           new FetchURL() {
                               @Override
                               public void onTaskDone(Object... values) {

                               if(polyline !=null){
                                   polyline.remove();


                               }

                                   polyline=cgoogleMap.addPolyline((PolylineOptions) values[0]);

                                   //values[0]


                               }

                               @Override
                               public void onTimeTaskDone(Maptimeobj maptimeobj) {

                                   ( (HomeActivity) getActivity()).setDuration(maptimeobj.getTimetext());

                               }

                               @Override
                               public void onDistanceTaskDone(Mapdistanceobj mapdistanceobj) {

                                   double startfee=60;
                                   double addtionfeeperkm=40;

                                   int distancevalue = mapdistanceobj.getDistancevalue();

                                   double estimatedPrice=0.0;
                                   double addionalm=0.0;
                                   if(distancevalue>1000){
                                       addionalm=distancevalue-1000;

                                       double adtionalPrice = ((int)(addionalm/1000)) * addtionfeeperkm;
                                       estimatedPrice = startfee+ adtionalPrice;

                                   }else{

                                       estimatedPrice=startfee;

                                   }

                                   ( (HomeActivity) getActivity()).setEstimatedvalue(estimatedPrice);

                               }
                           }.execute(getUrl(customerlocation,droplocation,"driving"),"driving");



                        }
                    });





                }else{


                    Toast.makeText(MapsFragment.super.getContext(),"Null object",Toast.LENGTH_LONG).show();



                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MapsFragment.super.getContext(),"Error"+e.toString(),Toast.LENGTH_LONG).show();

            }
        });


    }

    private BitmapDescriptor getBitmapDesc(FragmentActivity activity, int ic_tracking) {
        Drawable LAYER_1 = ContextCompat.getDrawable(activity,ic_tracking);
        LAYER_1.setBounds(0, 0, LAYER_1.getIntrinsicWidth(), LAYER_1.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(LAYER_1.getIntrinsicWidth(), LAYER_1.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        LAYER_1.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        GPSHelper gpsHelper=new GPSHelper(this);
        Location loca = gpsHelper.getCurrentLocationListner(getContext());



    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        Log.d("url","URL:"+url);
        return url;
    }



    public  void showRiderLocation(double lat,double lon){

//        Toast.makeText(getActivity(), "Rider Location updating...", Toast.LENGTH_SHORT).show();


        if(cgoogleMap!=null){

            if(ridermarker ==null){



                ridermarker=  cgoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title("Rider").icon(BitmapDescriptorFactory.fromResource(R.drawable.cartopsmall)));




            }else{


                ridermarker.setPosition(new LatLng(lat,lon));
            }


        }else{


        Log.d("Maps","Maps not Ready...");
            // Toast.makeText(Ma, "", Toast.LENGTH_SHORT).show();


        }



    }



   public DocumentReference getcurrntJobfromHomeActivity(){

        return  ((HomeActivity)getActivity()).getcurrentJobDoc();
 }


    public void showCustomer(double cuslat, double cuslon) {
        if(cgoogleMap !=null){

            if(currentmarker ==null){

                BitmapDescriptor iconcurrent=getBitmapDesc(getActivity(),R.drawable.ic_walkto);


                currentmarker=cgoogleMap.addMarker(new MarkerOptions().position(new LatLng(cuslat,cuslon)).title("customer").icon(iconcurrent));
            }else{

                currentmarker.setPosition(new LatLng(cuslat,cuslon));

            }




        }



    }


    public void showdropLocation(double droplat, double droplon) {


        if(cgoogleMap !=null){


            if(destnationmarker ==null){


                BitmapDescriptor  icontrack=getBitmapDesc(getActivity(),R.drawable.ic_tracking);

                destnationmarker=cgoogleMap.addMarker(new MarkerOptions().position(new LatLng(droplat,droplon)).title("drop..").icon(icontrack));

            }else{

                destnationmarker.setPosition(new LatLng(droplat,droplon));


            }





        }


    }
}