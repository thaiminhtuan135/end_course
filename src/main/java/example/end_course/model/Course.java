package example.end_course.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Name is required")
    @Size(max = 255 , message = "Name limit 255 characters")
    private String name;
    private LocalDate time;
    private String introduce;
    @NotEmpty(message = "Content is required")
    private String content;
    private int price;
    @Min(value = 20, message = "The number of student must be more than 20")
    @Max(value = 50, message = "The number of student is less than 50")
    private int amount_student;
    private int amount_subject;
    @Nullable
    private String image;
    @Column(name = "typeCourse_id", insertable = false, updatable = false)
    private int typeCourse_id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeCourse_id")
    @JsonBackReference
    private TypeCourse typeCourse;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
    @JsonManagedReference
    private List<Register> registers;
}
