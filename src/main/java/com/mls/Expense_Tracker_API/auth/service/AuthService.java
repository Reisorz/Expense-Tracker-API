package com.mls.Expense_Tracker_API.auth.service;

import com.mls.Expense_Tracker_API.auth.controller.RegisterRequest;
import com.mls.Expense_Tracker_API.auth.controller.TokenResponse;
import com.mls.Expense_Tracker_API.auth.repository.Token;
import com.mls.Expense_Tracker_API.auth.repository.TokenService;
import com.mls.Expense_Tracker_API.user.User;
import com.mls.Expense_Tracker_API.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenService tokenService;


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

    public void savUserToken(User user, String jwtToken) {
        final Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(Token.TokenType.BEARER)
                .isRevoked(false)
                .isExpired(false)
                .build();
        tokenService.saveToken(token);
    }

}

