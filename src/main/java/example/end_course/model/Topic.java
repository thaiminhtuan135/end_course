package example.end_course.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String content;
    @Column(name = "typePost_id",insertable = false, updatable = false)
    private int typePost_id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typePost_id")
    @JsonBackReference
    private TypePost typePost;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "topic")
    @JsonManagedReference
    private List<Post> posts;
}
