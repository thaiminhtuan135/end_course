package example.end_course.DTO;

import example.end_course.model.Account;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class PostDTO {
    private int id;
    private String name;
    private LocalDate createAt;
    private String author;
    private String content;
    private String shortContent;
    private int accountId;
    private String nickName;
    private int topicId;
    private String topicName;


}
