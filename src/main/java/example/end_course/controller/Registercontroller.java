package example.end_course.controller;


import example.end_course.model.*;
import example.end_course.service.account.AccountService;
import example.end_course.service.course.CourseService;
import example.end_course.service.register.RegisterService;
import example.end_course.service.status.StatusService;
import example.end_course.service.student.StudentService;
import example.end_course.util.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/admin/register")
public class Registercontroller {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private StudentService studentService;

    @Autowired
    private AccountService accountService;

    private final com.google.gson.Gson gson = Gson.gson();

    @GetMapping("/list")
    public List<Register> getTypePost() {
        return registerService.getRegisters();
    }


    @PostMapping("/create/course/{courseId}/student/{studentId}/status/{statusId}/account/{accountId}")
    private ResponseEntity<?> create(@RequestBody String register,
                                     @PathVariable Integer accountId,
                                     @PathVariable Integer courseId,
                                     @PathVariable Integer statusId,
                                     @PathVariable Integer studentId) {

        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
        }

        Optional<Account> account = accountService.getAccountById(accountId);
        if (account.isEmpty()) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        Optional<Status> status = statusService.getStatusById(statusId);
        if (status.isEmpty()) {
            return new ResponseEntity<>("Status not found", HttpStatus.NOT_FOUND);
        }

        Optional<Student> student = studentService.getStudentById(studentId);
        if (student.isEmpty()) {
            return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
        }
        try {
            Register register1 = gson.fromJson(register, Register.class);
            register1.setAccount(account.get());
            register1.setAccount_id(accountId);

            register1.setCourse(course.get());
            register1.setCourse_id(courseId);

            register1.setStatus(status.get());
            register1.setStatus_id(statusId);

            register1.setStudent(student.get());
            register1.setStudent_id(studentId);

            return new ResponseEntity<>(registerService.save(register1), HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{registerId}/edit/course/{courseId}/student/{studentId}/status/{statusId}/account/{accountId}")
    public ResponseEntity<?> update(@RequestBody String dataUpdate,
                                    @PathVariable Integer registerId,
                                    @PathVariable Integer courseId,
                                    @PathVariable Integer studentId,
                                    @PathVariable Integer statusId,
                                    @PathVariable Integer accountId) {

        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
        }

        Optional<Account> account = accountService.getAccountById(accountId);
        if (account.isEmpty()) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        Optional<Status> status = statusService.getStatusById(statusId);
        if (status.isEmpty()) {
            return new ResponseEntity<>("Status not found", HttpStatus.NOT_FOUND);
        }

        Optional<Student> student = studentService.getStudentById(studentId);
        if (student.isEmpty()) {
            return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
        }

        return registerService.getRegisterById(registerId).map(register -> {

            Register register1 = gson.fromJson(dataUpdate, Register.class);
            register1.setId(register.getId());

            register1.setAccount(account.get());
            register1.setAccount_id(accountId);

            register1.setCourse(course.get());
            register1.setCourse_id(courseId);

            register1.setStatus(status.get());
            register1.setStatus_id(statusId);

            register1.setStudent(student.get());
            register1.setStudent_id(studentId);

            return new ResponseEntity<>(registerService.save(register1), HttpStatus.OK);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Register not found"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getbyId(@PathVariable Integer id) {
        try {
            Register register = registerService.getRegisterById(id).get();
            return new ResponseEntity<>(register, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Register not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            Register register = registerService.getRegisterById(id).get();
            registerService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Register not found", HttpStatus.NOT_FOUND);
        }
    }
}
