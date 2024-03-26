package com.javafirebasetest.dao;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.javafirebasetest.entity.Patient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReferenceArray;

//SINGLETON DESIGN PATTERN

public class DBManager {
    private static DBManager instance;
    final public Firestore db;

    private DBManager() {
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
    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public void populateData() throws ExecutionException, InterruptedException {
    }

    // Add a document to a collection
    public void addDocument(String collectionPath, Map<String, Object> data) {
        db.collection(collectionPath).add(data);
    }

    // Get a document by document ID
    public Map<String, Object> getDocumentById(String collectionPath, String documentId) throws
            ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collectionPath).document(documentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document.getData();
        } else {
            return null;
        }
    }

    // Query documents based on certain conditions
    public ArrayList<Patient> getDocumentsByConditions(String collectionPath, Filter filter) throws
            ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = db.collection(collectionPath).where(filter).get();
        QuerySnapshot querySnapshot = future.get();

        ArrayList<Patient> res = new ArrayList<>();

        for (QueryDocumentSnapshot document : querySnapshot) {
            res.add(new Patient(document.getData()));
        }
        return res;
    }

    // Update a document
    public void updateDocument(String collectionPath, String documentId, Map<String, Object> newData) {
        DocumentReference docRef = db.collection(collectionPath).document(documentId);
        docRef.update(newData);
    }

    // Delete a document
    public void deleteDocument(String collectionPath, String documentId) {
        DocumentReference docRef = db.collection(collectionPath).document(documentId);
        docRef.delete();
    }

    public static Filter makeFilter(Filter ... filters){
        Filter res = null;
        for (Filter filter : filters){
            if (res == null) res = filter;
            else res = Filter.and(res, filter);
        }

        return res;
    }

    // Method to generate a random purchase date
    private static Date generateRandomDate() {
        Random rand = new Random();
        Calendar calendar = Calendar.getInstance();
        calendar.set(rand.nextInt(10) + 2010, rand.nextInt(12), rand.nextInt(28) + 1); // Random date between 2010 and current year
        return calendar.getTime();
    }

    private static LocalDate generateRandomBirthDate() {
        // Generating random birth date within the range of 1950 and 2003
        int year = 1950 + new Random().nextInt(54); // 1950 + random between 0 and 53
        int month = 1 + new Random().nextInt(12); // Random month between 1 and 12
        int day = 1 + new Random().nextInt(28); // Random day between 1 and 28 (assuming February)

        return LocalDate.of(year, month, day);
    }

    private static String generateRandomPhoneNumber() {
        // Generating random phone number (dummy implementation)
        // You can replace this with a more realistic phone number generator
        StringBuilder phoneNumber = new StringBuilder("+");

        for (int i = 0; i < 10; i++) {
            phoneNumber.append(new Random().nextInt(10));
        }

        return phoneNumber.toString();
    }
}
