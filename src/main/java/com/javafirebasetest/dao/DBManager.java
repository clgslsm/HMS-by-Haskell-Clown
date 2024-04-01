package com.javafirebasetest.dao;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

//SINGLETON DESIGN PATTERN

public class DBManager {
    private static DBManager instance;
    final public Firestore db;



    public enum CollectionPath {
        PATIENT("Patients"), STAFF("Staffs"), MEDICAL_RECORD("MedicalRecords");
        private final String value;
        CollectionPath(String value) {
            this.value = value;
        }
        public String getValue() {return value;}
        public static CollectionPath fromValue(String value) {
            for (CollectionPath cp : CollectionPath.values())
                if (cp.value.equalsIgnoreCase(value)) return cp;
            throw new IllegalArgumentException("Invalid Collection path: " + value);
        }
    }

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

    public void populateData() {

    }

    public void setUp() throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("users").document("idk");
        Map<String, Object> data = new HashMap<>();
        data.put("first", "clgslsm");
        data.put("age", "15");
        data.put("born", 1815);
        ApiFuture<WriteResult> result = docRef.set(data);
        System.out.println("Update time : " + result.get().getUpdateTime());
    }

    // Add a document to a collection
    public void addDocument(CollectionPath collectionPath, Map<String, Object> data) {
        CollectionReference docRef = db.collection(collectionPath.getValue());
        ApiFuture<DocumentReference> result = docRef.add(data);
    }

    public void get() throws ExecutionException, InterruptedException {
        // asynchronously retrieve all users
        ApiFuture<QuerySnapshot> query = db.collection("users").get();
// ...
// query.get() blocks on response
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("User: " + document.getId());
            System.out.println("First: " + document.getString("first"));
            if (document.contains("middle")) {
                System.out.println("Middle: " + document.getString("middle"));
            }
            System.out.println("Last: " + document.getString("last"));
            System.out.println("Born: " + document.getLong("born"));
        }
    }
    // Get a document by document ID

    public DocumentSnapshot getDocumentById(CollectionPath collectionPath, String documentId) throws
            ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collectionPath.getValue()).document(documentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            return document;
        } else {
            return null;
        }
    }

    // Query documents based on certain conditions
    public List<QueryDocumentSnapshot> getDocumentsByConditions(CollectionPath collectionPath, Filter ... filters) throws
            ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = db.collection(collectionPath.getValue()).where(makeFilter(filters)).get();
        QuerySnapshot querySnapshot = future.get();

        return querySnapshot.getDocuments();
    }

    // Query all document
    public List<QueryDocumentSnapshot> getAllDocuments(CollectionPath collectionPath) {
        ApiFuture<QuerySnapshot> future = db.collection(collectionPath.getValue()).get();

        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("No document in " + collectionPath.getValue());
        }

        return querySnapshot.getDocuments();
    }

    // Update a document
    public void updateDocument(CollectionPath collectionPath, String documentId, Map<String, Object> newData){
        try {
            //TO ADD DOCUMENT WITH CUSTOM ID
            if (! db.collection(collectionPath.getValue()).document(documentId).get().get().exists()){
                //SET new document with given id and blank data
                db.collection(collectionPath.getValue()).document(documentId).set(newData);
            }
            //SIMPLE UPDATE TO EXISTING DOCUMENT
            else{
                DocumentReference docRef = db.collection(collectionPath.getValue()).document(documentId);
                docRef.update(newData);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("This is not supposed to happen :/" + e.toString());
        }

    }

    // Delete a document
    public void deleteDocument(CollectionPath collectionPath, String documentId) {
        DocumentReference docRef = db.collection(collectionPath.getValue()).document(documentId);
        try{
            docRef.get().get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
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
        // Generating random birthdate within the range of 1950 and 2003
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
