package com.example.capstoneWorkline.repository;

import com.example.capstoneWorkline.entity.Job;
import com.example.capstoneWorkline.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByRecruiter(User recruiter);
}

