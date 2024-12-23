package com.mls.Expense_Tracker_API.expense;

import com.mls.Expense_Tracker_API.user.User;
import com.mls.Expense_Tracker_API.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserService userService;

    public List<Expense> listAllExpenses() {
        return expenseRepository.findAll();
    }

    public List<Expense> listAllByName(Long userId,String name) {
        return expenseRepository.findAllByUserIdAndNameContaining(userId, name);
    }

    public Expense saveExpense(ExpenseDTO expenseDTO) {
        User user = userService.findById(expenseDTO.getUserId()).orElse(null);
        Expense expense = Expense.builder()
                .name(expenseDTO.getName())
                .value(expenseDTO.getValue())
                .expenseType(expenseDTO.getExpenseType())
                .user(user)
                .build();
        return expenseRepository.save(expense);
    }

    public Expense updateExpense(ExpenseDTO expenseDTO) {
        User user = userService.findById(expenseDTO.getUserId()).orElse(null);
        Expense expense = Expense.builder()
                .id(expenseDTO.getId())
                .name(expenseDTO.getName())
                .value(expenseDTO.getValue())
                .createdAt(expenseDTO.getCreatedAt())
                .expenseType(expenseDTO.getExpenseType())
                .user(user)
                .build();
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
