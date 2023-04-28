package example.end_course.auth;


import example.end_course.Enum.RoleCustom;
import example.end_course.config.JwtService;
import example.end_course.model.Account;
import example.end_course.model.Role;
import example.end_course.repository.AccountRepository;
import example.end_course.repository.RoleRepository;
import example.end_course.repository.TokenRepository;
import example.end_course.token.Token;
import example.end_course.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository accountRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request, Role role) {

        RoleCustom roleAccount = RoleCustom.ADMIN;
        if(role.getId() == 2) {
            roleAccount = RoleCustom.USER;
        }

        var account = Account.builder()
                .nickName(request.getNickName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).roleCustom(roleAccount)
                .role(role)
                .build();


        var savedAccount = accountRepository.save(account);
        var jwtToken = jwtService.generateToken(account);
        saveAccountToken(savedAccount, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
        var account = accountRepository
                .findByEmail(request.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var jwtToken = jwtService.generateToken(account);
        revokeAllAccountTokens(account);
        saveAccountToken(account, jwtToken);
        return AuthenticationResponse.builder().token(jwtToken).build();
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

    private void revokeAllAccountTokens(Account account) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(account.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
