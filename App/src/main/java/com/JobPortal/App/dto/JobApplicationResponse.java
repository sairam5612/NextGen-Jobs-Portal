package com.JobPortal.App.dto;

import com.JobPortal.App.model.JobApplication;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JobApplicationResponse {
    private Long applicationId;
    private Long jobId;
    private String jobTitle;
    private Long applicantId;
    private String applicantUsername;
    private LocalDateTime appliedDate;
    private String resumeDownloadUrl;

    // Mapper to convert our Entity to this DTO
    public static JobApplicationResponse fromApplication(JobApplication application) {
        JobApplicationResponse response = new JobApplicationResponse();
        response.setApplicationId(application.getId());
        response.setJobId(application.getJob().getId());
        response.setJobTitle(application.getJob().getTitle());
        response.setApplicantId(application.getApplicant().getId());
        response.setApplicantUsername(application.getApplicant().getUsername());
        response.setAppliedDate(application.getAppliedDate());

        // We will set resumeDownloadUrl from the service,
        // as the service has access to the helper to build the URL.

        return response;
    }
}