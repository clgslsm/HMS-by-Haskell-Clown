package com.javafirebasetest.entity;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class Patient {
    private String patientId;
    private String name;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String phoneNumber;
    private BloodGroup bloodGroup;
    private String healthInsuranceNumber;

    public enum Gender {
        MALE("Male"), FEMALE("Female"), OTHER("Other");
        private final String value;

        Gender(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Patient.Gender fromValue(String value) {
            for (Patient.Gender g : Patient.Gender.values())
                if (g.value.equalsIgnoreCase(value)) return g;
            throw new IllegalArgumentException(STR."Invalid gender: \{value}");
        }
    }

    public enum BloodGroup {
        A_POSITIVE("A+"), A_NEGATIVE("A-"), B_POSITIVE("B+"), B_NEGATIVE("B-"), O_POSITIVE("O+"),
        O_NEGATIVE("O-"), AB_POSITIVE("AB+"), AB_NEGATIVE("AB-");
        private final String value;

        BloodGroup(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Patient.BloodGroup fromValue(String value) {
            for (Patient.BloodGroup bg : Patient.BloodGroup.values())
                if (bg.value.equalsIgnoreCase(value)) return bg;
            throw new IllegalArgumentException(STR."Invalid blood group: \{value}");
        }
    }

    public Patient() {
        super();
    }

    public Patient(String patientId, String name, LocalDate birthDate, Gender gender,
                   String address, String phoneNumber, BloodGroup bloodGroup, String healthInsuranceNumber) {
        super();
        this.patientId = patientId;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.bloodGroup = bloodGroup;
        this.healthInsuranceNumber = healthInsuranceNumber;
    }

    public Patient(String patientId, Map<String, Object> patient) {
        super();
        this.patientId = patientId;
        this.name = (String) patient.get("name");
        this.birthDate = LocalDate.parse((String) patient.get("birthDate")); // Assuming birthDate is stored as String in the map
        this.gender = Gender.fromValue((String) patient.get("gender")); // Assuming gender is stored as String in the map
        this.address = (String) patient.get("address"); // Assuming address is stored as String in the map
        this.phoneNumber = (String) patient.get("phoneNumber"); // Assuming phoneNumber is stored as String in the map
        this.bloodGroup = BloodGroup.fromValue((String) patient.get("bloodGroup")); // Assuming bloodGroup is stored as String in the map
        this.healthInsuranceNumber = (String) patient.get("healthInsuranceNumber");
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
    public String getHealthInsuranceNumber() {return healthInsuranceNumber;}
    public void setHealthInsuranceNumber(String healthInsuranceNumber) {this.healthInsuranceNumber = healthInsuranceNumber;}

    public String getformattedDate() { // Hiển thị ngày tháng theo định dạng "dd/mm/yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return birthDate.format(formatter);
    }

    public int getAge() { //Trả về tuổi chính xác theo ngày
        LocalDate currentDate = LocalDate.now();
        if (birthDate != null) return Period.between(birthDate, currentDate).getYears();
        else throw new IllegalArgumentException("Birthday or current date cannot be null");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", getName());
        map.put("birthDate", getBirthDate().toString());
        map.put("gender", getGender().value);
        map.put("address", getAddress());
        map.put("phoneNumber", getPhoneNumber());
        map.put("bloodGroup", getBloodGroup().value);
        map.put("healthInsuranceNumber", getHealthInsuranceNumber());
        return map;
    }

    @Override
    public String toString() {

        return "Patient [patientId=" + patientId + ", name=" + name + ", birthDate=" + getformattedDate() +
                ", address" + address + ", gender=" + gender.getValue() + ", phoneNumber=" + phoneNumber +
                ", bloodGroup=" + bloodGroup.getValue() + ", healthInsuranceNumber=" + healthInsuranceNumber + "]";

    }
}