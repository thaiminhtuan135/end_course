package example.end_course.controller;

import example.end_course.model.Status;
import example.end_course.service.status.StatusService;
import example.end_course.util.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/admin/status")
public class StatusCodeController {
    @Autowired
    private StatusService statusService;

    private final com.google.gson.Gson gson = Gson.gson();

    @GetMapping("/list")
    public List<Status> getStudents() {
        return statusService.getListStatus();
    }

    @PostMapping("/create")
    private ResponseEntity<Status> create(@RequestBody String status) {
        try {
            Status status1 = gson.fromJson(status, Status.class);
            return new ResponseEntity<>(statusService.save(status1), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Status> update(@RequestBody String dataUpdate, @PathVariable Integer id) {
        return statusService.getStatusById(id).map(status -> {
            Status status1 = gson.fromJson(dataUpdate, Status.class);
            status1.setId(status.getId());
            return new ResponseEntity<>(statusService.save(status1), HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getbyId(@PathVariable Integer id) {
        try {
            Status status = statusService.getStatusById(id).get();
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Status not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            Status status = statusService.getStatusById(id).get();
            statusService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Status not found", HttpStatus.NOT_FOUND);
        }
    }
}
