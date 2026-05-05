package com.leadflow.leadflow_backend.rest;

import com.leadflow.leadflow_backend.domain.Lead;
import com.leadflow.leadflow_backend.domain.LeadStatus;
import com.leadflow.leadflow_backend.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@CrossOrigin("*")
public class LeadResource {

    @Autowired
    private LeadService leadService;

    // ─── POST /api/leads ──────────────────────────────────────────────────────
    // Create a new lead (status auto-set to NEW)

    @PostMapping
    public ResponseEntity<?> createLead(@RequestBody Lead lead) {
        System.out.println("DEBUG: POST /api/leads called for: " + lead.getName());
        try {
            Lead created = leadService.createLead(lead);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating lead: " + e.getMessage());
        }
    }

    // ─── GET /api/leads ───────────────────────────────────────────────────────
    // Get all leads; optionally filter by ?status=NEW|CONTACTED|CONVERTED|LOST

    @GetMapping
    public ResponseEntity<?> getAllLeads(
            @RequestParam(required = false) LeadStatus status) {
        System.out.println("DEBUG: GET /api/leads called with status filter: " + status);
        try {
            List<Lead> leads = leadService.getAllLeads(status);
            return ResponseEntity.ok(leads);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching leads: " + e.getMessage());
        }
    }

    // ─── GET /api/leads/{id} ──────────────────────────────────────────────────
    // Get a single lead by MongoDB ID

    @GetMapping("/{id}")
    public ResponseEntity<?> getLeadById(@PathVariable String id) {
        System.out.println("DEBUG: GET /api/leads/" + id + " called");
        try {
            Lead lead = leadService.getLeadById(id);
            return ResponseEntity.ok(lead);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Lead not found: " + e.getMessage());
        }
    }

    // ─── PATCH /api/leads/{id} ────────────────────────────────────────────────
    // Update status and/or notes of an existing lead

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateLead(
            @PathVariable String id,
            @RequestBody Lead updatedData) {
        System.out.println("DEBUG: PATCH /api/leads/" + id + " called");
        try {
            Lead updated = leadService.updateLead(id, updatedData);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Lead not found: " + e.getMessage());
        }
    }

    // ─── DELETE /api/leads/{id} ───────────────────────────────────────────────
    // Delete a lead by ID

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLead(@PathVariable String id) {
        System.out.println("DEBUG: DELETE /api/leads/" + id + " called");
        try {
            leadService.deleteLead(id);
            return ResponseEntity.ok("Lead deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Lead not found: " + e.getMessage());
        }
    }
}
