package com.mls.Expense_Tracker_API.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    public List<Expense> findByUserId(Long id);

    public List<Expense> findAllByCreatedAtBetweenAndUserId(Date creationTimeStart, Date creationTimeEnd, Long userId);

    public List<Expense> findAllByUserIdAndNameContaining(Long userId, String name);

}
