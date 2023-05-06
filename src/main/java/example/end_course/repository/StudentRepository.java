package example.end_course.repository;

import example.end_course.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

//    @Query( "select s from Student s where s.email = ?1")
//    Optional<Student> existsByEmail(String email);
//
//    @Query( "select s from Student s where s.email = ?1 and s.id <> ?2")
//    Optional<Student> existsByEmailUpdate(String email,int id);

    @Query("SELECT s FROM Student s WHERE s.email = :email AND (s.id != :id OR :id IS NULL)")
    Optional<Student> existsByEmail(@Param("email") String email, @Param("id") Integer id);
}
