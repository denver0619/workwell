package com.digiview.workwell.data.models;

import com.google.firebase.firestore.PropertyName;

public class Journal {
    @PropertyName("JournalId")
    private String journalId;

    @PropertyName("Content")
    private String content;

    @PropertyName("OrganizationId")
    private String organizationId; // New field for organization ID

    @PropertyName("JournalId")
    public String getJournalId() {
        return journalId;
    }

    @PropertyName("JournalId")
    public void setJournalId(String journalId) {
        this.journalId = journalId;
    }

    @PropertyName("Content")
    public String getContent() {
        return content;
    }

    @PropertyName("Content")
    public void setContent(String content) {
        this.content = content;
    }

    @PropertyName("OrganizationId")
    public String getOrganizationId() {
        return organizationId;
    }

    @PropertyName("OrganizationId")
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "journalId='" + journalId + '\'' +
                ", content='" + content + '\'' +
                ", organizationId='" + organizationId + '\'' +
                '}';
    }
}
