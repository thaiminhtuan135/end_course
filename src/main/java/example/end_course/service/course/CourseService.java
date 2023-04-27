package example.end_course.service.course;

import example.end_course.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    Course save(Course course);

    Optional<Course> getCourseById(int id);

    List<Course> getCourses();

    void delete(int id);
}
