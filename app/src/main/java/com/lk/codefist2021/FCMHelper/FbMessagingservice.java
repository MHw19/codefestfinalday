package com.lk.codefist2021.FCMHelper;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;

public class FbMessagingservice extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {


            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.

            } else {
                // Handle message within 10 seconds

            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());


            try {
                Toast.makeText(this.getApplicationContext(), "NEW Message : "+
                        remoteMessage.getNotification().getTitle()+" "
                        +remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }



        }

        // Also if you inten

    }
}
