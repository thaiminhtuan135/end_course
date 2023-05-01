package example.end_course.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table
@AllArgsConstructor(staticName = "build")
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class TypeCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Name is not empty")
    @Size(min = 5 , message = "Name type course at least 5 characters")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "typeCourse")
    @JsonManagedReference
    private List<Course> courses;
}
