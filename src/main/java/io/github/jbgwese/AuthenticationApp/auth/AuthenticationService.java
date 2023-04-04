package io.github.jbgwese.AuthenticationApp.auth;

import io.github.jbgwese.AuthenticationApp.config.JwtService;
import io.github.jbgwese.AuthenticationApp.token.Token;
import io.github.jbgwese.AuthenticationApp.token.TokenRepository;
import io.github.jbgwese.AuthenticationApp.token.TokenType;
import io.github.jbgwese.AuthenticationApp.user.Role;
import io.github.jbgwese.AuthenticationApp.user.User;
import io.github.jbgwese.AuthenticationApp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationResponse register(RegistrationRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .role(Role.USER)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var savedUser = userRepository.save(user);
        var token = jwtService.generateToken(user);
        var userToken = Token
                .builder()
                .token(token)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(savedUser)
                .build();
        tokenRepository.save(userToken);

        return AuthenticationResponse
                .builder()
                .token(token)
                .build();

    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.
                findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("No such User"));

        revokeAllUserTokens(user);
        var token = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(token)
                .build();
    }


    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllTokenByUser(user.getId());
        validUserTokens.forEach(t -> {
                    t.setExpired(true);
                    t.setRevoked(true);
                }
        );
        tokenRepository.saveAll(validUserTokens);

    }


}



