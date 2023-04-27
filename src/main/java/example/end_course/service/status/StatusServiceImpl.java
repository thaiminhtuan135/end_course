package example.end_course.service.status;

import example.end_course.model.Status;
import example.end_course.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusServiceImpl implements StatusService{
    @Autowired
    private StatusRepository statusRepository;

    @Override
    public Status save(Status status) {
        return statusRepository.save(status);
    }

    @Override
    public Optional<Status> getStatusById(int id) {
        return statusRepository.findById(id);
    }

    @Override
    public void delete(int id) {
        statusRepository.deleteById(id);
    }

    @Override
    public List<Status> getListStatus() {
        return statusRepository.findAll();
    }
}
