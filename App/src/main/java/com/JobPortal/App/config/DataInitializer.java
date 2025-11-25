package com.JobPortal.App.config;

import com.JobPortal.App.model.*;
import com.JobPortal.App.repository.JobRepository;
import com.JobPortal.App.repository.RoleRepository;
import com.JobPortal.App.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired RoleRepository roleRepository;
    @Autowired UserRepository userRepository;
    @Autowired JobRepository jobRepository;
    @Autowired PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {

        // 1. Initialize Roles (Keep existing code)
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(ERole.ROLE_APPLICANT));
            roleRepository.save(new Role(ERole.ROLE_EMPLOYER));
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }

        // 2. Create Dummy Employer (Keep existing code)
        if (!userRepository.existsByUsername("google_hr")) {
            User employer = new User("google_hr", "hr@google.com", encoder.encode("password"));
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(ERole.ROLE_EMPLOYER).get());
            employer.setRoles(roles);
            userRepository.save(employer);
        }

        // 3. Seed 50 Jobs with RICH DESCRIPTIONS
        if (jobRepository.count() == 0) {
            User employer = userRepository.findByUsername("google_hr").get();

            String[] companies = {"Google", "Amazon", "Netflix", "Microsoft", "Meta", "Tesla", "SpaceX", "Adobe", "Salesforce", "Oracle"};
            String[] titles = {"Java Backend Developer", "Frontend Engineer", "Data Scientist", "DevOps Engineer", "Product Manager"};
            String[] locations = {"Remote", "New York", "San Francisco", "Hyderabad", "Berlin"};
            String[] modes = {"Remote", "On-site", "Hybrid"};
            String[] exps = {"Entry Level", "Mid Senior", "Senior", "Lead"};
            Random rand = new Random();

            for (int i = 1; i <= 50; i++) {
                Job job = new Job();
                String title = titles[rand.nextInt(titles.length)];
                String company = companies[rand.nextInt(companies.length)];

                job.setTitle(title);
                job.setCompanyName(company);
                job.setLocation(locations[rand.nextInt(locations.length)]);

                // --- GENERATE RICH HTML DESCRIPTION ---
                String richDescription = generateRichDescription(title, company);
                job.setDescription(richDescription);
                // --------------------------------------

                job.setJobType(JobType.FULL_TIME);
                job.setWorkMode(modes[rand.nextInt(modes.length)]);
                job.setExperienceLevel(exps[rand.nextInt(exps.length)]);
                job.setSalaryMin(60000.0 + (rand.nextDouble() * 40000));
                job.setSalaryMax(100000.0 + (rand.nextDouble() * 100000));
                job.setEmployer(employer);
                jobRepository.save(job);
            }
            System.out.println("✅ Database seeded with RICH Job Descriptions!");
        }
    }

    // --- HELPER METHOD FOR HTML DESCRIPTIONS ---
    private String generateRichDescription(String title, String company) {
        return "<strong>About the Role:</strong><br>" +
                "We are looking for a passionate <strong>" + title + "</strong> to join our dynamic team at " + company + ". " +
                "You will be working on scalable systems and cutting-edge technology.<br><br>" +

                "<strong>Key Responsibilities:</strong>" +
                "<ul>" +
                "<li>Design and develop high-performance software applications.</li>" +
                "<li>Collaborate with cross-functional teams to define, design, and ship new features.</li>" +
                "<li>Write clean, maintainable, and efficient code.</li>" +
                "<li>Troubleshoot and debug applications to optimize performance.</li>" +
                "</ul>" +

                "<strong>Requirements:</strong>" +
                "<ul>" +
                "<li>Bachelor's degree in Computer Science or related field.</li>" +
                "<li>Strong knowledge of data structures and algorithms.</li>" +
                "<li>Experience with Cloud platforms (AWS/Azure/GCP).</li>" +
                "<li>Excellent problem-solving skills and attention to detail.</li>" +
                "</ul>" +

                "<strong>Benefits:</strong><br>" +
                "Competitive salary, Health insurance, Remote work options, and Annual bonuses.";
    }
}