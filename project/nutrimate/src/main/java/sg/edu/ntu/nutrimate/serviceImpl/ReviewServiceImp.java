package sg.edu.ntu.nutrimate.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sg.edu.ntu.nutrimate.entity.Course;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.entity.CustomerRecipe;
import sg.edu.ntu.nutrimate.entity.Review;
import sg.edu.ntu.nutrimate.entity.ReviewWithCourse;
import sg.edu.ntu.nutrimate.entity.ReviewWithCustomerRecipe;
import sg.edu.ntu.nutrimate.exception.ReviewNotFoundException;
import sg.edu.ntu.nutrimate.exception.ReviewNotUpdateException;
import sg.edu.ntu.nutrimate.repository.CourseRepository;
import sg.edu.ntu.nutrimate.repository.CustomerRepository;
import sg.edu.ntu.nutrimate.repository.RecipeRepository;
import sg.edu.ntu.nutrimate.repository.ReviewRepository;
import sg.edu.ntu.nutrimate.repository.ReviewWithCourseRepository;
import sg.edu.ntu.nutrimate.repository.ReviewWithCustomerRecipeRepository;
import sg.edu.ntu.nutrimate.security.AuthenticatedUser;
import sg.edu.ntu.nutrimate.service.ReviewService;

@Service
public class ReviewServiceImp implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewWithCourseRepository reviewWithCourseRepository;

    @Autowired
    private ReviewWithCustomerRecipeRepository reviewWithCustomerRecipeRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    // ------------------- General Review------------------
    // Create Review
    @Override
    public Review createReview(Review review) {
        Customer authenticatedCustomer = getAuthenticatedCustomer();
        review.setCustomer(authenticatedCustomer);
        return reviewRepository.save(review);
    }

    // Get Specific ReviewId
    @Override
    public Review getReview(int reviewId) {
        Optional<Review> foundReview = reviewRepository.findById(reviewId);
        if (foundReview.isPresent()) {
            return foundReview.get();
        } else {
            throw new ReviewNotFoundException(reviewId);
        }
    }

    // Get All Reviews
    @Override
    public ArrayList<Review> getAllReviews() {
        List<Review> foundAllReviews = reviewRepository.findAll();
        return (ArrayList<Review>) foundAllReviews;
    }

    //Update General Reviews
    @Override
    public Review updateReview(int reviewId, Review review) {
        Customer authenticatedCustomer = getAuthenticatedCustomer();
        review.setCustomer(authenticatedCustomer);

        Optional<Review> wrappedReviewToUpdate = reviewRepository.findById(reviewId);
        if (wrappedReviewToUpdate.isPresent()) {
            Review reviewToUpdate = wrappedReviewToUpdate.get();
            reviewToUpdate.setTitle(review.getTitle());
            reviewToUpdate.setDescription(review.getDescription());
            reviewToUpdate.setRating(review.getRating());
            return reviewRepository.save(reviewToUpdate);
        } else {
            throw new ReviewNotUpdateException(reviewId);
        }
    }

    //Delete General Reviews
    @Override
    public void deleteReview(int reviewId) {
        Customer authenticatedCustomer = getAuthenticatedCustomer();
        reviewRepository.deleteById(reviewId);

    }

    // -------------------- Course Review----------------------
    //Create Course Review
    @Override
    public ReviewWithCourse createReviewWithCourse(int courseId, ReviewWithCourse review) {
        Customer authenticatedCustomer = getAuthenticatedCustomer();
        review.setCustomer(authenticatedCustomer);
        Optional<Course> wrappedSelectedCourse = courseRepository.findByCourseId(courseId);
        if (!wrappedSelectedCourse.isPresent()) {
            throw new RuntimeException("No course found with this courseID");
        }
        Course selectedCourse = wrappedSelectedCourse.get();
        review.setCourse(selectedCourse);

        return reviewWithCourseRepository.save(review);

    }
    //Delete Course Review
    @Override
    public void deleteReviewWithCourse(int reviewId) {
        Customer authenticatedCustomer = getAuthenticatedCustomer();
        reviewWithCourseRepository.deleteById(reviewId);
    }
    //Get All Reviews from Course
    @Override
    public ArrayList<ReviewWithCourse> getAllReviewsWithCourse(int courseId) {
        List<ReviewWithCourse> foundAllReviews = reviewWithCourseRepository.findByCourseCourseId(courseId);
        if (foundAllReviews.isEmpty()) {
            throw new RuntimeException("No reviews for this course");
        }
        return (ArrayList<ReviewWithCourse>) foundAllReviews;
    }
    //Get Specific Reviews from Course
    @Override
    public ReviewWithCourse getReviewWithCourse(int reviewId) {
        Optional<ReviewWithCourse> foundReview = reviewWithCourseRepository.findById(reviewId);
        if (foundReview.isPresent()) {
            return foundReview.get();
        } else {
            throw new ReviewNotFoundException(reviewId);
        }
    }

    //Update Course Review
    @Override
    public ReviewWithCourse updateReviewWithCourse(int reviewId, ReviewWithCourse review) {
        Customer authenticatedCustomer = getAuthenticatedCustomer();
        review.setCustomer(authenticatedCustomer);
        Optional<ReviewWithCourse> wrappedReviewToUpdate = reviewWithCourseRepository.findById(reviewId);
        if (wrappedReviewToUpdate.isPresent()) {
            ReviewWithCourse reviewToUpdate = wrappedReviewToUpdate.get();
            reviewToUpdate.setTitle(review.getTitle());
            reviewToUpdate.setDescription(review.getDescription());
            reviewToUpdate.setRating(review.getRating());
            return reviewWithCourseRepository.save(reviewToUpdate);
        } else {
            throw new ReviewNotUpdateException(reviewId);
        }
    }

    // ------------------Customer Recipe Reviews-------------------
    //Create Customer Recipe Reviews
    @Override
    public ReviewWithCustomerRecipe createReviewWithCustomerRecipe(int recipeId,
            ReviewWithCustomerRecipe review) {
        Customer authenticatedCustomer = getAuthenticatedCustomer();
        review.setCustomer(authenticatedCustomer);
        Optional<CustomerRecipe> wrappedSelectedRecipe = recipeRepository.findByRecipeId(recipeId);
        if (!wrappedSelectedRecipe.isPresent()) {
            throw new RuntimeException("No customer recipe found with this customerRecipe");
        }

        CustomerRecipe selectedRecipe = wrappedSelectedRecipe.get();
        review.setRecipe(selectedRecipe);

        return reviewWithCustomerRecipeRepository.save(review);
    }

    // Get All Customer Recipe Reviews
    @Override
    public ArrayList<ReviewWithCustomerRecipe> getAllReviewsWithCustomerRecipe(int recipeId) {
        List<ReviewWithCustomerRecipe> foundAllReviews = reviewWithCustomerRecipeRepository
                .findByRecipeRecipeId(recipeId);
        if (foundAllReviews.isEmpty()) {
            throw new RuntimeException("No reviews for this Customer Recipe");
        }
        return (ArrayList<ReviewWithCustomerRecipe>) foundAllReviews;
    }

    // Get Customer Recipe Specific Review
    @Override
    public ReviewWithCustomerRecipe getReviewWithCustomerRecipe(int reviewId) {
        Optional<ReviewWithCustomerRecipe> foundReview = reviewWithCustomerRecipeRepository.findById(reviewId);
        if (foundReview.isPresent()) {
            return foundReview.get();
        } else {
            throw new ReviewNotFoundException(reviewId);
        }
    }

    //Update Customer Recipe Review
    @Override
    public ReviewWithCustomerRecipe updateReviewWithCustomerRecipe(int reviewId,ReviewWithCustomerRecipe review) {
        Optional<ReviewWithCustomerRecipe> wrappedReviewToUpdate = reviewWithCustomerRecipeRepository.findById(reviewId);
        Customer authenticatedCustomer = getAuthenticatedCustomer();
        review.setCustomer(authenticatedCustomer);
        if (wrappedReviewToUpdate.isPresent()) {
            ReviewWithCustomerRecipe reviewToUpdate = wrappedReviewToUpdate.get();
            reviewToUpdate.setTitle(review.getTitle());
            reviewToUpdate.setDescription(review.getDescription());
            reviewToUpdate.setRating(review.getRating());
            return reviewWithCustomerRecipeRepository.save(reviewToUpdate);
        } else {
            throw new ReviewNotUpdateException(reviewId);
        }
    }

    //Delete Customer Recipe Review
    @Override
    public void deleteReviewWithCustomerRecipe(int reviewId) {
        Customer authenticatedCustomer = getAuthenticatedCustomer();
        reviewWithCustomerRecipeRepository.deleteById(reviewId);
        }

    // ------------------------------ Private Methods----------------------------------------------------

    private Customer getAuthenticatedCustomer() {
        String username = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();

        Optional<Customer> customerContainer = customerRepository.findByUserID(username.toLowerCase());

        if (!customerContainer.isPresent()) {
            customerContainer = customerRepository.findByEmail(username.toLowerCase());
        }

        if (!customerContainer.isPresent()) {
            throw new BadCredentialsException("Invalid Username");
        }

        return customerContainer.get();
    }

}

