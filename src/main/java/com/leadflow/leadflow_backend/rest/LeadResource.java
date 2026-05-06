package com.leadflow.leadflow_backend.rest;

import com.leadflow.leadflow_backend.domain.LeadStatus;
import com.leadflow.leadflow_backend.model.LeadDTO;
import com.leadflow.leadflow_backend.service.LeadService;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<?> createLead(@RequestBody @Valid final LeadDTO leadDTO) {
        System.out.println("DEBUG: POST /api/leads called for: " + leadDTO.getName());
        try {
            LeadDTO created = leadService.createLead(leadDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating lead: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllLeads(@RequestParam(required = false) String status) {
        System.out.println("DEBUG: GET /api/leads - Status Filter: " + status);
        try {
            return ResponseEntity.ok(leadService.getAllLeads(status));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLeadById(@PathVariable final String id) {
        try {
            return ResponseEntity.ok(leadService.getLeadById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLead(@PathVariable final String id, @RequestBody @Valid final LeadDTO leadDTO) {
        System.out.println("DEBUG: Updating Lead ID: " + id);
        try {
            return ResponseEntity.ok(leadService.updateLead(id, leadDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLead(@PathVariable final String id) {
        try {
            leadService.deleteLead(id);
            return ResponseEntity.ok("Lead deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}