package com.javafirebasetest.dao;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileManager {
    private static FileManager instance;
    private static Storage storage;
    private static Bucket defaultBucket;

    private static String localDataPath = "./HMSAppLocalData/";
    //HARD-CODED PATH
    private static String storageDataPath = "MedicalRecords/";

    public FileManager() {
        //Make dir if it doesnt exist, try to delete after the program terminates
        File localPath = new File(localDataPath);
        if (!localPath.exists()) localPath.mkdir();
        localPath.deleteOnExit();

        FileInputStream serviceAccount;
        try {
            serviceAccount = new FileInputStream("./serviceAccountKeyNew.json");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build()
                    .getService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        defaultBucket = StorageClient.getInstance().bucket();
    }

    public static FileManager getInstance() {
        if (instance == null) {
            DBManager.getInstance();
            instance = new FileManager();
        }
        return instance;
    }

    public static String uploadFile(String localFilePath) {
        getInstance();
        String[] paths = separateFolderPathAndFileName(localFilePath);

        BlobInfo blobInfo = Blob.newBuilder(defaultBucket, storageDataPath + paths[1])
                                .build();
        Blob newBlob = null;
        try {
            newBlob = storage.create(blobInfo, Files.readAllBytes(Path.of(localFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return newBlob.getBlobId().getName();
    }

    public static String uploadFile(String localFilePath, String medRecId) {
        getInstance();
        String[] paths = separateFolderPathAndFileName(localFilePath);
        medRecId += '/';

        BlobInfo blobInfo = Blob.newBuilder(defaultBucket, storageDataPath + medRecId + paths[1])
                .build();
        Blob newBlob = null;
        try {
            newBlob = storage.create(blobInfo, Files.readAllBytes(Path.of(localFilePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return newBlob.getBlobId().getName();
    }

    // Downloads a file from Firebase Storage
    public static String downloadFile(String storagePath){
        getInstance();
        BlobId blobId = BlobId.of(defaultBucket.getName(), storagePath);
        Blob newBlob = storage.get(blobId);

        //Validate and create local save path
        String[] paths = separateFolderPathAndFileName(storagePath);
        File savePath = new File(localDataPath + paths[0]);
        if (!savePath.exists()) savePath.mkdirs();

        newBlob.downloadTo(Path.of(localDataPath + storagePath));
        System.out.println("File downloaded to LocalData.");
        return localDataPath + storagePath;
    }

    //OPEN FILE SAVED IN LOCALDATA, DOWNLOAD IF DOESNT EXIST
    public static void openFileWithDefaultApp(String filePath){
        try {
            // Create a File object representing the file
            File file = new File(localDataPath + filePath);

            if (!file.exists()){
                downloadFile(filePath);
            }

            // Check if Desktop is supported on the current platform
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();

                // Check if the file exists
                if (file.exists()) {
                    // Open the file with the default application
                    desktop.open(file);
                } else {
                    System.err.println("File does not exist: " + filePath);
                }
            } else {
                System.err.println("Desktop is not supported.");
            }
        } catch (IOException e) {
            System.err.println("Failed to open file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Deletes a file from Firebase Storage
    public static void deleteFile(String storagePath){
        getInstance();
        BlobId blobId = BlobId.of(defaultBucket.getName(), storagePath);
        boolean res = storage.delete(blobId);
        System.out.println((res)? "File " + storagePath + " deleted successfully.": "Could not delete file " + storagePath);
    }

    private static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
    public static void cleanUp() {
        boolean result = deleteDirectory(new File(localDataPath));
        System.out.println("Locally downloaded file cleanup result: " + result);
    }

    public static String[] separateFolderPathAndFileName(String storagePath) {
        String[] segments = storagePath.split("[\\\\/]");
        String fileName = segments[segments.length - 1];
        String folderPath = String.join("/", Arrays.copyOf(segments, segments.length - 1));
        return new String[]{folderPath, fileName};
    }
}
