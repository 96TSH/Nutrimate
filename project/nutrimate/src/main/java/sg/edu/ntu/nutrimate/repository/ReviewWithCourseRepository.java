package sg.edu.ntu.nutrimate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.ntu.nutrimate.entity.Course;
import sg.edu.ntu.nutrimate.entity.Review;
import sg.edu.ntu.nutrimate.entity.ReviewWithCourse;

@Repository
public interface ReviewWithCourseRepository extends JpaRepository<ReviewWithCourse, Integer> {
    List<ReviewWithCourse> findByCourseCourseId(int courseId);
    List<ReviewWithCourse> findByCourse(Course course);
}

