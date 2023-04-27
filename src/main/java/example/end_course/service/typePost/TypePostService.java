package example.end_course.service.typePost;

import example.end_course.model.TypePost;

import java.util.List;
import java.util.Optional;

public interface TypePostService {

    TypePost save(TypePost typePost);

    Optional<TypePost> getTypePostById(int id);

    void delete(int id);

    List<TypePost> getTypePosts();
}
