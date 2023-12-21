package sg.edu.ntu.nutrimate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.ntu.nutrimate.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

}
