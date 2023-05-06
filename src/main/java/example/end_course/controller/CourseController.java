package example.end_course.controller;


import example.end_course.model.Course;
import example.end_course.model.TypeCourse;
import example.end_course.service.course.CourseService;
import example.end_course.service.typeCourse.TypeCourseService;
import example.end_course.util.Gson;
import example.end_course.util.UploadImage;
import example.end_course.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/admin/course")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private TypeCourseService typeCourseService;
    private final com.google.gson.Gson gson = Gson.gson();

    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    @GetMapping("/list")
    public List<Course> getCourses() {
        return courseService.getCourses();
    }

    @PostMapping("/create/type-course/{typeCourseId}")
    public ResponseEntity<?> create(
            @RequestParam String name,
            @RequestParam LocalDate time,
            @RequestParam String introduce,
            @RequestParam String content,
            @RequestParam Integer price,
            @RequestParam Integer amount_student,
            @RequestParam Integer amount_subject,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @PathVariable Integer typeCourseId) throws IOException {
        TypeCourse typeCourse = typeCourseService.getTypeCourseById(typeCourseId).get();
        Course course1 = new Course();



        course1.setName(name);
        course1.setTime(time);
        course1.setIntroduce(introduce);
        course1.setContent(content);
        course1.setPrice(price);
        course1.setAmount_student(amount_student);
        course1.setAmount_subject(amount_subject);

        course1.setTypeCourse(typeCourse);
        course1.setTypeCourse_id(typeCourseId);
        List<String> errors = Validator.validateObject(course1);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
//        if (!image.isEmpty()) {
//            course1.setImage(UploadImage.upload(image,"images"));
//        }
        return new ResponseEntity<>(courseService.save(course1), HttpStatus.CREATED);

    }

    @PutMapping("/{courseId}/edit/type-course/{typeCourseId}")
    public ResponseEntity<Course> update(@RequestParam String name,
                                         @RequestParam LocalDate time,
                                         @RequestParam String introduce,
                                         @RequestParam String content,
                                         @RequestParam Integer price,
                                         @RequestParam Integer amount_student,
                                         @RequestParam Integer amount_subject,
                                         @RequestParam(value = "image", required = false) MultipartFile image,
                                         @PathVariable Integer typeCourseId,
                                         @PathVariable Integer courseId){

        return courseService.getCourseById(courseId).map(course1 -> {

            Path staticPath = Paths.get("static");
            Path imagePath = Paths.get("images");
            try {
                TypeCourse typeCourse = typeCourseService.getTypeCourseById(typeCourseId).get();

//                if (course1.getImage() == null || course1.getImage().equals("")) {
//                    if (!image.isEmpty()) {
//                        course1.setImage(UploadImage.upload(image,"images"));
//                    }
//                } else {
//                    if (!image.isEmpty())
//                    {
//                        Path fileToDelete = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(course1.getImage());
//                        System.out.println(fileToDelete);
//                        try {
//                            Files.deleteIfExists(fileToDelete);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        course1.setImage(UploadImage.upload(image,"images"));
//                    }
//                    else {
//                        Path fileToDelete = Paths.get("src/main/resources/static/" + course1.getImage());
//                        if (Files.exists(fileToDelete)) {
//                            Files.delete(fileToDelete);
//                        }
//                        course1.setImage("");
//                    }
//                }
                course1.setName(name);
                course1.setTime(time);
                course1.setIntroduce(introduce);
                course1.setContent(content);
                course1.setPrice(price);
                course1.setAmount_student(amount_student);
                course1.setAmount_subject(amount_subject);

                course1.setTypeCourse(typeCourse);
                course1.setTypeCourse_id(typeCourseId);

                return new ResponseEntity<>(courseService.save(course1), HttpStatus.OK);
            } catch (NoSuchElementException e) {
                return new ResponseEntity<Course>(HttpStatus.NOT_FOUND);
            }
//            catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            catch (IOException e) {
//                throw new RuntimeException(e);
//            }
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
