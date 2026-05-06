package com.leadflow.leadflow_backend.service;

import com.leadflow.leadflow_backend.domain.Lead;
import com.leadflow.leadflow_backend.domain.LeadStatus;
import com.leadflow.leadflow_backend.exception.ResourceNotFoundException;
import com.leadflow.leadflow_backend.model.LeadDTO;
import com.leadflow.leadflow_backend.repos.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeadService {

    @Autowired
    private LeadRepository leadRepository;

    //private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LeadService.class);
    // ─── Create Lead ──────────────────────────────────────────────────────────
    public LeadDTO createLead(final LeadDTO leadDTO) {
        System.out.println("DEBUG: Saving lead: " + leadDTO.getName());

        final Lead lead = new Lead();
        mapToEntity(leadDTO, lead);

        lead.setStatus(LeadStatus.NEW);
        lead.setCreatedAt(LocalDateTime.now());
        lead.setUpdatedAt(LocalDateTime.now());

        final Lead savedLead = leadRepository.save(lead);
        return mapToDTO(savedLead, new LeadDTO());
    }

    // ─── Get All Leads ─────────────────────────
    public List<LeadDTO> getAllLeads(String statusStr) {
        System.out.println("DEBUG: Fetching leads with status: " + statusStr);

        List<Lead> leads;
        if (statusStr != null && !statusStr.trim().isEmpty()) {
            try {
                LeadStatus status = LeadStatus.valueOf(statusStr.toUpperCase());
                leads = leadRepository.findByStatus(status);
            } catch (IllegalArgumentException e) {
                leads = leadRepository.findAll();
            }
        } else {
            leads = leadRepository.findAll();
        }

        return leads.stream()
                .map(lead -> mapToDTO(lead, new LeadDTO()))
                .collect(Collectors.toList());
    }
    // ─── Get Lead By ID ───────────────────────────────────────────────────────
    public LeadDTO getLeadById(final String id) {
        System.out.println("DEBUG: Fetching lead ID: " + id);

        final Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with ID: " + id));
        return mapToDTO(lead, new LeadDTO());
    }

    // ─── Update Lead ──────────────────────────────────────────────────────────
    public LeadDTO updateLead(final String id, final LeadDTO leadDTO) {
        System.out.println("DEBUG: Updating lead ID: " + id);

        final Lead existing = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with ID: " + id));

        mapToEntity(leadDTO, existing);
        existing.setUpdatedAt(LocalDateTime.now());

        final Lead updated = leadRepository.save(existing);
        return mapToDTO(updated, new LeadDTO());
    }

    // ─── Delete Lead ──────────────────────────────────────────────────────────
    public void deleteLead(final String id) {
        System.out.println("DEBUG: Deleting lead ID: " + id);

        if (!leadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lead not found with ID: " + id);
        }
        leadRepository.deleteById(id);
    }

    // ─── Check ID for Validation ─────────────────────────────────────────────
    public boolean idExists(final String id) {
        System.out.println("DEBUG: Validating existence for ID: " + id);
        return leadRepository.existsById(id);
    }

    // ─── Helper Methods (Mapping Logic) ──────────────────────────────────────

    private Lead mapToEntity(final LeadDTO leadDTO, final Lead lead) {
        lead.setName(leadDTO.getName());
        lead.setPhone(leadDTO.getPhone());
        lead.setSource(leadDTO.getSource());
        lead.setNotes(leadDTO.getNotes());
        if (leadDTO.getStatus() != null) {
            lead.setStatus(LeadStatus.valueOf(leadDTO.getStatus()));
        }
        return lead;
    }

    private LeadDTO mapToDTO(final Lead lead, final LeadDTO leadDTO) {
        leadDTO.setId(lead.getId());
        leadDTO.setName(lead.getName());
        leadDTO.setPhone(lead.getPhone());
        leadDTO.setSource(lead.getSource());
        leadDTO.setStatus(lead.getStatus().name());
        leadDTO.setNotes(lead.getNotes());
        if (lead.getCreatedAt() != null) {
            leadDTO.setCreatedAt(lead.getCreatedAt().atOffset(ZoneOffset.UTC).toLocalDateTime());
        }
        return leadDTO;
    }
}