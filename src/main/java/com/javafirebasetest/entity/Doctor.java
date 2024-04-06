package com.javafirebasetest.entity;

import com.google.cloud.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class Doctor extends Staff {
    private DeptType department;
    private Long patientCount;
    protected static Doctor instanceDoctor;
    private Doctor() {super();}
    private Doctor(String username, String password, String id, String name, DeptType department) {
        super(username, password, User.Mode.DOCTOR, id, name);
        this.department = department;
    }
    public Doctor(String doctorId, Map<String, Object> doctorData) {
        this.ID = doctorId;
        this.name = (String) doctorData.get("name");
        this.department = DeptType.fromValue((String) doctorData.get("department"));
        this.patientCount = (Long) doctorData.get("patientCount");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("department", department.getValue());
        map.put("patientCount", patientCount);

        return map;
    }

    public static Doctor getInstanceDoctor() {
        if (instanceDoctor == null) instanceDoctor = new Doctor();
        return instanceDoctor;
    }
    public static Doctor getInstanceDoctor(String username, String password, String id, String name, DeptType department) {
        if (instanceDoctor == null) instanceDoctor = new Doctor(username, password, id, name, department);
        return instanceDoctor;
    }
    @Override
    public String toString() {
        return STR."doctor [ID=\{getID()}, name=\{getName()}, department=\{department.getValue()}, patientCount=\{patientCount}]";
    }
}
