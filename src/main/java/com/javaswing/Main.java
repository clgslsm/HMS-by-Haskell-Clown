package com.javaswing;

import com.javafirebasetest.LoginManager;
import com.javafirebasetest.dao.MedRecDAO;
import com.javafirebasetest.dao.UserDAO;
import com.javafirebasetest.entity.MedicalRecord;
import com.javafirebasetest.entity.User;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        List<MedicalRecord> medRecList = MedRecDAO.getMedRecByCondition("doctorId", "0e8Z7qsDKt4DJEcWy7KN", "status", MedicalRecord.Status.PENDING.getValue());
        System.out.println(medRecList);
        medRecList = MedRecDAO.getMedRecByCondition("doctorId", "0e8Z7qsDKt4DJEcWy7KN", "status", MedicalRecord.Status.CHECKED.getValue());
        System.out.println(medRecList);
        medRecList = MedRecDAO.getMedRecByCondition("doctorId", "0e8Z7qsDKt4DJEcWy7KN", "status", MedicalRecord.Status.CHECKEDOUT.getValue());
        System.out.println(medRecList);

    }
}


