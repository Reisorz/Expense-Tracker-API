package com.mls.Expense_Tracker_API.expense;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/expense")
@CrossOrigin(value = "http://localhost:4200")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/list-all-expenses")
    public List<Expense> listAllExpenses() {
        return expenseService.listAllExpenses();
    }

    @GetMapping("/list-user-expenses-by-name/{id}-{name}")
    public List<Expense> listUserExpensesByName(@PathVariable Long id,@PathVariable String name) {
        return expenseService.listAllByName(id,name);
    }

    @GetMapping("/list-user-expenses/{userId}")
    public List<Expense> listUserExpenses(@PathVariable Long userId) {
        return expenseService.findExpenseByUserId(userId);
    }

    @GetMapping("/list-user-expenses-date-range-filter/{userId}")
    public List<Expense> listUserExpensesInDateRange (@RequestParam("creationTimeStart") String creationTimeStart,
                                                      @RequestParam("creationTimeEnd") String creationTimeEnd,
                                                      @PathVariable Long userId) throws Exception {


        //Transform into a correct Date format
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date startDate = sdf.parse(creationTimeStart);
        Date endDate = sdf.parse(creationTimeEnd);

        return expenseService.findAllByCreatedAtBetweenAndUserId(startDate, endDate, userId);
    }

    @PostMapping("/add-expense")
    public ResponseEntity<Expense> addExpense(@RequestBody ExpenseDTO expenseDTO) {
        Expense addedExpense = expenseService.saveExpense(expenseDTO);
        return ResponseEntity.ok(addedExpense);
    }

    @PutMapping("/update-expense")
    public ResponseEntity<Expense> updateExpense(@RequestBody ExpenseDTO requestedExpense) {
            Expense updatedExpense = expenseService.updateExpense(requestedExpense);
            return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/delete-expense/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteExpense(@PathVariable Long id) {
        Expense expense = expenseService.findExpenseById(id);
        if(expense == null) {
            throw new RuntimeException("Expense with id " + id + " not found.");
        }
        expenseService.deleteExpense(expense);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-expense-by-id/{id}")
    public ResponseEntity<Expense> findExpenseById(@PathVariable Long id) {
        Expense expense = expenseService.findExpenseById(id);
        return ResponseEntity.ok(expense);
    }
}
