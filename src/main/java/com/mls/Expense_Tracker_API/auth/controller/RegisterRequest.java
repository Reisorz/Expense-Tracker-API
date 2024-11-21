package com.mls.Expense_Tracker_API.auth.controller;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
}
