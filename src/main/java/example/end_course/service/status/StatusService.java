package example.end_course.service.status;


import example.end_course.model.Status;

import java.util.List;
import java.util.Optional;

public interface StatusService {
    Status save(Status status);

    Optional<Status> getStatusById(int id);

    void delete(int id);

    List<Status> getListStatus();
}
