package com.mls.Expense_Tracker_API.auth.config;

import com.mls.Expense_Tracker_API.auth.repository.TokenService;
import com.mls.Expense_Tracker_API.auth.service.JwtService;
import com.mls.Expense_Tracker_API.user.User;
import com.mls.Expense_Tracker_API.user.UserService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;


    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {

        if(request.getServletPath().contains("/auth")) {
            filterChain.doFilter(request,response);
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(userEmail == null || authentication != null) {
            filterChain.doFilter(request,response);
            return;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        final boolean isTokenNotExpiredOrRevoked = tokenService.findByToken(jwt)
                .map(token -> !token.getIsExpired() && !token.getIsRevoked()).orElse(false);

        if (isTokenNotExpiredOrRevoked) {
            final Optional<User> user = userService.findByEmail(userEmail);

            if(user.isPresent()) {
                final boolean isTokenValid = jwtService.isTokenValid(jwt ,user.get());

                if(isTokenValid) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
