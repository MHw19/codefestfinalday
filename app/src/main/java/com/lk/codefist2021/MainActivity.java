package com.lk.codefist2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lk.codefist2021.Model.Customer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN =132;
    private static final String TAG = "MainActivityToken";


    SignInButton loginbutton;
    String FCMToken="";



    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginbutton = findViewById(R.id.login_btn);


        intFCM();

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSignInIntent();



            }
        });


    }

    private  void intFCM(){

        // get Existing Token

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                
                
                if(task.isSuccessful()){

                    FCMToken=task.getResult();

                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();

                }
                
                

                

            }
        });



    }





        public void createSignInIntent() {
            // [START auth_fui_create_intent]
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(

                    new AuthUI.IdpConfig.GoogleBuilder().build());


            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
            // [END auth_fui_create_intent]
        }

        // [START auth_fui_result]
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
                IdpResponse response = IdpResponse.fromResultIntent(data);

                if (resultCode == RESULT_OK) {
                    // Successfully signed in
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    String email = user.getEmail();
                    String name = user.getDisplayName();
                    String mobile = user.getPhoneNumber();
                    String googleauth = user.getUid();





                    boolean isAlreadyregisterd=false;




                    db.collection("customers").whereEqualTo("email",email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                            if(document.exists()){
                                String customer_Id = document.getId();
                                Customer customer = document.toObject(Customer.class);

                                updateFCMToken(customer_Id);

                                Intent intent =new Intent(MainActivity.this,HomeActivity.class);

                                intent.putExtra("hname",name);
                                intent.putExtra("hemail",email);
                                intent.putExtra("mobile",customer.getTelephone());
                                intent.putExtra("googleauth",googleauth);
                                intent.putExtra("userDocId",customer_Id);

                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                                startActivity(intent);




                            }else{

                                Intent intent =new Intent(MainActivity.this,Registeractivity.class);

                                intent.putExtra("auth name",name);
                                intent.putExtra("auth email",email);
                                intent.putExtra("auth mobile",mobile);
                                intent.putExtra("googleauth",googleauth);

                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                                startActivity(intent);

                            }










                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });






                    if(!isAlreadyregisterd){






                    }else{





                    }















                    // ...
                } else {
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    // ...



                }
            }
        }

    private void updateFCMToken(String customer_Id) {

       db.collection("customers").document(customer_Id).update("fcmId",FCMToken).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {

               Log.d(TAG,FCMToken);

           }
       });


    }
    // [END auth_fui_result]

        public void signOut() {
            // [START auth_fui_signout]
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
            // [END auth_fui_signout]
        }

        public void delete() {
            // [START auth_fui_delete]
            AuthUI.getInstance()
                    .delete(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
            // [END auth_fui_delete]
        }

        public void themeAndLogo() {
            List<AuthUI.IdpConfig> providers = Collections.emptyList();

            // [START auth_fui_theme_logo]
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.ic_user_signup)      // Set logo drawable
                           // .setTheme(R.style.MySuperAppTheme)      // Set theme
                            .build(),
                    RC_SIGN_IN);
            // [END auth_fui_theme_logo]
        }

        public void privacyAndTerms() {
            List<AuthUI.IdpConfig> providers = Collections.emptyList();
            // [START auth_fui_pp_tos]
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setTosAndPrivacyPolicyUrls(
                                    "https://example.com/terms.html",
                                    "https://example.com/privacy.html")
                            .build(),
                    RC_SIGN_IN);
            // [END auth_fui_pp_tos]
        }



}