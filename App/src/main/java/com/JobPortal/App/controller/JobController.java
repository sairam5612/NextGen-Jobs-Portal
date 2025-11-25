package com.JobPortal.App.controller;

import com.JobPortal.App.dto.JobApplicationResponse;
import com.JobPortal.App.dto.JobPostRequest;
import com.JobPortal.App.dto.JobResponse;
import com.JobPortal.App.repository.JobRepository;
import com.JobPortal.App.service.FileStorageService;
import com.JobPortal.App.service.JobApplicationService;
import com.JobPortal.App.service.JobService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobPostRequest jobRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        JobResponse createdJob = jobService.createJob(jobRequest, username);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<JobResponse> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        JobResponse job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @PostMapping(value = "/{id}/apply", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<JobApplicationResponse> applyForJob(
            @PathVariable Long id,
            @RequestParam("resume") MultipartFile resumeFile) {

        if (resumeFile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        JobApplicationResponse applicationResponse = jobApplicationService.applyForJob(id, username, resumeFile);

        return new ResponseEntity<>(applicationResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/applications")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<List<JobApplicationResponse>> getApplicationsForJob(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JobApplicationResponse> applications = jobApplicationService.getApplicationsForJob(id, username);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/applications/resume/{fileName:.+}")
    public ResponseEntity<Resource> downloadResume(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @Autowired
    private JobRepository jobRepository; // We need to access the repo directly for the custom search

    @GetMapping("/search")
    public ResponseEntity<List<JobResponse>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String workMode,
            @RequestParam(required = false) String experience) {

        // Call the custom query we wrote in the Repository
        List<com.JobPortal.App.model.Job> jobs = jobRepository.searchJobs(title, location, workMode, experience);

        // Convert to DTOs
        List<JobResponse> response = jobs.stream()
                .map(JobResponse::fromJob)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-jobs")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<List<JobResponse>> getMyJobs() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JobResponse> jobs = jobService.getJobsByEmployer(username);
        return ResponseEntity.ok(jobs);
    }
}