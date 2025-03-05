package com.feedback.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.feedback.entity.Feedback;

@Repository
public interface FeedBackRepo extends JpaRepository<Feedback, Long> {
     List<Feedback> findByUserId(Long userId);
}