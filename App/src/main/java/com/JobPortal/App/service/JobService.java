package com.JobPortal.App.service;


import com.JobPortal.App.dto.JobPostRequest;
import com.JobPortal.App.dto.JobResponse;
import com.JobPortal.App.model.Job;
import com.JobPortal.App.model.User;
import com.JobPortal.App.repository.JobApplicationRepository;
import com.JobPortal.App.repository.JobRepository;
import com.JobPortal.App.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new job posting.
     *
     * @param jobRequest The DTO containing job details.
     * @param employerUsername The username of the employer posting the job.
     * @return The created Job object, mapped to a DTO.
     */
    public JobResponse createJob(JobPostRequest jobRequest, String employerUsername) {
        User employer = userRepository.findByUsername(employerUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Employer not found"));

        Job job = new Job();
        job.setTitle(jobRequest.title());
        job.setDescription(jobRequest.description());
        job.setLocation(jobRequest.location());
        job.setJobType(jobRequest.jobType());
        job.setCompanyName(jobRequest.companyName());

        // --- MAP THE NEW FIELDS ---
        job.setExperienceLevel(jobRequest.experienceLevel());
        job.setWorkMode(jobRequest.workMode());
        job.setSalaryMin(jobRequest.salaryMin());
        job.setSalaryMax(jobRequest.salaryMax());

        job.setEmployer(employer);

        Job savedJob = jobRepository.save(job);

        return JobResponse.fromJob(savedJob);
    }

    /**
     * Retrieves all job postings.
     *
     * @return A list of all jobs, mapped to DTOs.
     */
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(JobResponse::fromJob) // Convert each Job to a JobResponse
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single job by its ID.
     *
     * @param id The ID of the job to find.
     * @return The job DTO.
     * @throws RuntimeException if the job is not found.
     */
    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
        return JobResponse.fromJob(job);
    }
    @Autowired
    private JobApplicationRepository jobApplicationRepository; // Inject this

    public List<JobResponse> getJobsByEmployer(String username) {
        User employer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Job> jobs = jobRepository.findByEmployerId(employer.getId());

        // Convert to DTO and populate Application Count
        return jobs.stream().map(job -> {
            JobResponse res = JobResponse.fromJob(job);
            // Fetch the count for this specific job
            res.setApplicationCount(jobApplicationRepository.countByJobId(job.getId()));
            return res;
        }).collect(Collectors.toList());
    }
}
