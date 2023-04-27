package example.end_course.service.topic;

import example.end_course.model.Topic;
import example.end_course.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicServiceImpl implements TopicService{
    @Autowired
    private TopicRepository topicRepository;
    @Override
    public Topic save(Topic topic) {
        return topicRepository.save(topic);
    }

    @Override
    public Optional<Topic> getTopicById(int id) {
        return topicRepository.findById(id);
    }

    @Override
    public void delete(int id) {
        topicRepository.deleteById(id);
    }

    @Override
    public List<Topic> getTopics() {
        return topicRepository.findAll();
    }
}
