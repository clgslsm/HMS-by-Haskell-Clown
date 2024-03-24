package com.javafirebasetest;

import java.util.concurrent.ExecutionException;

public class Main {
    public static <Firestore> void main(String[] args) throws ExecutionException, InterruptedException {
        DBManager db = DBManager.getInstance();
        db.populateData();

        System.out.println("Hello world!");
    }
}