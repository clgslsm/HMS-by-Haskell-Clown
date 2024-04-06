package com.javafirebasetest.entity;

import com.google.cloud.Timestamp;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class MedicalRecord {
    public String medicalRecordID;
    public String patientId;
    public DeptType department;
    public String doctorId;

    public Timestamp checkIn;

    public Timestamp checkOut;
    public String observation;
    public Status status;
    public String serviceReview;
    public String prescription;

    public enum Status {
        PENDING("Pending"), CHECKED("Checked"), CHECKEDOUT("CheckedOut");
        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static MedicalRecord.Status fromValue(String value) {
            for (MedicalRecord.Status bg : MedicalRecord.Status.values())
                if (bg.value.equalsIgnoreCase(value)) return bg;
            throw new IllegalArgumentException("Invalid blood group: " + value);
        }
    }

    public MedicalRecord() {}

    //    String[] columnNames = {"Tên khoa", "Tên bác sĩ", "Thời gian vào", "Thời gian ra", "Chẩn đoán", "Trạng thái", "Đánh giá dịch vụ"};
    public MedicalRecord(String medicalRecordID, String patientId, DeptType department, String doctorId, Timestamp checkIn,
                         Timestamp checkOut, String observation, Status status, String serviceReview, String prescription) {
        this.medicalRecordID = medicalRecordID;
        this.patientId = patientId;
        this.department = department;
        this.doctorId = doctorId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.observation = observation;
        this.status = status;
        this.serviceReview = serviceReview;
        this.prescription = prescription;
    }

    public MedicalRecord(String medRecId, Map<String, Object> medRec) {
        super();
        this.medicalRecordID = medRecId;
        this.patientId = (String) medRec.get("patientId");
        this.department = DeptType.fromValue((String) medRec.get("department"));
        this.doctorId = (String) medRec.get("doctorId");
        this.checkIn = (Timestamp) medRec.get("checkIn");
        this.checkOut = (Timestamp) medRec.get("checkOut");
        this.observation = (String) medRec.get("observation");
        this.status = Status.fromValue((String) medRec.get("status"));
        this.serviceReview = (String) medRec.get("serviceReview");
        this.prescription = (String) medRec.get("prescription");
    }

    public String getmedicalRecordId() {
        return medicalRecordID;
    }

    public void setID(String medicalRecordId) {
        this.medicalRecordID = medicalRecordId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public DeptType getDepartment() {
        return department;
    }

    public void setDepartment(DeptType department) {
        this.department = department;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDid(String doctorId) {
        this.doctorId = doctorId;
    }

    public Timestamp getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Timestamp checkIn) {
        this.checkIn = checkIn;
    }

    public Timestamp getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Timestamp checkOut) {
        this.checkOut = checkOut;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getServiceReview() {
        return serviceReview;
    }

    public void setServiceReview(String serviceReview) {
        this.serviceReview = serviceReview;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }
    //endregion

    public String getformattedCheckIn() { // Hiển thị ngày tháng theo định dạng "dd/mm/yyyy"
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            return sdf.format(checkIn.toDate());
        } catch (Exception e) {
            return "null";
        }
    }

    public String getformattedCheckOut() { // Hiển thị ngày tháng theo định dạng "dd/mm/yyyy"
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            return sdf.format(checkOut.toDate());
        } catch (Exception e) {
            return "null";
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("patientId", getPatientId());
        map.put("department", getDepartment().getValue());
        map.put("doctorId", getDoctorId());
        map.put("checkIn", getCheckIn());
        map.put("checkOut", getCheckOut());
        map.put("observation", getObservation());
        map.put("status", getStatus().getValue());
        map.put("serviceReview", getServiceReview());
        map.put("prescription", getPrescription());

        return map;
    }

    @Override
    public String toString() {
        return "MedicalRecord [medicalRecordId=" + medicalRecordID + ", patientId=" + patientId + ", department=" +
                department.getValue() + ", doctorId=" + doctorId + ", checkIn=" + getformattedCheckIn() + ", checkOut=" +
                getformattedCheckOut() + ", observation=" + observation + ", status=" + status.getValue() +
                ", serviceReview=" + serviceReview + ", prescription=" + prescription + "]";
    }
}
