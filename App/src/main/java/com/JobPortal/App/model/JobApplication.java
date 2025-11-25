package com.JobPortal.App.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
@Getter
@Setter
@NoArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The job they applied for
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    // The user who applied
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @Column
    private String resumePath;

    private LocalDateTime appliedDate;

    // We can add a status later (e.g., PENDING, REVIEWED, REJECTED)
    // For now, we'll keep it simple.

    @PrePersist
    protected void onCreate() {
        appliedDate = LocalDateTime.now();
    }
}
