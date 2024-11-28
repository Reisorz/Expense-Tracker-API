package com.mls.Expense_Tracker_API.user;

import com.mls.Expense_Tracker_API.auth.controller.RegisterRequest;
import com.mls.Expense_Tracker_API.auth.controller.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/find-all-users")
    public List<UserResponse> getUserList() {
        return userService.getUserList()
                .stream()
                .map(user -> new UserResponse(user.getName(), user.getEmail())).toList();
    }
}
