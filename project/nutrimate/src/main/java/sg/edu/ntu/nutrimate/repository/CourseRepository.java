package sg.edu.ntu.nutrimate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.ntu.nutrimate.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByCourseId(int courseId);
}
