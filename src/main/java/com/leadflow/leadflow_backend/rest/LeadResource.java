package com.leadflow.leadflow_backend.rest;
import com.leadflow.leadflow_backend.domain.Lead;
import com.leadflow.leadflow_backend.model.LeadDTO;
import com.leadflow.leadflow_backend.model.User;
import com.leadflow.leadflow_backend.service.LeadService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/leads")
public class LeadResource {

    @Autowired
    private LeadService leadService;

    private String extractUserId(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getEmail();
        }
        return authentication.getName();
    }

    @PostMapping
    public ResponseEntity<?> createLead(
            @Valid @RequestBody final LeadDTO leadDTO,
            Authentication authentication) {
        log.info("REST request to create a new lead. Name: {}", leadDTO.getName());
        try {
            String userId = extractUserId(authentication);
            LeadDTO created = leadService.createLead(leadDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            log.error("Error creating lead: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating lead: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllLeads(
            @RequestParam(required = false) String status,
            Authentication authentication) {
        log.info("REST request to get all leads with status: {}", status);
        try {
            String userId = extractUserId(authentication);
            return ResponseEntity.ok(leadService.getAllLeadsForUser(userId, status));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            log.error("Failed to fetch leads: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLeadById(
            @PathVariable final String id,
            Authentication authentication) {
        log.info("REST request to get lead by ID: {}", id);
        try {
            String userId = extractUserId(authentication);
            return ResponseEntity.ok(leadService.getLeadById(id, userId));
        } catch (ResponseStatusException e) {
            log.error("Error for ID {}: {}", id, e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (RuntimeException e) {
            log.warn("Lead not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Lead>> search(
            @RequestParam String query,
            Authentication authentication) {
        log.info("REST request to search leads with query: {}", query);
        String userId = extractUserId(authentication);
        return ResponseEntity.ok(leadService.searchLeads(query, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLead(
            @PathVariable final String id,
            @RequestBody final LeadDTO leadDTO,
            Authentication authentication) {
        log.info("REST request to update lead ID: {}", id);
        try {
            String userId = extractUserId(authentication);
            return ResponseEntity.ok(leadService.updateLead(id, leadDTO, userId));
        } catch (ResponseStatusException e) {
            log.error("Error for ID {}: {}", id, e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (RuntimeException e) {
            log.error("Update failed for ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLead(
            @PathVariable final String id,
            Authentication authentication) {
        log.warn("REST request to delete lead with ID: {}", id);
        try {
            String userId = extractUserId(authentication);
            leadService.deleteLead(id, userId);
            return ResponseEntity.ok(java.util.Map.of(
                    "status", "SUCCESS",
                    "message", "Lead deleted successfully."
            ));
        } catch (ResponseStatusException e) {
            log.error("Error on delete for ID {}: {}", id, e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (RuntimeException e) {
            log.error("Delete failed for ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}