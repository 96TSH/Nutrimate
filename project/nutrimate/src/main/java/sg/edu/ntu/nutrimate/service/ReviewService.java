package sg.edu.ntu.nutrimate.service;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.nutrimate.entity.Review;
import sg.edu.ntu.nutrimate.entity.ReviewWithCourse;
import sg.edu.ntu.nutrimate.entity.ReviewWithCustomerRecipe;

public interface ReviewService {

    //General Review Service
    Review createReview(Review review);
    Review getReview(int reviewId);
    ArrayList<Review>getAllReviews();
    Review updateReview(int reviewId, Review review);
    void deleteReview(int reviewId);

   //Course Review Service
    ReviewWithCourse createReviewWithCourse(int courseId, ReviewWithCourse review);
    ReviewWithCourse getReviewWithCourse(int reviewId);
    ArrayList<ReviewWithCourse> getAllReviewsWithCourse(int courseId);
    ReviewWithCourse updateReviewWithCourse(int reviewId, ReviewWithCourse review);
    void deleteReviewWithCourse(int reviewId);


    //Customer Recipe Review Service
    ReviewWithCustomerRecipe createReviewWithCustomerRecipe(int recipeId, ReviewWithCustomerRecipe review);
    ReviewWithCustomerRecipe getReviewWithCustomerRecipe(int reviewId);
    ArrayList<ReviewWithCustomerRecipe> getAllReviewsWithCustomerRecipe(int recipeId);
    ReviewWithCustomerRecipe updateReviewWithCustomerRecipe(int reviewId, ReviewWithCustomerRecipe review);
    void deleteReviewWithCustomerRecipe(int reviewId);

}
