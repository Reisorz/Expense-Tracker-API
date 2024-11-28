package com.mls.Expense_Tracker_API.auth.service;

import com.mls.Expense_Tracker_API.auth.controller.AuthRequest;
import com.mls.Expense_Tracker_API.auth.controller.RegisterRequest;
import com.mls.Expense_Tracker_API.auth.controller.TokenResponse;
import com.mls.Expense_Tracker_API.auth.repository.Token;
import com.mls.Expense_Tracker_API.auth.repository.TokenService;
import com.mls.Expense_Tracker_API.user.User;
import com.mls.Expense_Tracker_API.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public TokenResponse register (RegisterRequest request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword()) //Encode password later
                .build();

        User savedUser = userService.saveUser(user);
        String jwtToken = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);
        savUserToken(savedUser,jwtToken);
        return new TokenResponse(jwtToken,refreshToken);
    }

    public TokenResponse authenticate (AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userService.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        savUserToken(user,jwtToken);
        return new TokenResponse(jwtToken,refreshToken);
    }

    private void savUserToken(User user, String jwtToken) {
        final Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(Token.TokenType.BEARER)
                .isRevoked(false)
                .isExpired(false)
                .build();
        tokenService.saveToken(token);
    }

    private void revokeAllUserTokens(User user) {
        final List<Token> validUserTokens = tokenService.findAllValidTokenByUserId(user.getId());
        if(!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setIsRevoked(true);
                token.setIsExpired(true);
            });
            tokenService.saveAllTokens(validUserTokens);
        }
    }

    public TokenResponse refreshToken(final String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Bearer token");
        }

        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail == null) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }

        final User user = userService.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(userEmail));

        if(!jwtService.isTokenValid(refreshToken,user)) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }

        final String accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        savUserToken(user, accessToken);
        return new TokenResponse(accessToken,refreshToken);

    }

}

