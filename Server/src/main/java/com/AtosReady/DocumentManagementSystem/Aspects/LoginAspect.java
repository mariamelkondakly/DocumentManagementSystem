package com.AtosReady.DocumentManagementSystem.Aspects;

import com.AtosReady.DocumentManagementSystem.DTO.LoginRequest;
import com.AtosReady.DocumentManagementSystem.Exceptions.ShortPasswordException;
import com.AtosReady.DocumentManagementSystem.Exceptions.UniquenessViolationException;
import com.AtosReady.DocumentManagementSystem.Models.User;
import com.AtosReady.DocumentManagementSystem.Repositories.UserRepo;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoginAspect {
    @Autowired
    private UserRepo repo;
    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

    @Pointcut("execution(* com.AtosReady.DocumentManagementSystem.Services.UserService.verify(..)) && args(user)")
    public void LoginUserPointcut(LoginRequest user) {

    }
    @Before("LoginUserPointcut(user)")
    public void validateUserBeforeLogin(LoginRequest user) {
        User existingUser= repo.findByEmail(user.getEmail());
        if(existingUser==null){
            throw new UsernameNotFoundException("This user was not found.");
        }
        if (!encoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

    }
}
