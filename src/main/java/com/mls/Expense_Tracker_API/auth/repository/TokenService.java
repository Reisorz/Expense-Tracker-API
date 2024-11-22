package com.mls.Expense_Tracker_API.auth.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;


    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    public List<Token> findAllValidTokenByUser(Long id) {
        return tokenRepository.findAllValidTokenByUser(id);
    }

    public void saveAllTokens(List<Token> tokenList) {
        tokenRepository.saveAll(tokenList);
    }
}
