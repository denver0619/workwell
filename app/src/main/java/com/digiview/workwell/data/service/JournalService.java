package com.digiview.workwell.data.service;

import com.digiview.workwell.data.models.Journal;
import com.digiview.workwell.data.repository.JournalRepository;
import com.digiview.workwell.data.util.AuthHelper;

import java.util.concurrent.CompletableFuture;

public class JournalService {

    private final JournalRepository repository;

    public JournalService() {
        this.repository = new JournalRepository();
    }

    /**
     * Fetch a journal by its ID using CompletableFuture.
     *
     * @param journalId The ID of the journal to fetch.
     * @return A CompletableFuture containing the fetched journal.
     */
    public CompletableFuture<Journal> getJournal(String journalId) {
        return repository.getJournal(journalId);
    }

    /**
     * Create a new journal, ensuring OrganizationId is set from the current user's token.
     *
     * @param journal The journal object to create.
     * @return A CompletableFuture representing the operation.
     */
    public CompletableFuture<Void> createJournal(Journal journal) {
        // Use the helper to retrieve OrganizationId
        return AuthHelper.getOrganizationIdFromToken()
                .thenCompose(organizationId -> {
                    // Set the OrganizationId in the journal
                    journal.setOrganizationId(organizationId);

                    // Pass the updated journal to the repository for creation
                    return repository.createJournal(journal);
                });
    }
}
