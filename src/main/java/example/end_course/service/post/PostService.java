package example.end_course.service.post;

import example.end_course.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post save(Post post);

    Optional<Post> getPostById(int id);

    void delete(int id);

    List<Post> getPosts();
}
