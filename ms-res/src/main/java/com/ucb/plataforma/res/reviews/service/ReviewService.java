package com.ucb.plataforma.res.reviews.service;

import com.ucb.plataforma.res.reviews.dto.ReviewCreateRequest;
import com.ucb.plataforma.res.reviews.dto.ReviewResponse;
import com.ucb.plataforma.res.reviews.dto.ReviewUpdateRequest;

import java.util.List;

public interface ReviewService {
    List<ReviewResponse> findAll();
    ReviewResponse findById(long id);
    ReviewResponse create(ReviewCreateRequest request);
    ReviewResponse update(long id, ReviewUpdateRequest request);
    void delete(long id);
}
