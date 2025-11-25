package com.JobPortal.App.repository;

import com.JobPortal.App.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    // Find all applications for a specific job
    List<JobApplication> findByJobId(Long jobId);

    // Find all applications submitted by a specific user
    List<JobApplication> findByApplicantId(Long applicantId);

    // Check if a specific user has already applied for a specific job
    Optional<JobApplication> findByJobIdAndApplicantId(Long jobId, Long applicantId);

    // --- NEW METHOD ---
    long countByJobId(Long jobId);
}
