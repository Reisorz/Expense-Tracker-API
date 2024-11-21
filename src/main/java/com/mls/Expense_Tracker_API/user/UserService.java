package com.mls.Expense_Tracker_API.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public  User saveUser(User user) {
        return userRepository.save(user);
    }
}
