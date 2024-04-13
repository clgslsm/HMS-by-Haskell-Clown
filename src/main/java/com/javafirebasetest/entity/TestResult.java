package com.javafirebasetest.entity;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class TestResult {
    private String testType;
    private File analysis;
    private String diagnosis;
    private String prescription;

    public TestResult() {}
    public TestResult(String testType, File analysis, String diagnosis, String prescription) {
        this.testType = testType;
        this.analysis = analysis;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
    }
    public TestResult(String testType, Map<String, Object> testResult) {
        this.testType = testType;
        this.diagnosis = (String) testResult.get("diagnosis");
        this.prescription = (String) testResult.get("prescription");
        try {
            this.analysis = new File((String) testResult.get("analysis"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getTestType() {
        return testType;
    }
    public void setTestType(String testType) {
        this.testType = testType;
    }
    public File getAnalysis() {
        return analysis;
    }
    public void setAnalysis(File analysis) {
        this.analysis = analysis;
    }
    public String getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    public String getPrescription() {
        return prescription;
    }
    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }
    public Map<String, Object> toMap() {
        Map<String, Object> testResultData = new HashMap<>();
        testResultData.put("testType", testType);
        testResultData.put("analysis", analysis.getPath());
        testResultData.put("diagnosis", diagnosis);
        testResultData.put("prescription", prescription);
        return testResultData;
    }
    @Override
    public String toString() {
        return "TestResult [testType=" + testType + "analysis=" + analysis + ", diagnosis=" + diagnosis + ", prescription=" +
                prescription + "]";
    }
}
