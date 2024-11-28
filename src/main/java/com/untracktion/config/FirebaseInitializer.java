package com.untracktion.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseInitializer {
    public static void main(String[] args) {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/java/com/untracktion/config/untrack-db-firebase-adminsdk-ig80l-5ef75d5ddf.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://untrack-db-default-rtdb.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("your_node");

            ref.setValueAsync("Hello, Firebase!").addListener(() -> {
                System.out.println("Data written successfully.");
            }, Runnable::run);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("Data read: " + dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.err.println("Error reading data: " + databaseError.getMessage());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}