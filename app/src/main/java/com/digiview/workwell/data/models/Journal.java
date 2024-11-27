package com.digiview.workwell.data.models;

import com.google.firebase.firestore.PropertyName;

public class Journal {
    @PropertyName("JournalId")
    private String journalId;
    @PropertyName("Content")
    private String content;

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
    public void setContent(String content) { this.content = content; }

}
