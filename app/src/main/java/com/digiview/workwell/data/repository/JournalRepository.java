package com.digiview.workwell.data.repository;

import com.digiview.workwell.data.models.Journal;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class JournalRepository {

    private final CollectionReference journalRef;

    public JournalRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        journalRef = db.collection("journals");
    }

    /**
     * Create a new journal using CompletableFuture.
     *
     * @param journal The journal object to create.
     * @return A CompletableFuture representing the operation.
     */
    public CompletableFuture<Void> createJournal(Journal journal) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (journal.getJournalId() == null) {
            journal.setJournalId(journalRef.document().getId());
        }
        journalRef.document(journal.getJournalId()).set(journal)
                .addOnSuccessListener(unused -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**
     * Fetch a journal by its ID using CompletableFuture.
     *
     * @param journalId The ID of the journal to fetch.
     * @return A CompletableFuture containing the fetched journal.
     */
    public CompletableFuture<Journal> getJournal(String journalId) {
        CompletableFuture<Journal> future = new CompletableFuture<>();
        journalRef.document(journalId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Journal journal = documentSnapshot.toObject(Journal.class);
                    future.complete(journal);
                })
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }
}
