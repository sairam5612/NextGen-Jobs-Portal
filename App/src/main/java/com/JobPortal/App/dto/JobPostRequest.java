package com.JobPortal.App.dto;

import com.JobPortal.App.model.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JobPostRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String location,
        @NotNull JobType jobType,
        @NotBlank String companyName,

        // --- NEW FIELDS ---
        @NotBlank String experienceLevel,
        @NotBlank String workMode,
        @NotNull Double salaryMin,
        @NotNull Double salaryMax
) {}