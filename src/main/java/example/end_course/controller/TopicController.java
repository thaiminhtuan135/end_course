package example.end_course.controller;

import example.end_course.model.Topic;
import example.end_course.model.TypePost;
import example.end_course.service.topic.TopicService;
import example.end_course.service.typePost.TypePostService;
import example.end_course.util.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("admin/topic")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @Autowired
    private TypePostService typePostService;
    private final com.google.gson.Gson gson = Gson.gson();

    @GetMapping("/list")
    public List<Topic> getTypePost() {
        return topicService.getTopics();
    }


    @PostMapping("/create/type-post/{typePostId}")
    private ResponseEntity<Topic> create(@RequestBody String topic,
                                         @PathVariable Integer typePostId) {
        return typePostService.getTypePostById(typePostId).map(typePost -> {
            Topic topic1 = gson.fromJson(topic, Topic.class);
            topic1.setTypePost(typePost);
            topic1.setTypePost_id(typePost.getId());
            return new ResponseEntity<>(topicService.save(topic1), HttpStatus.CREATED);
        }).orElseGet(
                () -> new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @PutMapping("/{topicId}/edit/type-post/{typePostId}")
    public ResponseEntity<?> update(@RequestBody String dataUpdate,
                                    @PathVariable Integer topicId,
                                    @PathVariable Integer typePostId) {

        return topicService.getTopicById(topicId).map(topic -> {

            try {
                TypePost typePost = typePostService.getTypePostById(typePostId).get();
                Topic topic1 = gson.fromJson(dataUpdate, Topic.class);
                topic1.setId(topic.getId());
                topic1.setTypePost(typePost);
                topic1.setTypePost_id(typePost.getId());
                return new ResponseEntity<>(topicService.save(topic1), HttpStatus.OK);
            } catch (NoSuchElementException e) {
                return new ResponseEntity<>("Type post not found", HttpStatus.NOT_FOUND);
            }
        }).orElseGet(() -> new ResponseEntity<>("Topic not found", HttpStatus.NOT_FOUND));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Topic> getbyId(@PathVariable Integer id) {
        try {
            Topic topic = topicService.getTopicById(id).get();
            return new ResponseEntity<>(topic, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            Topic topic = topicService.getTopicById(id).get();
            topicService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
