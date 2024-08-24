package com.AtosReady.DocumentManagementSystem.Controllers;

import com.AtosReady.DocumentManagementSystem.DTO.LoginRequest;
import com.AtosReady.DocumentManagementSystem.DTO.UserDTO;
import com.AtosReady.DocumentManagementSystem.Mappers.UserMapper;
import com.AtosReady.DocumentManagementSystem.Models.User;
import com.AtosReady.DocumentManagementSystem.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public UserDTO register( @Valid @RequestBody User user)
    {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody  LoginRequest user)
    {
        return userService.verify(user);
    }



    @GetMapping("/Users")
    public ArrayList<UserDTO> getUsers() {
        return userService.geAllUsers();
    }

}
