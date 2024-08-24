package com.AtosReady.DocumentManagementSystem.DTO;

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

}
