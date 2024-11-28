package com.mls.Expense_Tracker_API.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {


    @Query(value = """
            SELECT t FROM Token t
            INNER JOIN t.user u
            WHERE u.id = :id AND (t.isExpired = false OR t.isRevoked = false)
            """)
    List<Token> findAllValidTokenByUserId(Long id);

    Optional<Token> findByToken (String token);
}
