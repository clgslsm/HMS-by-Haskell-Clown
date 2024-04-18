package com.javafirebasetest.entity;

import com.google.cloud.Timestamp;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class MedicalRecord {
    private String medRecId;
    private String patientId;
    //private DeptType department;
    private String doctorId;
    private String receptionistId;
    private Timestamp checkIn;
    private Timestamp checkOut;
    private Status status;
    private int serviceRating;
    private TestResult testResult;

    public enum Status {
        PENDING("Pending"), TESTING("Testing"), TESTED("Tested"), DIAGNOSED("Diagnosed"), CHECKED_OUT("Checked out");
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
            throw new IllegalArgumentException("Invalid Medical Record status: " + value);
        }
    }

    public MedicalRecord() {
    }

    //    String[] columnNames = {"Tên khoa", "Tên bác sĩ", "Thời gian vào", "Thời gian ra", "Chẩn đoán", "Trạng thái", "Đánh giá dịch vụ"};
//    public MedicalRecord(String medicalRecordID, String patientId, String doctorId, String receptionistId, Timestamp checkIn,
//                         Timestamp checkOut, Status status, int serviceRating, TestResult testResult) {
//        this.medicalRecordID = medicalRecordID;
//        this.patientId = patientId;
//        this.doctorId = doctorId;
//        this.receptionistId = receptionistId;
//        this.checkIn = checkIn;
//        this.checkOut = checkOut;
//        this.status = status;
//        this.serviceRating = serviceRating;
//        this.testResult = testResult;
//    }

    public MedicalRecord(String medRecId, String patientId, String doctorId, String receptionistId, Timestamp checkIn,
                         Timestamp checkOut, Status status, int serviceRating, TestResult testResult) {
        this.medRecId = medRecId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.receptionistId = receptionistId;
        this.checkIn = (checkIn == null)? Timestamp.now() : checkIn;
        this.checkOut = checkOut;
        this.status = (status == null)? Status.PENDING : status;
        this.serviceRating = serviceRating;
        this.testResult = testResult;
    }

    public MedicalRecord(String medRecId, Map<String, Object> medRec) {
        super();
        this.medRecId = medRecId;
        this.patientId = (String) medRec.get("patientId");
        this.doctorId = (String) medRec.get("doctorId");
        this.receptionistId = (String) medRec.get("receptionistId");
        this.checkIn = (Timestamp) medRec.get("checkIn");
        this.checkOut = (Timestamp) medRec.get("checkOut");
        this.status = Status.fromValue((String) medRec.get("status"));
        this.serviceRating = (int) medRec.get("serviceRating");

        this.testResult = new TestResult((Map<String, Object>) medRec.get("testResult"));
    }

    public String getMedRecId() {
        return medRecId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getReceptionistId() {
        return receptionistId;
    }

    public Timestamp getCheckIn() {
        return checkIn;
    }
    public Timestamp getCheckOut() {
        return checkOut;
    }

    public Status getStatus() {
        return status;
    }

    public int getServiceRating() {
        return serviceRating;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public void mergeTestResult(TestResult testResult) {
        this.testResult.merge(testResult);
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
        map.put("doctorId", getDoctorId());
        map.put("receptionistId", getReceptionistId());
        map.put("checkIn", getCheckIn());
        map.put("checkOut", getCheckOut());
        map.put("status", getStatus().toString());
        map.put("serviceRating", getServiceRating());

        map.put("testResult", getTestResult().toMap());

        return map;
    }

    public void advanceStatus(){
        if (status != Status.CHECKED_OUT)
            status = Status.values()[status.ordinal() + 1];
    }

    public void openAnalysisFile() {
        testResult.openAnalysisFile();
    }


    @Override
    public String toString() {
        return "MedicalRecord [medicalRecordId=" + medRecId + ", patientId=" + patientId + ", doctorId=" + doctorId +
                ", receptionisId=" + receptionistId + ", checkIn=" + getformattedCheckIn() + ", checkOut=" + getformattedCheckOut() +
                ", status=" + status.getValue() + ", serviceReview=" + serviceRating + ", testResult=" + testResult + "]";
    }
}
