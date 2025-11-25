package com.JobPortal.App.repository;

import com.JobPortal.App.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByEmployerId(Long employerId);

    @Query("SELECT j FROM Job j WHERE " +
            "(:title IS NULL OR :title = '' OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:location IS NULL OR :location = '' OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:workMode IS NULL OR :workMode = '' OR j.workMode = :workMode) AND " +
            "(:experience IS NULL OR :experience = '' OR j.experienceLevel = :experience)")
    List<Job> searchJobs(
            @Param("title") String title,
            @Param("location") String location,
            @Param("workMode") String workMode,
            @Param("experience") String experience
    );
}