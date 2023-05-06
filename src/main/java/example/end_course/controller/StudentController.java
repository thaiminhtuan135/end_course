package example.end_course.controller;

import example.end_course.model.Student;
import example.end_course.service.student.StudentService;
import example.end_course.util.Gson;
import example.end_course.util.UploadImage;
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
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/admin/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    private final com.google.gson.Gson gson = Gson.gson();

    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    @GetMapping("/list")
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @PostMapping("/create")
    private ResponseEntity<?> create(@RequestParam String name,
                                     @RequestParam LocalDate dob,
                                     @RequestParam String telephone,
                                     @RequestParam String email,
                                     @RequestParam String province,
                                     @RequestParam String district,
                                     @RequestParam String wards,
                                     @RequestParam Integer apartmentNumber,
                                     @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        if (studentService.checkEmailExist(email, null)) {
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
//        if (!image.isEmpty()) {
//            student.setImage(UploadImage.upload(image, "images"));
//        }

        return new ResponseEntity<>(studentService.save(student), HttpStatus.CREATED);

    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<?> update(@RequestParam String name,
                                    @RequestParam LocalDate dob,
                                    @RequestParam String telephone,
                                    @RequestParam String email,
                                    @RequestParam String province,
                                    @RequestParam String district,
                                    @RequestParam String wards,
                                    @RequestParam Integer apartmentNumber,
                                    @RequestParam(value = "image", required = false) MultipartFile image,
                                    @PathVariable Integer id) throws IOException {
        if (studentService.checkEmailExist(email, id)) {
            return new ResponseEntity<>("Email Taken", HttpStatus.BAD_REQUEST);
        }

        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("students");

        return studentService.getStudentById(id).map(student -> {
//            try {
//                if (student.getImage() == null || student.getImage().equals("")) {
//                    if (!image.isEmpty()) {
//                        student.setImage(UploadImage.upload(image,"images"));
//                    }
//                } else {
//                    if (!image.isEmpty())
//                    {
//                        Path fileToDelete = CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(student.getImage());
//                        try {
//                            Files.deleteIfExists(fileToDelete);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        student.setImage(UploadImage.upload(image,"images"));
//                    }
//                    else {
//                        Path fileToDelete = Paths.get("src/main/resources/static/" + student.getImage());
//                        if (Files.exists(fileToDelete)) {
//                            Files.delete(fileToDelete);
//                        }
//                        student.setImage("");
//                    }
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

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
