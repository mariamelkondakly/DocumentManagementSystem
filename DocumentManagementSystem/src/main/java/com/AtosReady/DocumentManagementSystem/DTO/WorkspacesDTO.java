package com.AtosReady.DocumentManagementSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkspacesDTO {
    private String name;

    @Override
    public String toString() {
        return "WorkspacesDTO{" +
                "name='" + name + '\'' +
                '}';
    }
}
