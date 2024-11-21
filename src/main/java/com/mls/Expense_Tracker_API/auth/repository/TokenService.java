package com.mls.Expense_Tracker_API.auth.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;


    public void saveToken(Token token) {
        tokenRepository.save(token);
    }
}
