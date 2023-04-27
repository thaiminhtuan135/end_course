package example.end_course.controller;


import example.end_course.model.Course;
import example.end_course.model.TypeCourse;
import example.end_course.service.course.CourseService;
import example.end_course.service.typeCourse.TypeCourseService;
import example.end_course.util.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("admin/course")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private TypeCourseService typeCourseService;
    private final com.google.gson.Gson gson = Gson.gson();

    @GetMapping("/list")
    public List<Course> getCourses() {
        return courseService.getCourses();
    }

    @PostMapping("/create/type-course/{typeCourseId}")
    public ResponseEntity<Course> create(@RequestBody String course, @PathVariable Integer typeCourseId) {

        return typeCourseService.getTypeCourseById(typeCourseId).map(typeCourse -> {
            Course course1 = gson.fromJson(course, Course.class);
            course1.setTypeCourse(typeCourse);
            course1.setTypeCourse_id(typeCourse.getId());
            return new ResponseEntity<>(courseService.save(course1), HttpStatus.CREATED);
        }).orElseGet(
                () -> new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @PutMapping("/{courseId}/edit/type-course/{typeCourseId}")
    public ResponseEntity<Course> update(@RequestBody String course,
                                         @PathVariable Integer courseId,
                                         @PathVariable Integer typeCourseId) {

        return courseService.getCourseById(courseId).map(course1 -> {

            try {
                TypeCourse typeCourse = typeCourseService.getTypeCourseById(typeCourseId).get();
                Course course2 = gson.fromJson(course, Course.class);
                course2.setId(course1.getId());
                course2.setTypeCourse(typeCourse);
                course2.setTypeCourse_id(typeCourse.getId());
                return new ResponseEntity<>(courseService.save(course2), HttpStatus.OK);
            } catch (NoSuchElementException e) {
                return new ResponseEntity<Course>(HttpStatus.NOT_FOUND);
            }
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getbyId(@PathVariable Integer id) {
        try {
            Course course = courseService.getCourseById(id).get();
            return new ResponseEntity<>(course, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            Course Course = courseService.getCourseById(id).get();
            courseService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
