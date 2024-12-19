package com.mls.Expense_Tracker_API.expense;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ExpenseDTO {

    private Long id;
    private String name;
    private Float value;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm" , timezone = "Europe/Madrid")
    private Date createdAt;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private Expense.ExpenseType expenseType;

    public enum ExpenseType {
        GROCERIES,
        ELECTRONICS,
        CLOTHING,
        HEALTH,
        LEISURE
    }

}
