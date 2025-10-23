package com.ucb.plataforma.res.reviews.repository;

import com.ucb.plataforma.res.reviews.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCourseId(Long courseId);
    List<Review> findByUserId(Long userId);

    // Derived query
    List<Review> findByRatingGreaterThanEqual(int minRating);

    // JPQL query
    @Query("SELECT r FROM Review r WHERE LOWER(r.comment) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Review> searchByCommentContaining(@Param("keyword") String keyword);

    // Pageable support
    Page<Review> findAll(Pageable pageable);
}
