package com.AtosReady.DocumentManagementSystem.Aspects;

import com.AtosReady.DocumentManagementSystem.DTO.LoginRequest;
import com.AtosReady.DocumentManagementSystem.Exceptions.BadCredentialsException;
import com.AtosReady.DocumentManagementSystem.Exceptions.UserNotFoundException;
import com.AtosReady.DocumentManagementSystem.Models.User;
import com.AtosReady.DocumentManagementSystem.Repositories.UserRepo;
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
        user.setId(existingUser.getId());

    }
}
