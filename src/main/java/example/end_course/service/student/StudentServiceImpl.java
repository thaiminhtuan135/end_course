package example.end_course.service.student;

import example.end_course.model.Student;
import example.end_course.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> getStudentById(int id) {
        return studentRepository.findById(id);
    }

    @Override
    public void delete(int id) {
    studentRepository.deleteById(id);
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student update(Student student) {

        return studentRepository.save(student);
    }

    @Override
    public boolean checkEmailExist(String email, Integer id) {
        Optional<Student> studentOptional = studentRepository.existsByEmail(email,id);

        return studentOptional.isPresent();
    }
}
