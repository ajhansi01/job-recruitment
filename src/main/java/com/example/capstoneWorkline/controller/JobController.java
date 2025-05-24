package com.example.capstoneWorkline.controller;

import com.example.capstoneWorkline.dto.JobRequest;
import com.example.capstoneWorkline.entity.Job;
import com.example.capstoneWorkline.entity.User;
import com.example.capstoneWorkline.repository.UserRepository;
import com.example.capstoneWorkline.service.JobService;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    @Autowired
    private JobService jobService;
    @Autowired private UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> createJob(@RequestBody @Valid JobRequest request, Authentication authentication) {
        User recruiter = getUserFromAuth(authentication);
        Job job = jobService.createJob(request, recruiter);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> updateJob(@PathVariable Long jobId, @RequestBody @Valid JobRequest request, Authentication authentication) {
        User recruiter = getUserFromAuth(authentication);
        Job job = jobService.updateJob(jobId, request, recruiter);
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> deleteJob(@PathVariable Long jobId, Authentication authentication) {
        User recruiter = getUserFromAuth(authentication);
        jobService.deleteJob(jobId, recruiter);
        return ResponseEntity.ok("Job deleted");
    }

    @GetMapping
    public ResponseEntity<?> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<?> getJobDetails(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobService.getJobById(jobId));
    }

    private User getUserFromAuth(Authentication auth) {
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

