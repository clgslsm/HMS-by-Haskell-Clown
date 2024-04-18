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
    public TestResult(String testType, Map<String, Object> testResult) {
        this.testType = testType;
        this.diagnosis = (String) testResult.get("diagnosis");
        this.prescription = (String) testResult.get("prescription");
        this.analysisFilePath = (String) testResult.get("analysis");
    }
    public String getTestType() {
        return testType;
    }

    public String getAnalysisFilePath() {
        return analysisFilePath;
    }

    public void setAnalysisFilePath(String analysisFilePath) {
        this.analysisFilePath = analysisFilePath;
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
        if (analysisFilePath != null)
            FileManager.openFileWithDefaultApp(analysisFilePath);
    }

    public void merge(TestResult newTestResult){
        if (newTestResult.testType != null) testType = newTestResult.testType;
        if (newTestResult.analysisFilePath != null) testType = newTestResult.analysisFilePath;
        if (newTestResult.diagnosis != null) testType = newTestResult.diagnosis;
        if (newTestResult.prescription != null) testType = newTestResult.prescription;
    }

    @Override
    public String toString() {
        return "TestResult [testType=" + testType + "analysis=" + analysisFilePath + ", diagnosis=" + diagnosis + ", prescription=" +
                prescription + "]";
    }
}
