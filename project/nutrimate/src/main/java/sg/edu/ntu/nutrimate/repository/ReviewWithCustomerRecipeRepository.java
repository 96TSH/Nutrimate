package sg.edu.ntu.nutrimate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.ntu.nutrimate.entity.Review;
import sg.edu.ntu.nutrimate.entity.ReviewWithCourse;
import sg.edu.ntu.nutrimate.entity.ReviewWithCustomerRecipe;

@Repository
public interface ReviewWithCustomerRecipeRepository extends JpaRepository<ReviewWithCustomerRecipe, Integer> {
    List<ReviewWithCustomerRecipe> findByRecipeRecipeId(int recipeId);
}

