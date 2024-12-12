package com.mls.Expense_Tracker_API.expense;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<Expense> listAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public void deleteExpense(Expense expense) {
        expenseRepository.delete(expense);
    }

    public Expense findExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    public List<Expense> findExpenseByUserId(Long id) {
        return expenseRepository.findByUserId(id);
    }

    public List<Expense> findAllByCreatedAtBetweenAndUserId (Date creationTimeStart, Date creationTimeEnd, Long userId) {
        return expenseRepository.findAllByCreatedAtBetweenAndUserId(creationTimeStart,creationTimeEnd, userId);
    }


}