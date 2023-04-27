package example.end_course.controller;

import example.end_course.model.Student;
import example.end_course.service.student.StudentService;
import example.end_course.util.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/admin/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    private final com.google.gson.Gson gson = Gson.gson();

    @GetMapping("/list")
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @PostMapping("/create")
    private ResponseEntity<?> create(@RequestBody String student) {
        try {
            Student student1 = gson.fromJson(student, Student.class);
            return new ResponseEntity<>(studentService.save(student1), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Student> update(@RequestBody String dataUpdate, @PathVariable Integer id) {
        return studentService.getStudentById(id).map(student -> {
            Student student1 = gson.fromJson(dataUpdate, Student.class);
            student1.setId(student.getId());
            return new ResponseEntity<>(studentService.save(student1), HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getbyId(@PathVariable Integer id) {
        try {
            Student student = studentService.getStudentById(id).get();
            return new ResponseEntity<>(student, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            Student student = studentService.getStudentById(id).get();
            studentService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
        }
    }

}
