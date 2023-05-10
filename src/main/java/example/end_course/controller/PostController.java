package example.end_course.controller;

import example.end_course.DTO.PostDTO;
import example.end_course.model.Account;
import example.end_course.model.Post;
import example.end_course.model.Topic;
import example.end_course.service.account.AccountService;
import example.end_course.service.post.PostService;
import example.end_course.service.topic.TopicService;
import example.end_course.util.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/admin/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private AccountService accountService;
    private final com.google.gson.Gson gson = Gson.gson();

    @GetMapping("/list")
    public List<PostDTO> getPosts() {
        List<PostDTO> postDTOS = postService.getPosts().stream().map(post -> {

            Account account = accountService.getAccountById(post.getAccount_id()).get();
            Topic topic = topicService.getTopicById(post.getTopic_id()).get();
            return PostDTO.builder()
                    .id(post.getId())
                    .name(post.getName())
                    .createAt(post.getCreateAt())
                    .author(post.getAuthor())
                    .content(post.getContent())
                    .shortContent(post.getShortContent())
                    .accountId(account.getId())
                    .nickName(account.getNickName())
                    .topicId(topic.getId())
                    .topicName(topic.getName())
                    .build();
        }).collect(Collectors.toList());
        return postDTOS;
    }

    @PostMapping("/create/topic/{topicId}/account/{accountId}")
    private ResponseEntity<?> create(@RequestBody String post,
                                     @PathVariable Integer topicId,
                                     @PathVariable Integer accountId) {

        Optional<Topic> topic = topicService.getTopicById(topicId);
        if (topic.isEmpty()) {
            return new ResponseEntity<>("Topic not found", HttpStatus.NOT_FOUND);

        }
        Optional<Account> account = accountService.getAccountById(accountId);
        if (account.isEmpty()) {
            return new ResponseEntity<>("account not found", HttpStatus.NOT_FOUND);

        }

        try {
            Post post1 = gson.fromJson(post, Post.class);
            post1.setTopic(topic.get());
            post1.setTopic_id(topicId);

            post1.setAccount(account.get());
            post1.setAccount_id(accountId);
            return new ResponseEntity<>(postService.save(post1), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
//        return topicService.getTopicById(topicId).map(topic -> {
//            Post post1 = gson.fromJson(post, Post.class);
//            post1.setTopic(topic);
//            post1.setTopic_id(topic.getId());
//            return new ResponseEntity<>(postService.save(post1), HttpStatus.OK);
//        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{postId}/edit/topic/{topicId}/account/{accountId}")
    public ResponseEntity<?> update(@RequestBody String dataUpdate,
                                    @PathVariable Integer postId,
                                    @PathVariable Integer topicId,
                                    @PathVariable Integer accountId) {

        Optional<Topic> topic = topicService.getTopicById(topicId);
        if (topic.isEmpty()) {
            return new ResponseEntity<>("Topic not found", HttpStatus.NOT_FOUND);

        }
        Optional<Account> account = accountService.getAccountById(accountId);
        if (account.isEmpty()) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);

        }

        return postService.getPostById(postId).map(post -> {
                Post post1 = gson.fromJson(dataUpdate, Post.class);
                post1.setId(post.getId());
                post1.setTopic(topic.get());
                post1.setTopic_id(topicId);

                post1.setAccount(account.get());
                post1.setAccount_id(accountId);
                return new ResponseEntity<>(postService.save(post1), HttpStatus.OK);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not found"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getbyId(@PathVariable Integer id) {
        try {
            Post post = postService.getPostById(id).get();
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            Post post = postService.getPostById(id).get();
            postService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        }
    }
}
