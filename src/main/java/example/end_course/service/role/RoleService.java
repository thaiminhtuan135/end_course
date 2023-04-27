package example.end_course.service.role;

import example.end_course.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role save(Role role);

    Optional<Role> getRoleById(int id);

    void delete(int id);

    List<Role> getRoles();
}
