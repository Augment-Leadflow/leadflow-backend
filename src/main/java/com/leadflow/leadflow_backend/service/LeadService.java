package com.leadflow.leadflow_backend.service;

import com.leadflow.leadflow_backend.domain.Lead;
import com.leadflow.leadflow_backend.domain.LeadStatus;
import com.leadflow.leadflow_backend.repos.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeadService {

    @Autowired
    private LeadRepository leadRepository;

    // ─── Create Lead ──────────────────────────────────────────────────────────

    public Lead createLead(Lead lead) {
        System.out.println("DEBUG: Saving lead: " + lead.getName());

        lead.setStatus(LeadStatus.NEW);
        lead.setCreatedAt(LocalDateTime.now());
        lead.setUpdatedAt(LocalDateTime.now());

        return leadRepository.save(lead);
    }

    // ─── Get All Leads (with optional status filter) ─────────────────────────

    public List<Lead> getAllLeads(LeadStatus status) {
        System.out.println("DEBUG: Fetching leads with status: " + status);

        if (status != null) {
            return leadRepository.findByStatus(status);
        }
        return leadRepository.findAll();
    }

    // ─── Get Lead By ID ───────────────────────────────────────────────────────

    public Lead getLeadById(String id) {
        System.out.println("DEBUG: Fetching lead ID: " + id);

        return leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with ID: " + id));
    }

    // ─── Update Lead ──────────────────────────────────────────────────────────

    public Lead updateLead(String id, Lead updatedData) {
        System.out.println("DEBUG: Updating lead ID: " + id);

        Lead existing = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with ID: " + id));

        if (updatedData.getStatus() != null) {
            existing.setStatus(updatedData.getStatus());
        }
        if (updatedData.getNotes() != null) {
            existing.setNotes(updatedData.getNotes());
        }
        existing.setUpdatedAt(LocalDateTime.now());

        return leadRepository.save(existing);
    }

    // ─── Delete Lead ──────────────────────────────────────────────────────────

    public void deleteLead(String id) {
        System.out.println("DEBUG: Deleting lead ID: " + id);

        leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with ID: " + id));

        leadRepository.deleteById(id);
    }
}