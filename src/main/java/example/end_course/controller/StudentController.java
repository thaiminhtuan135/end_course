package example.end_course.controller;

import example.end_course.model.Student;
import example.end_course.service.student.StudentService;
import example.end_course.util.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/admin/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    private final com.google.gson.Gson gson = Gson.gson();

    @GetMapping("/list")
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @PostMapping("/create")
    private ResponseEntity<?> create(@RequestParam String name ,
                                     @RequestParam LocalDate dob ,
                                     @RequestParam String telephone ,
                                     @RequestParam String email ,
                                     @RequestParam String province ,
                                     @RequestParam String district ,
                                     @RequestParam String wards ,
                                     @RequestParam Integer apartmentNumber ) {
        if (studentService.checkEmailExist(email,null)) {
            return new ResponseEntity<>("Email Taken", HttpStatus.BAD_REQUEST);
        }
            Student student = new Student();
            student.setName(name);
            student.setDob(dob);
            student.setTelephone(telephone);
            student.setEmail(email);
            student.setProvince(province);
            student.setDistrict(district);
            student.setWards(wards);
            student.setApartmentNumber(apartmentNumber);

            return new ResponseEntity<>(studentService.save(student), HttpStatus.CREATED);

    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<?> update(@RequestParam String name ,
                                          @RequestParam LocalDate dob ,
                                          @RequestParam String telephone ,
                                          @RequestParam String email ,
                                          @RequestParam String province ,
                                          @RequestParam String district ,
                                          @RequestParam String wards ,
                                          @RequestParam Integer apartmentNumber,
                                          @PathVariable Integer id) {
        return studentService.getStudentById(id).map(student -> {
            if (studentService.checkEmailExist(email,id)) {
                return new ResponseEntity<>("Email Taken", HttpStatus.BAD_REQUEST);
            }
            student.setName(name);
            student.setDob(dob);
            student.setTelephone(telephone);
            student.setEmail(email);
            student.setProvince(province);
            student.setDistrict(district);
            student.setWards(wards);
            student.setApartmentNumber(apartmentNumber);

                return new ResponseEntity<>(studentService.save(student), HttpStatus.OK);
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
