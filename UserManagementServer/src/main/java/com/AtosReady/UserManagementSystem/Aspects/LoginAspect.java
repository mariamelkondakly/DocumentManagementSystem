package com.AtosReady.UserManagementSystem.Aspects;

import com.AtosReady.UserManagementSystem.DTO.LoginRequest;
import com.AtosReady.UserManagementSystem.Exceptions.BadCredentialsException;
import com.AtosReady.UserManagementSystem.Exceptions.UserNotFoundException;
import com.AtosReady.UserManagementSystem.Models.User;
import com.AtosReady.UserManagementSystem.Repositories.UserRepo;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoginAspect {
    @Autowired
    private UserRepo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Pointcut("execution(* com.AtosReady.DocumentManagementSystem.Services.UserService.verify(..)) && args(user)")
    public void LoginUserPointcut(LoginRequest user) {

    }
    @Before("LoginUserPointcut(user)")
    public void validateUserBeforeLogin(LoginRequest user) {
        User existingUser= repo.findByEmail(user.getEmail());
        if (existingUser==null){
            throw new UserNotFoundException("This user was not found.");
        }
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

    }
}
