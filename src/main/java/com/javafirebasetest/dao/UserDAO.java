package com.javafirebasetest.dao;

import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.javafirebasetest.entity.User;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.javafirebasetest.entity.HashPassword.getSHA;
import static com.javafirebasetest.entity.HashPassword.toHexString;

public class UserDAO {
    private static final DBManager dbManager = DBManager.getInstance();

    static String idPrefix = "US";
    //CRUD

    //CREATE METHODS

    //USER SPECIAL ADD
    public static String addUser(User user) {
        String hexId = null;
        String newUserId = null;

        if (getUserByUsername(user.getUsername()) != null)
            throw new RuntimeException("Username already exist");
        try {
            hexId = toHexString(getSHA(LocalDateTime.now().toLocalTime().toString()));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }

        newUserId = idPrefix + hexId.substring(hexId.length() - (DBManager.idHashLength));

        dbManager.updateDocument(DBManager.CollectionPath.USER, newUserId, user.toMap());

        return newUserId;
//        if (user.getUserId() == null) {
//            if (getUserByUsername(user.getUsername()) != null)
//                throw new RuntimeException("Username already exist");
//            return dbManager.addDocument(DBManager.CollectionPath.USER, user.toMap());
//        } else {
//            dbManager.updateDocument(DBManager.CollectionPath.USER, user.getUserId(), user.toMap());
//            return user.getUserId();
//        }
    }

    //READ METHODS
    public static User getUserById(String userId) {
        Map<String, Object> userData = dbManager.getDocumentById(DBManager.CollectionPath.USER, userId).getData();
        assert userData != null;
        return new User(userId, userData);
    }

    //100% unique
    public static User getUserByUsername(String username) {
        List<QueryDocumentSnapshot> querySnapshot;

        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.USER,
                Filter.equalTo("username", username)
        );

        List<User> userList = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            userList.add(new User(qds.getId(), qds.getData()));
        }

        if (userList.isEmpty()) return null;

        return userList.getFirst();
    }

    public static User getUserByUsernamePassword(String username, String password) {
        List<QueryDocumentSnapshot> querySnapshot;

        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.USER,
                Filter.equalTo("username", username),
                Filter.equalTo("password", User.hashPassword(password))
        );

        List<User> userList = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            userList.add(new User(qds.getId(), qds.getData()));
        }

        if (userList.isEmpty()) return null;

        return userList.getFirst();
    }

    public static List<User> getUserByUserMode(User.Mode userMode) {
        List<QueryDocumentSnapshot> querySnapshot;

        querySnapshot = dbManager.getDocumentsByConditions(
                DBManager.CollectionPath.USER,
                Filter.equalTo("userMode", userMode)
        );

        List<User> userList = new ArrayList<>();

        for (QueryDocumentSnapshot qds : querySnapshot) {
            userList.add(new User(qds.getId(), qds.getData()));
        }
        return userList;
    }

    public static List<User> getAllUser() {
        List<QueryDocumentSnapshot> querySnapshot;
        querySnapshot = dbManager.getAllDocuments(DBManager.CollectionPath.USER);

        List<User> userData = new ArrayList<>();
        for (QueryDocumentSnapshot qds : querySnapshot) {
            userData.add(new User(qds.getId(), qds.getData()));
        }

        return userData;
    }

    //SPECIAL UPDATE TO HASH PASSWORD
    public static void updateUser(String userId, Object... fieldsAndValues) {
        Map<String, Object> newData = new HashMap<>();
        for (int i = 0; i < fieldsAndValues.length; i += 2) {
            //Forbid editting username
            if (fieldsAndValues[i].toString().equalsIgnoreCase("username")){
                throw new RuntimeException("Updating username is not allowed!");
            }
            if (fieldsAndValues[i].toString().equalsIgnoreCase("password")){
                fieldsAndValues[i + 1] = User.hashPassword((String) fieldsAndValues[i + 1]);
            }
            newData.put((String) fieldsAndValues[i], fieldsAndValues[i + 1]);
        }
        dbManager.updateDocument(DBManager.CollectionPath.USER, userId, newData);
    }

    //DELETE METHODS
    public static void deleteUserById(String userId) {
        dbManager.deleteDocument(DBManager.CollectionPath.USER, userId);
    }
}
