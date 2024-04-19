package com.javafirebasetest.entity;

import java.util.HashMap;
import java.util.Map;

public class Doctor extends Staff {
    public static Long PATIENT_LIMIT = 5L;
    private DeptType department;
    private Long patientCount;

    public Doctor() {
        super();
    }

    public Doctor(String doctorId, String name, DeptType department) {
        super(doctorId, name, User.Mode.DOCTOR);
        this.department = department;
        this.patientCount = 0L;
    }

    public Doctor(String doctorId, Map<String, Object> doctorData) {
        super(doctorId, (String) doctorData.get("name"), User.Mode.DOCTOR);
        this.staffId = doctorId;
        this.name = (String) doctorData.get("name");
        this.department = DeptType.fromValue((String) doctorData.get("department"));
        this.patientCount = (Long) doctorData.get("patientCount");
    }

    public DeptType getDepartment() {
        return department;
    }

    public void setDepartment(DeptType department) {
        this.department = department;
    }

    public Long getPatientCount() {
        return patientCount;
    }

    public void setPatientCount(Long patientCount) {
        this.patientCount = patientCount;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("userMode", userMode.getValue());
        map.put("department", department.getValue());
        map.put("patientCount", patientCount);
        return map;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "department=" + department +
                ", patientCount=" + patientCount +
                ", ID='" + staffId + '\'' +
                ", name='" + name + '\'' +
                ", userMode=" + userMode +
                '}';
    }
}
