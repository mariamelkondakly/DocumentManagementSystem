package com.AtosReady.DocumentManagementSystem.Aspects;

import com.AtosReady.DocumentManagementSystem.Exceptions.ShortPasswordException;
import com.AtosReady.DocumentManagementSystem.Exceptions.UniquenessViolationException;
import com.AtosReady.DocumentManagementSystem.Models.User;
import com.AtosReady.DocumentManagementSystem.Repositories.UserRepo;
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

    @Pointcut("execution(* com.AtosReady.DocumentManagementSystem.Services.UserService.register(..)) && args(user)")
    public void registerUserPointcut(User user) {
    }

    @Before("registerUserPointcut(user)")
    public void validateUserBeforeRegistration(User user) {
        if (user.getPassword().length() < 8) {
            throw new ShortPasswordException();
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


