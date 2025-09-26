package com.ifsc.tarefas.auth;

import java.io.IOException;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends OncePerRequestFilter 
{
    private final AuthRepository authRepository;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final JwtUtil jwtUtil;

    public AuthFilter(AuthRepository authRepository, JwtUtil jwtUtil) 
    {
        this.authRepository = authRepository;
        this.jwtUtil = jwtUtil;
    }

    private static final Set<String> PUBLIC_PATTERNS = Set.of
    (
        "/login", 
        "/login/**", 
        "/register", 
        "/register/**",
        "/css/**",
        "/js/**",
        "/images/**",
        "/webjars/**",
        "/h2-console/**",
        "/"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException 
    {
        String path = request.getServletPath();
        for(String url: PUBLIC_PATTERNS) 
        {
            if(antPathMatcher.match(url, path)) 
            {
                return true;
            }
        }

        if("OPTIONS".equalsIgnoreCase(request.getMethod())) 
        {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException 
    {
        String token = extractTokenFromCookie(request, "AUTH_TOKEN");

        if(token == null || token.isBlank()) 
        {
            String authHeader = request.getHeader("Authorization");
            if(authHeader != null && authHeader.startsWith("Bearer ")) 
            {
                token = authHeader.substring(7);
            }
        }
        var user = authRepository.validate(token);

        if(user.isEmpty())
        {
            String accept = request.getHeader("Accept");
            if(accept != null && accept.contains("text/html")) 
            {
                String redirectTo = request.getRequestURI();
                response.sendRedirect("/login?redirect=" + redirectTo);
                return; 
            }
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        String username = user.get();
        request.setAttribute("AUTH_USER", username);
        try 
        {
            String role = jwtUtil.getRole(token);
            request.setAttribute("AUTH_ROLE", role != null ? role : authRepository.getRoleByUsername(username));
        } catch (Exception e) 
        {
            request.setAttribute("AUTH_ROLE",  authRepository.getRoleByUsername(username));
        }
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookie(HttpServletRequest request, String cookieAutentificacao) 
    {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) return null;

        for(Cookie cookie : cookies) 
        {
            if(cookieAutentificacao.equals(cookie.getName())) 
            {
                return cookie.getValue();
            }
        }
        return null;
    }
}
