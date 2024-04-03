package com.javafirebasetest.dao;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.dao.DBManager;
import com.javafirebasetest.entity.User;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.javafirebasetest.entity.HashPassword.getSHA;
import static com.javafirebasetest.entity.HashPassword.toHexString;

public class UserDAO {
    private static final DBManager dbManager = DBManager.getInstance();

    public static void createUser(String userName, String password, User.Mode mode) throws NoSuchAlgorithmException, ExecutionException, InterruptedException {
        // Create a map of user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("userName", userName);
        userData.put("password", getHashPassword(password)); // Store hashed password
        userData.put("userMode", mode);

        // Add the user to the database
        dbManager.addDocument(DBManager.CollectionPath.USER, userData);
    }
    // READ METHODS
    public static User getUserByUsername(String username) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> querySnapshot;
        try {
            querySnapshot = dbManager.getDocumentsByConditions(
                    DBManager.CollectionPath.USER,
                    Filter.equalTo("userName", username)
            );
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (querySnapshot.isEmpty()) {
            return null; // User not found
        }

        // Assuming username is unique, so there should be only one document in the result
        QueryDocumentSnapshot document = querySnapshot.getFirst();
        Map<String, Object> userData = document.getData();

        String password = (String) userData.get("password");
        User.Mode userMode = User.Mode.fromValue((String) userData.get("userMode"));
        return new User(username, password, userMode);

    }

    // Method to authenticate user by comparing hashed passwords
    public static boolean authenticateUser(User user, String enteredPassword) {
        try {
            String hashedEnteredPassword = getHashPassword(enteredPassword); // Hash entered password
            System.out.println(hashedEnteredPassword);
            return user != null && user.getPassword().equals(hashedEnteredPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getHashPassword(String enteredPassword) throws NoSuchAlgorithmException {
        return toHexString(getSHA(enteredPassword));
    }
}
