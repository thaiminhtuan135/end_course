package example.end_course.repository;

import example.end_course.model.TypeCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeCourseRepository extends JpaRepository<TypeCourse, Integer> {
    Page<TypeCourse> findAll(Pageable pageable);

}
