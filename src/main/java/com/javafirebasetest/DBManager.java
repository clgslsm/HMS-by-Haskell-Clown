package com.javafirebasetest;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

//SINGLETON DESIGN PATTERN

public class DBManager {
    private static DBManager instance;
    private com.google.cloud.firestore.Firestore db;
    private DBManager(){
        FileInputStream serviceAccount;
        try {
            serviceAccount = new FileInputStream("./serviceAccountKey.json");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        FirebaseOptions options;
        try {
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://ltnc-48c50-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FirebaseApp.initializeApp(options);

        db = FirestoreClient.getFirestore();
    }

    //PUBLIC
    public static DBManager getInstance(){
        if (instance == null){
            instance = new DBManager();
        }
        return instance;
    }

    public void populateData() throws ExecutionException, InterruptedException {
    }

    // Method to generate a random purchase date
    private static Date generateRandomDate() {
        Random rand = new Random();
        Calendar calendar = Calendar.getInstance();
        calendar.set(rand.nextInt(10) + 2010, rand.nextInt(12), rand.nextInt(28) + 1); // Random date between 2010 and current year
        return calendar.getTime();
    }

}
