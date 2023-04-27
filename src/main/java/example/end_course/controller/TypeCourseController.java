package example.end_course.controller;


import example.end_course.model.TypeCourse;
import example.end_course.repository.TypeCourseRepository;
import example.end_course.service.typeCourse.TypeCourseService;
import example.end_course.util.Gson;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/admin/type-course")
public class TypeCourseController {
    @Autowired
    private TypeCourseService typeCourseService;
    @Autowired
    private TypeCourseRepository typeCourseRepository;
    private final com.google.gson.Gson gson = Gson.gson();

    @GetMapping("/list")
    public List<TypeCourse> getTypeCourse() {
        return typeCourseService.getTypeCourses();
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid String typeCourse, BindingResult result) {
        if (result.hasErrors()) {

            Map<String, String> errorMap = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errorMap.put(error.getField(), error.getDefaultMessage());
            });
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        TypeCourse typeCourse1 = gson.fromJson(typeCourse, TypeCourse.class);
        return new ResponseEntity<>(typeCourseService.save(typeCourse1), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<TypeCourse> update(@RequestBody String typeCourse, @PathVariable Integer id) {
        return typeCourseService.getTypeCourseById(id).map(typeCourse1 -> {
            TypeCourse typeCourse2 = gson.fromJson(typeCourse, TypeCourse.class);
            typeCourse1.setName(typeCourse2.getName());
            return new ResponseEntity<>(typeCourseService.save(typeCourse1), HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeCourse> getbyId(@PathVariable Integer id) {
        try {
            TypeCourse typeCourse = typeCourseService.getTypeCourseById(id).get();
            return new ResponseEntity<>(typeCourse, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            TypeCourse typeCourse = typeCourseService.getTypeCourseById(id).get();
            typeCourseService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/pagination")
    public Page<TypeCourse> pagination(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        return typeCourseService.pagination(pageable);
    }
}
