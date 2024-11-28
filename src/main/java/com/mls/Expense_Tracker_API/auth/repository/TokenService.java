package com.mls.Expense_Tracker_API.auth.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;


    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    public List<Token> findAllValidTokenByUserId(Long id) {
        return tokenRepository.findAllValidTokenByUserId(id);
    }

    public void saveAllTokens(List<Token> tokenList) {
        tokenRepository.saveAll(tokenList);
    }

    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
}
