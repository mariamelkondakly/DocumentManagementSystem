package com.AtosReady.UserManagementSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String email;
    private long nid;
    private String first_name;
    private String last_name;
    private String age;

    @Override
    public String toString() {
        return "UserDTO{" +
                "email='" + email + '\'' +
                ", nid=" + nid +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
