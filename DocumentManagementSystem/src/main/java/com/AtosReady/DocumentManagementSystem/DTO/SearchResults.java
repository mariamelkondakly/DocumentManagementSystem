package com.AtosReady.DocumentManagementSystem.DTO;

import com.AtosReady.DocumentManagementSystem.Models.Directories;
import com.AtosReady.DocumentManagementSystem.Models.Workspaces;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@Setter
@Getter
public class SearchResults {

    private Page<DirectoryDTO> directories;
    private Page<WorkspacesDTO> workspaces;

}
