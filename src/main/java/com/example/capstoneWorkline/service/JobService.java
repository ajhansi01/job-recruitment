package com.example.capstoneWorkline.service;

import com.example.capstoneWorkline.dto.JobRequest;
import com.example.capstoneWorkline.entity.Job;
import com.example.capstoneWorkline.entity.User;
import com.example.capstoneWorkline.enu.JobStatus;
import com.example.capstoneWorkline.repository.JobRepository;
import com.example.capstoneWorkline.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired private UserRepository userRepository;

    public Job createJob(JobRequest request, User recruiter) {
        Job job = new Job();
        job.setTitle(request.title);
        job.setDescription(request.description);
        job.setRequirements(request.requirements);
        job.setSalary(request.salary);
        job.setLocation(request.location);
        job.setCategory(request.category);
        job.setStatus(JobStatus.OPEN);
        job.setRecruiter(recruiter);

        return jobRepository.save(job);
    }

    public Job updateJob(Long jobId, JobRequest request, User recruiter) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiter.getId()))
            throw new RuntimeException("Unauthorized");

        if (job.getStatus() == JobStatus.CLOSED)
            throw new RuntimeException("Cannot update closed jobs");

        job.setTitle(request.title);
        job.setDescription(request.description);
        job.setRequirements(request.requirements);
        job.setSalary(request.salary);
        job.setLocation(request.location);
        job.setCategory(request.category);

        return jobRepository.save(job);
    }

    public void deleteJob(Long jobId, User recruiter) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiter.getId()))
            throw new RuntimeException("Unauthorized");

        if (job.getStatus() == JobStatus.CLOSED)
            throw new RuntimeException("Cannot delete closed jobs");

        jobRepository.delete(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }
}

