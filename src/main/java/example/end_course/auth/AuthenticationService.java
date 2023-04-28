package example.end_course.auth;


import example.end_course.config.JwtService;
import example.end_course.model.Account;
import example.end_course.model.Role;
import example.end_course.repository.AccountRepository;
import example.end_course.repository.RoleRepository;
import example.end_course.repository.TokenRepository;
import example.end_course.token.Token;
import example.end_course.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository accountRepository;

    private final TokenRepository tokenRepository;

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
//    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request,Role role) {

        var account = Account.builder()
                .nickName(request.getNickName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();


        var savedAccount = accountRepository.save(account);
        var jwtToken = jwtService.generateToken(account);
        saveAccountToken(savedAccount, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveAccountToken(Account account, String jwtToken) {
        var token = Token.builder()
                .account(account)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
