package com.digiview.workwell.data.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Diagnosis implements Serializable {

    private String DiagnosisId;
    private String Uid;
    private String OrganizationId;
    private String Symptoms;
    private String DiagnosisResult;
    private String SeverityLevel;
    private Date DiagnosisDate;
    private String RecommendedErgonomicAdjustments;
    private String PhysicalTherapyRecommendations;
    private String MedicationPrescriptions;
    private Date TreatmentPlanStartDate;
    private Date FollowUpPlan;

    public Diagnosis() {
    }

    // Date formatting methods
    public String getFormattedDiagnosisDate() {
        return formatDate(DiagnosisDate);
    }

    public void setFormattedDiagnosisDate(String formattedDate) {
        this.DiagnosisDate = parseDate(formattedDate);
    }

    public String getFormattedTreatmentPlanStartDate() {
        return formatDate(TreatmentPlanStartDate);
    }

    public void setFormattedTreatmentPlanStartDate(String formattedDate) {
        this.TreatmentPlanStartDate = parseDate(formattedDate);
    }

    public String getFormattedFollowUpPlan() {
        return formatDate(FollowUpPlan);
    }

    public void setFormattedFollowUpPlan(String formattedDate) {
        this.FollowUpPlan = parseDate(formattedDate);
    }

    private String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            return dateFormat.format(date);
        }
        return null;
    }

    private Date parseDate(String formattedDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            return dateFormat.parse(formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Getters and Setters
    public String getDiagnosisId() {
        return DiagnosisId;
    }

    public void setDiagnosisId(String diagnosisId) {
        DiagnosisId = diagnosisId;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getOrganizationId() {
        return OrganizationId;
    }

    public void setOrganizationId(String organizationId) {
        OrganizationId = organizationId;
    }

    public String getSymptoms() {
        return Symptoms;
    }

    public void setSymptoms(String symptoms) {
        Symptoms = symptoms;
    }

    public String getDiagnosisResult() {
        return DiagnosisResult;
    }

    public void setDiagnosisResult(String diagnosisResult) {
        DiagnosisResult = diagnosisResult;
    }

    public String getSeverityLevel() {
        return SeverityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        SeverityLevel = severityLevel;
    }

    public Date getDiagnosisDate() {
        return DiagnosisDate;
    }

    public void setDiagnosisDate(Date diagnosisDate) {
        DiagnosisDate = diagnosisDate;
    }

    public String getRecommendedErgonomicAdjustments() {
        return RecommendedErgonomicAdjustments;
    }

    public void setRecommendedErgonomicAdjustments(String recommendedErgonomicAdjustments) {
        RecommendedErgonomicAdjustments = recommendedErgonomicAdjustments;
    }

    public String getPhysicalTherapyRecommendations() {
        return PhysicalTherapyRecommendations;
    }

    public void setPhysicalTherapyRecommendations(String physicalTherapyRecommendations) {
        PhysicalTherapyRecommendations = physicalTherapyRecommendations;
    }

    public String getMedicationPrescriptions() {
        return MedicationPrescriptions;
    }

    public void setMedicationPrescriptions(String medicationPrescriptions) {
        MedicationPrescriptions = medicationPrescriptions;
    }

    public Date getTreatmentPlanStartDate() {
        return TreatmentPlanStartDate;
    }

    public void setTreatmentPlanStartDate(Date treatmentPlanStartDate) {
        TreatmentPlanStartDate = treatmentPlanStartDate;
    }

    public Date getFollowUpPlan() {
        return FollowUpPlan;
    }

    public void setFollowUpPlan(Date followUpPlan) {
        FollowUpPlan = followUpPlan;
    }
}
