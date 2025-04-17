
package com.venyou.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        logger.debug("Processing request: {} {}", method, path);

        // Skip authentication for public endpoints
        if (isPublicEndpoint(path, method)) {
            logger.debug("Public endpoint, skipping JWT check: {} {}", method, path);
            chain.doFilter(request, response);
            return;
        }

        // Require JWT for protected endpoints
        String token = getJwtFromRequest(request);
        if (token == null) {
            logger.warn("JWT token missing for protected endpoint: {} {}", method, path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"JWT token is missing\"}");
            return;
        }

        try {
            String email = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractRole(token);
            logger.debug("JWT token found: email={}, role={}", email, role);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (jwtUtil.validateToken(token, userDetails)) {
                    // Convert authorities or use JWT role
                    List<SimpleGrantedAuthority> authorities = role != null
                            ? List.of(new SimpleGrantedAuthority("ROLE_" + role))
                            : userDetails.getAuthorities().stream()
                                    .map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
                                    .collect(Collectors.toList());
                    logger.debug("Authorities set: {}", authorities);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    logger.warn("Invalid JWT token for email: {}", email);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Invalid JWT token\"}");
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("JWT processing error: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"JWT processing error\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isPublicEndpoint(String path, String method) {
        boolean isPublic = (path.startsWith("/api/auth/") ||
                path.equals("/api/bookings/check-availability") && method.equals("POST") ||
                (method.equals("GET") && (path.equals("/api/halls") || path.matches("/api/halls/\\d+"))) ||
                (method.equals("GET") && (path.equals("/api/categories") || path.matches("/api/categories/.+"))) ||
                (method.equals("GET") && (path.equals("/api/brands") || path.matches("/api/brands/\\d+") || path.matches("/api/brands/name/.+"))));
        logger.debug("isPublicEndpoint: {} {} -> {}", method, path, isPublic);
        return isPublic;
    }
}
