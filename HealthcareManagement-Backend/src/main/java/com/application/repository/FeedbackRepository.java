package com.application.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.application.model.Feedback;

public interface FeedbackRepository extends CrudRepository<Feedback, Integer> {
    List<Feedback> findAll();
}
