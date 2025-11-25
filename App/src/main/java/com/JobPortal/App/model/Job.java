package com.JobPortal.App.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String companyName;

    // --- NEW FIELDS FOR FILTERING ---
    @Column(nullable = false)
    private String experienceLevel; // e.g., "Entry Level", "3-5 Years", "Senior"

    @Column(nullable = false)
    private String workMode; // e.g., "Remote", "Hybrid", "On-site"

    private Double salaryMin;

    private Double salaryMax;

    @Enumerated(EnumType.STRING)
    private JobType jobType; // FULL_TIME, PART_TIME, etc.

    private LocalDateTime postedDate;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private User employer;

    @PrePersist
    protected void onCreate() {
        postedDate = LocalDateTime.now();
    }
}