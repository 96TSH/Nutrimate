package sg.edu.ntu.nutrimate.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.ntu.nutrimate.entity.Review;
import sg.edu.ntu.nutrimate.entity.ReviewWithCourse;
import sg.edu.ntu.nutrimate.entity.ReviewWithCustomerRecipe;
import sg.edu.ntu.nutrimate.service.ReviewService;

@RestController
// @RequestMapping("/customers/review")
@RequestMapping("/nutrimate")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    //CRUD

    //Create Review as General Review by User
    @PostMapping("/customers/reviews/create")
    public ResponseEntity<Review> createReview(@RequestBody Review review){
        Review newReview = reviewService.createReview(review);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }

    //Create Review for Specific CourseID by User
    @PostMapping("/customers/reviews/create/courses/{courseId}")
    public ResponseEntity<ReviewWithCourse>createCourseReview(@PathVariable int courseId, @RequestBody ReviewWithCourse review){
        ReviewWithCourse newCourseReview = reviewService.createReviewWithCourse(courseId, review);
        return new ResponseEntity<>(newCourseReview, HttpStatus.CREATED);
    }

    // Create Review for Specific CustomerRecipe by User
    @PostMapping("/customers/reviews/create/recipes/{recipeId}")
    public ResponseEntity<ReviewWithCustomerRecipe>createCustomerRecipeReview(@PathVariable int recipeId, @RequestBody ReviewWithCustomerRecipe review){
        ReviewWithCustomerRecipe newCourseReview = reviewService.createReviewWithCustomerRecipe(recipeId, review);
        return new ResponseEntity<>(newCourseReview, HttpStatus.CREATED);
    }

    //Search All Reviews
    @GetMapping("/customers/reviews")
    public ResponseEntity<ArrayList<Review>> getAllReviews(){
        ArrayList<Review> foundReview = reviewService.getAllReviews();
        return new ResponseEntity<>(foundReview, HttpStatus.OK);
    }

    //Search All Reviews By Course Id
    @GetMapping("/customers/reviews/courses/{courseId}")
    public ResponseEntity<ArrayList<ReviewWithCourse>> getAllReviewWithCourse(@PathVariable int courseId){
        ArrayList<ReviewWithCourse> foundReview = reviewService.getAllReviewsWithCourse(courseId);
        return new ResponseEntity<>(foundReview, HttpStatus.OK);
    }

    //Search All Reviews By CustomerRecipe Id
    @GetMapping("/customers/reviews/recipes/{recipeId}")
    public ResponseEntity<ArrayList<ReviewWithCustomerRecipe>> getAllReviewWithCustomerRecipe(@PathVariable int recipeId){
        ArrayList<ReviewWithCustomerRecipe> foundReview = reviewService.getAllReviewsWithCustomerRecipe(recipeId);
        return new ResponseEntity<>(foundReview, HttpStatus.OK);
    }

    //Update User's Specific General Review
    @PutMapping("/customers/reviews/update/{reviewId}")
    public ResponseEntity<Object> updateReview(@PathVariable int reviewId, @RequestBody Review review){
        Review updatedReview = reviewService.updateReview(reviewId, review);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    //Update User's Specific Review by Specific Course
    @PutMapping("/customers/reviews/courses/update/{reviewId}")
    public ResponseEntity<Object> updateReview(@PathVariable int reviewId, @RequestBody ReviewWithCourse review){
        ReviewWithCourse updatedReview = reviewService.updateReviewWithCourse(reviewId, review);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }
    
    //Update User's Specific Review by Specific Customer Recipe
    @PutMapping("/customers/reviews/recipes/update/{reviewId}")
    public ResponseEntity<Object> updateReview(@PathVariable int reviewId, @RequestBody ReviewWithCustomerRecipe review){
        ReviewWithCustomerRecipe updatedReview = reviewService.updateReviewWithCustomerRecipe(reviewId, review);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    //Delete User's Specific General Review
    @DeleteMapping("/customers/reviews/delete/{reviewId}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable int reviewId){
        reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Delete User's Specific Review by Specific Course
    @DeleteMapping("/customers/reviews/courses/delete/{reviewId}")
    public ResponseEntity<HttpStatus> deleteReviewWithCourse(@PathVariable int reviewId){
    reviewService.deleteReviewWithCourse(reviewId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Delete User's Specific Review by Specific Review
    @DeleteMapping("/customers/reviews/recipes/delete/{reviewId}")
    public ResponseEntity<HttpStatus> deleteReviewWithCustomerRecipe(@PathVariable int reviewId){
        reviewService.deleteReviewWithCustomerRecipe(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
