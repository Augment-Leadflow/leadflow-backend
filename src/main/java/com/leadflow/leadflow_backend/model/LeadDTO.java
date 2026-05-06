package com.leadflow.leadflow_backend.model;

import com.leadflow.leadflow_backend.domain.LeadStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LeadDTO {

    private String id;           // null on create requests, populated in responses

    private String name;
    private String phone;
    private String source;
    private String notes;

    // Sent as String from frontend ("NEW", "CONTACTED", "CONVERTED", "LOST")
    // null on create → backend defaults to NEW
    private String status;

    // Read-only — returned in responses, ignored when sent by frontend
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}