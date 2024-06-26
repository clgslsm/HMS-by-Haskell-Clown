package com.javafirebasetest.entity;
import com.javafirebasetest.dao.FileManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TestResult {
    private String testType;
    private String analysisFilePath;
    private String diagnosis;
    private String prescription;

    public TestResult() {}
    public TestResult(String testType, String analysisFilePath, String diagnosis, String prescription) {
        this.testType = testType;
        this.analysisFilePath = analysisFilePath;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
    }
    public TestResult(Map<String, Object> testResult) {
        if (testResult == null) return;
        this.testType = (String) testResult.get("testType");
        this.diagnosis = (String) testResult.get("diagnosis");
        this.prescription = (String) testResult.get("prescription");
        this.analysisFilePath = (String) testResult.get("analysisFilePath");
    }
    public String getTestType() {
        return testType;
    }

    public String getAnalysisFilePath() {
        return analysisFilePath;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> testResultData = new HashMap<>();
        testResultData.put("testType", testType);
        testResultData.put("analysisFilePath", analysisFilePath);
        testResultData.put("diagnosis", diagnosis);
        testResultData.put("prescription", prescription);
        return testResultData;
    }

    public void openAnalysisFile(){
        FileManager.openFileWithDefaultApp(analysisFilePath);
    }

    public void merge(TestResult newTestResult){
        if (newTestResult.testType != null) testType = newTestResult.testType;
        if (newTestResult.analysisFilePath != null) analysisFilePath = newTestResult.analysisFilePath;
        if (newTestResult.diagnosis != null) diagnosis = newTestResult.diagnosis;
        if (newTestResult.prescription != null) prescription = newTestResult.prescription;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "testType='" + testType + '\'' +
                ", analysisFilePath='" + analysisFilePath + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", prescription='" + prescription + '\'' +
                '}';
    }
}
