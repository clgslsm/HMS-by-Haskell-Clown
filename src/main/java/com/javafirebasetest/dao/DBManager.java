package com.javafirebasetest.dao;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

//SINGLETON DESIGN PATTERN

public class DBManager {
    private static DBManager instance;
    final public Firestore db;
    public enum CollectionPath {
        PATIENT("Patients"), STAFF("Staffs"), MEDICAL_RECORD("MedicalRecords"), MACHINE("Machines"), MEDICINE("Medicines"), USER("Users");
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
                    .setStorageBucket("ltnc-new.appspot.com")
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


    // Add a document to a collection
    public String addDocument(CollectionPath collectionPath, Map<String, Object> data){
        getInstance();
        CollectionReference colRef = db.collection(collectionPath.getValue());
        ApiFuture<DocumentReference> result = colRef.add(data);

        try {
            DocumentReference docRef = result.get();
            return result.get().getId();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("DBManager addDocument - cannot get added document" + e);
            return null;
        }
    }
    // Get a document by document ID

    public DocumentSnapshot getDocumentById(CollectionPath collectionPath, String documentId){
        getInstance();
        try {
            if (documentId == null) return null;
            DocumentReference docRef = db.collection(collectionPath.getValue()).document(documentId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("DBManager getDocumentById - cannot get document (ID does not exist?) " + e);
            return null;
        }
    }

    // Query documents based on certain conditions
    public List<QueryDocumentSnapshot> getDocumentsByConditions(CollectionPath collectionPath, Filter ... filters){
        getInstance();
        ApiFuture<QuerySnapshot> future = db.collection(collectionPath.getValue()).where(makeFilter(filters)).get();
        QuerySnapshot querySnapshot = null;

        try {
            querySnapshot = future.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("DBManager getDocumentsByConditions - cannot get document " + e);
            return null;
        }

        return querySnapshot.getDocuments();
    }

    // Query all document
    public List<QueryDocumentSnapshot> getAllDocuments(CollectionPath collectionPath) {
        getInstance();
        ApiFuture<QuerySnapshot> future = db.collection(collectionPath.getValue()).get();

        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = future.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("DBManager getAllDocuments - cannot get documents " + e);
            return null;
        }

        return querySnapshot.getDocuments();
    }

    // Update a document
    public void updateDocument(CollectionPath collectionPath, String documentId, Map<String, Object> newData){
        getInstance();
        try {
            //TO ADD DOCUMENT WITH CUSTOM ID
            if (!db.collection(collectionPath.getValue()).document(documentId).get().get().exists()){
                //SET new document with given id and blank data
                db.collection(collectionPath.getValue()).document(documentId).set(newData);
            }
            //SIMPLE UPDATE TO EXISTING DOCUMENT
            else{
                DocumentReference docRef = db.collection(collectionPath.getValue()).document(documentId);
                docRef.update(newData);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("DBManager updateDocument - cannot get document (ID does not exist?)" + e);
            return;
        }
    }

    // Delete a document
    public void deleteDocument(CollectionPath collectionPath, String documentId) {
        getInstance();
        DocumentReference docRef = db.collection(collectionPath.getValue()).document(documentId);
        try{
            docRef.get().get();
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("DBManager deleteDocument - cannot delete document (ID does not exist?)");
            return;
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
}
