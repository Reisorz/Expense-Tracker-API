package com.mls.Expense_Tracker_API.user;

import com.mls.Expense_Tracker_API.auth.controller.RegisterRequest;
import com.mls.Expense_Tracker_API.auth.controller.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/find-user-by-id/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        User user = userService.findById(id).orElse(null);
        return ResponseEntity.ok(user);
    }
}
