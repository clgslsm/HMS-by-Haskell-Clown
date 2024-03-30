import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.javafirebasetest.dao.DBManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public void main() throws IOException, ExecutionException, InterruptedException {
    DBManager db = DBManager.getInstance();
    Map<String, Object> data = new HashMap<>();
    data.put("first", "tranyhala");
    data.put("last", "tnun");
    data.put("born", 2016);
    db.addDocument(DBManager.CollectionPath.PATIENT, data);
}