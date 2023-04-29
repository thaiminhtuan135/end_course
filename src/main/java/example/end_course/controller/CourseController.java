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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

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
    public ResponseEntity<Course> create(
            @RequestParam String name,
            @RequestParam LocalDate time,
            @RequestParam String introduce,
            @RequestParam String content,
            @RequestParam Integer price,
            @RequestParam Integer amount_student,
            @RequestParam Integer amount_subject,
            @RequestParam(value = "image",required = false) MultipartFile image,
            @PathVariable Integer typeCourseId) throws IOException {
        TypeCourse typeCourse = typeCourseService.getTypeCourseById(typeCourseId).get();
        Course course1 = new Course();
        System.out.println(CURRENT_FOLDER); // C:\Users\ADMIN\Desktop\java\end_course
        if (image != null) {
            System.out.println(image.getOriginalFilename());
            Path staticPath = Paths.get("static"); // static
            Path imagePath = Paths.get("images");   // images

            if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
                Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
            }

            int hashCode = Objects.requireNonNull(image.getOriginalFilename()).hashCode();

            Path file = CURRENT_FOLDER.resolve(staticPath)
                    .resolve(imagePath).resolve(hashCode + "." + image.getOriginalFilename());
            System.out.println(file); // C:\Users\ADMIN\Desktop\java\end_course\static\images\...

            try (OutputStream os = Files.newOutputStream(file)) {
                os.write(image.getBytes());
            }
            course1.setImage(imagePath.resolve(hashCode+"."+image.getOriginalFilename()).toString());
        }

        course1.setName(name);
        course1.setTime(time);
        course1.setIntroduce(introduce);
        course1.setContent(content);
        course1.setPrice(price);
        course1.setAmount_student(amount_student);
        course1.setAmount_subject(amount_subject);

        course1.setTypeCourse(typeCourse);
        course1.setTypeCourse_id(typeCourseId);

        return new ResponseEntity<>(courseService.save(course1), HttpStatus.CREATED);

    }

    @PutMapping("/{courseId}/edit/type-course/{typeCourseId}")
    public ResponseEntity<Course> update(@RequestParam String name, @RequestParam LocalDate time,
                                         @RequestParam String introduce, @RequestParam String content,
                                         @RequestParam Integer price, @RequestParam Integer amount_student,
                                         @RequestParam Integer amount_subject,
                                         @RequestParam(value = "image",required = false) MultipartFile image,
                                         @PathVariable Integer typeCourseId,
                                         @PathVariable Integer courseId)  {

        return courseService.getCourseById(courseId).map(course1 -> {

            try {
                TypeCourse typeCourse = typeCourseService.getTypeCourseById(typeCourseId).get();

//                Path staticPath = Paths.get("static");
//                Path imagePath = Paths.get("images");
//                if (course1.getImage() == null) {
//                    if (image != null) {
//                        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
//                            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
//                        }
//
//                        int hashCode = Objects.requireNonNull(image.getOriginalFilename()).hashCode();
//
//                        Path file = CURRENT_FOLDER.resolve(staticPath)
//                                .resolve(imagePath).resolve(hashCode + "." + image.getOriginalFilename());
//
//                        try (OutputStream os = Files.newOutputStream(file)) {
//                            os.write(image.getBytes());
//                        }
//                        course1.setImage(imagePath.resolve(hashCode+"."+image.getOriginalFilename()).toString());
//                    }
//                }
//                else {
//                    if (image != null) {
//                        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
//                            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
//                        }
//
//                        int hashCode = Objects.requireNonNull(image.getOriginalFilename()).hashCode();
//
//                        Path file = CURRENT_FOLDER.resolve(staticPath)
//                                .resolve(imagePath).resolve(hashCode + "." + image.getOriginalFilename());
//                        System.out.println(file); // C:\Users\ADMIN\Desktop\java\end_course\static\images\...
//
//                        try (OutputStream os = Files.newOutputStream(file)) {
//                            os.write(image.getBytes());
//                        }
//                        course1.setImage(imagePath.resolve(hashCode+"."+image.getOriginalFilename()).toString());
//                    }
//                    else {
//                        Path fileToDelete = Paths.get("src/main/resources/static/" + course1.getImage());
//                        if (Files.exists(fileToDelete)) {
//                            Files.delete(fileToDelete);
//                        }
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
