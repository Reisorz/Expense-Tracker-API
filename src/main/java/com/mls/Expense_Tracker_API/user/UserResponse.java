package com.mls.Expense_Tracker_API.user;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserResponse {

    private String name;
    private String email;
}
