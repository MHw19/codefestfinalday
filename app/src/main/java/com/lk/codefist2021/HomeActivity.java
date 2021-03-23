package com.lk.codefist2021;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.lk.codefist2021.Model.Customer;
import com.lk.codefist2021.Model.Job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    Button logout,placejob;

    TextView nameview,emailview,status;


   TextView estmateprice,estmatedduration;
    private LatLng customerlocation;
    private LatLng droplocation;
    double estimatedprices;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private double estiprice;
    private String userDocId;
    private String email;
    private String name;
    private String  mobile;
    private String  userid;

    public CollectionReference jobcollection;
    public CollectionReference customercollection;
    private Customer setcurrentcustomer;

    public  Job currentjob;
    private DocumentReference currentjobdoceRef;

    FragmentManager fm=getSupportFragmentManager();
    private MapsFragment mapfragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mapfragment = (MapsFragment)fm.findFragmentById(R.id.customer_map_fragment);

        nameview=findViewById(R.id.homenamefield);
        emailview=findViewById(R.id.homeemailfield);

        estmateprice=findViewById(R.id.estmatedprice);
        estmatedduration=findViewById(R.id.estimatedtime);
         placejob=findViewById(R.id.placejobbtn);
         status=findViewById(R.id.status_lbl);

        logout=findViewById(R.id.hlogout);

        Bundle bundle = getIntent().getExtras();

         email = bundle.getString("hemail");
         name = bundle.getString("hname");
         mobile = bundle.getString("mobile");
        userid = bundle.getString("googleauth");
        userDocId = bundle.getString("userDocId");





        customercollection=db.collection("customers");
        jobcollection=db.collection("Job");


        // current customer
        getcurrentcustomerDoc();
        loadCurrentcustomer();



        // try  to load current JOB

      getcurrentJobDoc();



        nameview.setText(name);
        emailview.setText(email);


   placejob.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {

           placejob.setEnabled(false);
           placejob.setText("Processing...");


           Job newJob=new Job();

           newJob.setCustomerName(name);
           newJob.setJobcreatedAt(new Date(System.currentTimeMillis()));

           if((customerlocation ==null) || (droplocation==null)){

               Toast.makeText(HomeActivity.this, "Select Your Location", Toast.LENGTH_SHORT).show();
               return;
           }

           newJob.setStartlocation_lat(customerlocation.latitude);
           newJob.setStartlocation_lon(customerlocation.longitude);
           newJob.setEndlocation_lat(droplocation.latitude);
           newJob.setEndlocation_lon(droplocation.longitude);

           newJob.setStatus("Job Placed");
           newJob.setTpNumber(mobile);
           newJob.setCustomerID(userDocId);
           newJob.setTimeminutes(estmatedduration.getText().toString());

           newJob.setCurrentcustomer_lat(customerlocation.latitude);
           newJob.setCurrentcustomer_lon(customerlocation.longitude);

           newJob.setEmail(email);

           newJob.setStatustime(new Date(System.currentTimeMillis()));

           newJob.setEstimatedprice(estiprice);


           db.collection("Job").add(newJob).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
               @Override
               public void onSuccess(DocumentReference documentReference) {


                   placejob.setEnabled(true);
                   placejob.setText("Cancle");
                   Toast.makeText(HomeActivity.this, "Sucessfully", Toast.LENGTH_SHORT).show();


                   jobEngine(documentReference);




               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   placejob.setEnabled(true);
                   placejob.setText("Place A Job");
                   Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_SHORT).show();

               }
           });




       }
   });




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                signOut();


            }
        });


   // placejob.setOnClickListener(new );






    }

    private void jobEngine(DocumentReference documentReference) {

        currentjobdoceRef=documentReference;

        currentjobdoceRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                Job cjob = value.toObject(Job.class);
                if(cjob.getStatus().equals("Job Placed")){

                    status.setText("Job Placed");


                }else if(cjob.getStatus().equals("Rider Accepted")){

                    status.setText("Rider Accepted");
                    Log.d("TAG Home",cjob.getCurrentRider_lat()+""+cjob.getCurrentRider_lon());

                    mapfragment.showRiderLocation(cjob.getCurrentRider_lat(),cjob.getCurrentRider_lon());


                }else if(cjob.getStatus().equals("pickup")){

                    status.setText("pickup");
                   // Log.d("TAG Home",cjob.getCurrentRider_lat()+""+cjob.getCurrentRider_lon());

                    mapfragment.showRiderLocation(cjob.getCurrentRider_lat(),cjob.getCurrentRider_lon());

                }else if(cjob.getStatus().equals("DropOff")){

                    status.setText("DropOff");

                }



            }
        });






    }

    public DocumentReference getcurrentJobDoc() {


                List<String> statuslist=new ArrayList<>();

               statuslist.add("Rider Accepted");
               statuslist.add("Job Placed");

          jobcollection.whereEqualTo("customerID",userDocId).whereIn("status",statuslist).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();

                    if(documentSnapshots.size()>0){

                        DocumentSnapshot d = documentSnapshots.get(0);

                        if(d.exists()){

                            Job job = d.toObject(Job.class);

                            double cuslat = job.getCurrentcustomer_lat();
                            double cuslon = job.getCurrentcustomer_lon();
                            double droplat = job.getEndlocation_lat();
                            double droplon = job.getEndlocation_lon();

                            setCurrentJob(d.getId(),(Job) job);

                            status.setText(job.getStatus());
                            estmateprice.setText(job.getEstimatedprice()+"");
                            estmatedduration.setText(job.getTimeminutes());

                          //  placejob.setEnabled(true);
                            placejob.setText("Cancle");


                            if(status.getText().equals("Job Placed")){

                                mapfragment.showCustomer(cuslat,cuslon);
                                mapfragment.showdropLocation(droplat,droplon);
                            }else if (status.getText().equals("")){



                            }





                            Toast.makeText(HomeActivity.this, "one current JOB", Toast.LENGTH_SHORT).show();

                            return;

                        }else{

                            Toast.makeText(HomeActivity.this, "No current JOB", Toast.LENGTH_SHORT).show();

                        }

                    }



                }







            }
        });


        return null;
    }

    private void setCurrentJob(String id, Job toObject) {

         currentjobdoceRef = jobcollection.document(id);
        // 1.
       //  jobEngine(currentjobdoceRef);

        //2.
//         currentjobdoceRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//             @Override
//             public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                 Job cjob = value.toObject(Job.class);
//                 if(cjob.getStatus().equals("Job Placed")){
//
//                     status.setText("Job Placed");
//
//
//                 }else if(cjob.getStatus().equals("Rider Accepted")){
//
//                     status.setText("Rider Accepted");
//                     Log.d("TAG Home",cjob.getCurrentRider_lat()+""+cjob.getCurrentRider_lon());
//                 }
//
//
//             }
//         });
         currentjob=toObject;

    }

    private void loadCurrentcustomer() {
        String currentJobId="";
        getcurrentcustomerDoc().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){

                    Customer customer = documentSnapshot.toObject(Customer.class);

                    setCurrentCustomer(customer);

                }else{

                    Toast.makeText(HomeActivity.this, "Error customer Loading..", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void setCurrentCustomer(Customer customer) {

        this.setcurrentcustomer=customer;
    }

    private DocumentReference getcurrentcustomerDoc() {

        return customercollection.document(userDocId);


    }


    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent=new Intent(HomeActivity.this,MainActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                        finish();

                    }
                });
        // [END auth_fui_signout]
    }

    public void setDuration(String timetext) {

        estmatedduration.setText(timetext);

    }

    public void setEstimatedvalue(double estimatedPrice) {
      this.estiprice=estimatedPrice;

    estmateprice.setText("RS:"+estiprice);

    }


    public void setLatlng(LatLng customerlocation, LatLng droplocation) {
        this.customerlocation=customerlocation;
        this.droplocation=droplocation;


    }
}