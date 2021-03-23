package com.lk.codefist2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lk.codefist2021.Model.Customer;

public class Registeractivity extends AppCompatActivity {

    Button Registerbutton;

    EditText namefield,emailfield,telephonefield,addressfield;

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity);


        namefield=findViewById(R.id.Rname);
        emailfield=findViewById(R.id.Remail);
        telephonefield=findViewById(R.id.Rmobile);
        addressfield=findViewById(R.id.Raddress);


        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("auth name");
        String email= bundle.getString("auth email");
        String mobile = bundle.getString("auth mobile");
        String googleauth = bundle.getString("googleauth");



        namefield.setText(name);
        emailfield.setText(email);
        telephonefield.setText(mobile);






        Registerbutton=findViewById(R.id.regis_btn);

        Registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String  Rname = namefield.getText().toString();
                String  Remail = emailfield.getText().toString();
                String  Rtelephone= telephonefield.getText().toString();
                String  Raddress = addressfield.getText().toString();

                Customer customer= new Customer(Rname,Remail,Rtelephone,Raddress,googleauth,1,null);

                db.collection("customers")
                        .add(customer)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Toast.makeText(Registeractivity.this,"Registration sucessfully",Toast.LENGTH_LONG).show();


                                Intent intent=new Intent(Registeractivity.this,HomeActivity.class);


                                intent.putExtra("hname",Rname);
                                intent.putExtra("hemail",Remail);


                                startActivity(intent);



                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                                Toast.makeText(Registeractivity.this,"Registration Faield",Toast.LENGTH_LONG).show();


                            }
                        });








            }
        });





    }
}