package com.AtosReady.UserManagementSystem.Controllers;

import com.AtosReady.UserManagementSystem.DTO.LoginRequest;
import com.AtosReady.UserManagementSystem.DTO.UserDTO;
import com.AtosReady.UserManagementSystem.Mappers.UserMapper;
import com.AtosReady.UserManagementSystem.Models.User;
import com.AtosReady.UserManagementSystem.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<HashMap<String, Object>> register(@RequestBody User user)
    {
        System.out.println(user);
        return userService.register(user);
    }

    @PostMapping("/login")
    public List<Object> login(@Valid @RequestBody  LoginRequest user)
    {
        return userService.verify(user);
    }



    @GetMapping("/Users")
    public ArrayList<UserDTO> getUsers() {
        return userService.geAllUsers();
    }

}
