package com.JobPortal.App.service;

import com.JobPortal.App.dto.JobApplicationResponse;
import com.JobPortal.App.model.Job;
import com.JobPortal.App.model.JobApplication;
import com.JobPortal.App.model.User;
import com.JobPortal.App.repository.JobApplicationRepository;
import com.JobPortal.App.repository.JobRepository;
import com.JobPortal.App.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    @Autowired
    private com.JobPortal.App.service.EmailService emailService;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Allows an applicant to apply for a job.
     */
    public JobApplicationResponse applyForJob(Long jobId, String applicantUsername, MultipartFile file) {
        // 1. Find User & Job
        User applicant = userRepository.findByUsername(applicantUsername)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // 2. Check if Already Applied
        if (jobApplicationRepository.findByJobIdAndApplicantId(jobId, applicant.getId()).isPresent()) {
            throw new RuntimeException("You have already applied for this job!");
        }

        // 3. Store File
        String fileName = fileStorageService.storeFile(file, applicant.getId(), jobId);

        // 4. Save Application
        JobApplication newApplication = new JobApplication();
        newApplication.setApplicant(applicant);
        newApplication.setJob(job);
        newApplication.setResumePath(fileName);

        JobApplication savedApplication = jobApplicationRepository.save(newApplication);

        // 5. SEND EMAIL (SAFE MODE)
        try {
            String subject = "Application Received: " + job.getTitle();
            String body = "Hi " + applicant.getUsername() + ",\n\n" +
                    "We received your application for " + job.getCompanyName() + ".\n" +
                    "Best,\nNextGen Jobs";

            emailService.sendEmail(applicant.getEmail(), subject, body);
        } catch (Exception e) {
            // Log error but DO NOT crash the request
            System.err.println("⚠️ EMAIL FAILED (Ignored): " + e.getMessage());
        }

        return mapToResponseWithUrl(savedApplication);
    }

    /**
     * Allows an employer to view all applications for one of their jobs.
     */
    public List<JobApplicationResponse> getApplicationsForJob(Long jobId, String employerUsername) {
        // 1. Find the employer
        User employer = userRepository.findByUsername(employerUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found: " + employerUsername));

        // 2. Find the job
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        // 3. SECURITY CHECK: Ensure the person asking *is* the one who posted the job
        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new SecurityException("You are not authorized to view applications for this job.");
        }

        // 4. Get applications and convert to DTOs
        return jobApplicationRepository.findByJobId(jobId).stream()
                .map(this::mapToResponseWithUrl) // Use new helper method
                .collect(Collectors.toList());
    }

    private JobApplicationResponse mapToResponseWithUrl(JobApplication application) {
        JobApplicationResponse response = JobApplicationResponse.fromApplication(application);

        // Build the download URL (e.g., http://localhost:8080/api/jobs/applications/resume/applicant_1_...pdf)
        String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/jobs/applications/resume/")
                .path(application.getResumePath())
                .toUriString();

        response.setResumeDownloadUrl(downloadUrl);
        return response;
    }
}