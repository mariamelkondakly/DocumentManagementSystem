package com.AtosReady.UserManagementSystem.Aspects;

import com.AtosReady.UserManagementSystem.DTO.UserDTO;
import com.AtosReady.UserManagementSystem.Exceptions.WeakPasswordException;
import com.AtosReady.UserManagementSystem.Exceptions.UniquenessViolationException;
import com.AtosReady.UserManagementSystem.Models.User;
import com.AtosReady.UserManagementSystem.Repositories.UserRepo;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RegistrationAspect {

    @Autowired
    private UserRepo repo;

    @Pointcut("execution(* com.AtosReady.UserManagementSystem.Services.UserService.register(..)) && args(user)")
    public void registerUserPointcut(User user) {
    }

    @Before("registerUserPointcut(user)")
    public void validateUserBeforeRegistration(User user) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        if (!user.getPassword().matches(passwordPattern)) {
            throw new WeakPasswordException("Password must be at least 8 characters long and include uppercase, lowercase, digit, and special character.");
        }

        User userCheck = repo.findByEmail(user.getEmail());
        if (userCheck != null) {
            throw new UniquenessViolationException("an email");
        }

        User userCheck2 = repo.findByNid(user.getNid());
        if (userCheck2 != null) {
            throw new UniquenessViolationException("a national id");
        }
    }
}


