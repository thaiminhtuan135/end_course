package example.end_course.service.student;

import example.end_course.model.Student;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student save(Student student);

    Optional<Student> getStudentById(int id);

    void delete(int id);

    List<Student> getStudents();

    Student update(Student student);

    boolean checkEmailExist(String email,Integer id);
}
