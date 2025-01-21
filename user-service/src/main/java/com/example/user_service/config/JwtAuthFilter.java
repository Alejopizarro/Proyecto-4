package com.example.user_service.config;

import com.example.user_service.commons.entities.UserModel;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            Optional<Integer> userIdOptional = Optional.ofNullable(jwtService.extractUserId(jwtToken));
            userIdOptional.flatMap(userId -> userRepository.findById(Long.valueOf(userId)))
                    .ifPresent(userDetails -> {
                        request.setAttribute("X-User-Id", userDetails.getUserId());
                        if (!jwtService.isExpired(jwtToken)) {
                            processAuthentication(request, userDetails);
                        }
                    });
            filterChain.doFilter(request, response);
        }
    }

    private void processAuthentication(HttpServletRequest request, UserModel userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
