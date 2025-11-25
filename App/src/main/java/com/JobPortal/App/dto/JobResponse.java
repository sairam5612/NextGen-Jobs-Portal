package com.JobPortal.App.dto;

import com.JobPortal.App.model.Job;
import com.JobPortal.App.model.JobType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private JobType jobType;
    private String companyName;
    private LocalDateTime postedDate;
    private String employerUsername;

    private String experienceLevel;

    private Long applicationCount;
    private String workMode;
    private Double salaryMin;
    private Double salaryMax;

    public static JobResponse fromJob(Job job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setLocation(job.getLocation());
        response.setJobType(job.getJobType());
        response.setCompanyName(job.getCompanyName());
        response.setPostedDate(job.getPostedDate());

        // Map the new fields
        response.setExperienceLevel(job.getExperienceLevel());
        response.setWorkMode(job.getWorkMode());
        response.setSalaryMin(job.getSalaryMin());
        response.setSalaryMax(job.getSalaryMax());

        if (job.getEmployer() != null) {
            response.setEmployerUsername(job.getEmployer().getUsername());
        }

        return response;
    }
}