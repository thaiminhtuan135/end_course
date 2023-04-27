package example.end_course.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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

    private String name;
    private LocalDate time;
    private String introduce;
    private String content;
    private int price;
    private int amount_student;
    private int amount_subject;
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
