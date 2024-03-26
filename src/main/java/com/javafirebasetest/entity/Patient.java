package com.javafirebasetest.entity;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Patient {
    private String patientId;
    private String name;
    private LocalDate birthDate;
    private Gender gender;
    private String address;
    private String phoneNumber;
    private BloodGroup bloodGroup;

    public enum Gender {
        MALE("Male"), FEMALE("Female"), OTHER("Other");
        private final String value;
        Gender(String value) {this.value = value;}
        public String getValue() {return value;}
        public static Patient.Gender fromValue(String value) {
            for (Patient.Gender g : Patient.Gender.values())
                if (g.value.equalsIgnoreCase(value)) return g;
            throw new IllegalArgumentException("Invalid gender: " + value);
        }
    }
    public enum BloodGroup {
        A_POSITIVE("A+"), A_NEGATIVE("A-"), B_POSITIVE("B+"), B_NEGATIVE("B-"), O_POSITIVE("O+"),
        O_NEGATIVE("O-"), AB_POSITIVE("AB+"), AB_NEGATIVE("AB-");
        private final String value;
        BloodGroup(String value) {this.value = value;}
        public String getValue() {return value;}
        public static Patient.BloodGroup fromValue(String value) {
            for (Patient.BloodGroup bg : Patient.BloodGroup.values())
                if (bg.value.equalsIgnoreCase(value)) return bg;
            throw new IllegalArgumentException("Invalid blood group: " + value);
        }
    }
    public Patient() {super();}
    public Patient(String patientId, String name, LocalDate birthDate, Gender gender,
                    String address, String phoneNumber, BloodGroup bloodGroup) {
        super();
        this.patientId = patientId;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.bloodGroup = bloodGroup;
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
    }

    public void setPatientId(String patientId) {this.patientId = patientId;}
    public void setName(String name) {this.name = name;}
    public void setBirthDate(LocalDate birthDate) {this.birthDate = birthDate;}
    public void setGender(Gender gender) {this.gender = gender;}
    public void setAddress(String address) {this.address = address;}
    public void setBloodGroup(BloodGroup bloodGroup) {this.bloodGroup = bloodGroup;}
    public String getPatientId() {return patientId;}
    public String getName() {return name;}
    public LocalDate getBirthDate() {return birthDate;}
    public Gender getGender() {return gender;}
    public String getAddress() {return address;}
    public String getMobileNo() {return phoneNumber;}
    public BloodGroup getBloodGroup() {return bloodGroup;}
    public String getformattedDate() { // Hiển thị ngày tháng theo định dạng "dd/mm/yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return birthDate.format(formatter);
    }
    public int getAge() { //Trả về tuổi chính xác theo ngày
        LocalDate currentDate = LocalDate.now();
        if (birthDate != null) return Period.between(birthDate, currentDate).getYears();
        else throw new IllegalArgumentException("Birthday or current date cannot be null");
    }
    @Override
    public String toString() {
        return "Patient [patientId=" + patientId + ", name=" + name + ", birthDate=" + getformattedDate() +
                ", address" + address + ", gender=" + gender.getValue() + ", phoneNumber=" + phoneNumber +
                ", bloodGroup=" + bloodGroup.getValue() + "]";
    }
}