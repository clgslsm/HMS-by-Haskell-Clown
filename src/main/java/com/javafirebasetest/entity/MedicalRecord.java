package com.javafirebasetest.entity;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class MedicalRecord {
    public String MedicalRecordId;
    public String patientId;
    public DeptType department;
    public String doctorId;

    public LocalDateTime checkIn;

    public LocalDateTime checkOut;
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
    public MedicalRecord(String medicalRecordId, String patient, DeptType department, String doctorId, LocalDateTime checkIn,
                         LocalDateTime checkOut, String observation, Status status, String serviceReview, String prescription) {
        this.MedicalRecordId = medicalRecordId;
        this.patientId = patient;
        this.department = department;
        this.doctorId = doctorId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.observation = observation;
        this.status = status;
        this.serviceReview = serviceReview;
        this.prescription = prescription;
    }
//    public String getMedicalRecordId() {return MedicalRecordId;}
//    public void setID(String medicalRecordId) {this.MedicalRecordId = medicalRecordId;}
//    public String getPatientId() {return patientId;}
//    public void setPatientId(String patientId) {this.patientId = patientId;}
//    public DeptType getDepartment() {return department;}
//    public void setDepartment(DeptType department) {this.department = department;}
//    public String getDoctorId() {return doctorId;}
//    public void setDid(String doctorId) {this.doctorId = doctorId;}
//    public Timestamp getCheckIn() {return checkIn;}
//    public void setCheckIn(Timestamp checkIn) {this.checkIn = checkIn;}
//    public Timestamp getCheckOut() {return checkOut;}
//    public void setCheckOut(Timestamp checkOut) {this.checkOut = checkOut;}
//    public String getObservation() {return observation;}
//    public void setObservation(String observation) {this.observation = observation;}
//    public Status getStatus() {return status;}
//    public void setStatus(Status status) {this.status = status;}
//    public String getServiceReview() {return serviceReview;}
//    public void setServiceReview(String serviceReview) {this.serviceReview = serviceReview;}
//    public String getPrescription() {return prescription;}
//    public void setPrescription(String prescription) {this.prescription = prescription;}
    public String getformattedCheckIn() { // Hiển thị ngày tháng theo định dạng "dd/mm/yyyy"
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(checkIn);
    }
    public String getformattedCheckOut() { // Hiển thị ngày tháng theo định dạng "dd/mm/yyyy"
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(checkOut);
    }
    @Override
    public String toString() {
        return "MedicalRecord [MedicalRecordId=" + MedicalRecordId + ", patientId=" + patientId + ", department=" +
                department.getValue() + ", doctorId=" + doctorId + ", checkIn=" + getformattedCheckIn() + ", checkOut=" +
                getformattedCheckOut() + ", observation=" + observation + ", status=" + status.getValue() +
                ", serviceReview=" + serviceReview + ", prescription=" + prescription + "]";
    }
}
