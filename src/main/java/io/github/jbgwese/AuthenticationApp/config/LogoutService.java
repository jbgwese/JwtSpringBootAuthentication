package io.github.jbgwese.AuthenticationApp.config;

import io.github.jbgwese.AuthenticationApp.auth.AuthenticationService;
import io.github.jbgwese.AuthenticationApp.token.TokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader== null|| !authHeader.startsWith("Bearer ")){
            return;
        }
        final String token =authHeader.substring(7);
       var storedToken = tokenRepository.findByToken(token).orElseThrow();
       if(storedToken!=null){
           storedToken.setRevoked(true);
           storedToken.setExpired(true);
           tokenRepository.save(storedToken);
       }
    }
}
