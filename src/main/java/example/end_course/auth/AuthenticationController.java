package example.end_course.auth;

import example.end_course.model.Role;
import example.end_course.service.account.AccountService;
import example.end_course.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/register/role/{id}")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request, @PathVariable Integer id) {

        try {
            Role role = roleService.getRoleById(id).get();
            if (accountService.existAccount(request.getEmail())) {
                return new ResponseEntity<>("Account Taken", HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok(authenticationService.register(request,role));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Role not found",HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
