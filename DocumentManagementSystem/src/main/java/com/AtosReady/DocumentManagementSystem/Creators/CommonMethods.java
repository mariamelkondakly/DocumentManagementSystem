package com.AtosReady.DocumentManagementSystem.Creators;

import com.AtosReady.DocumentManagementSystem.Exceptions.DirectoryDeletionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Component
public class CommonMethods {
    @Value("${app.base-folder}")
    protected String baseFolderPath;

    private boolean deleteDirectoryRecursively(File dir) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (!deleteDirectoryRecursively(file)) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    protected void hideRecursively(File file) throws IOException {
        if (file.isDirectory()) {
            // Hide the directory itself
            setHiddenAttribute(file);

            // Recursively hide all subdirectories and files
            for (File child : Objects.requireNonNull(file.listFiles())) {
                hideRecursively(child);
            }
        } else {
            // Hide the file
            setHiddenAttribute(file);
        }
    }

    protected void setHiddenAttribute(File file) throws IOException {

        String command = "attrib +h \"" + file.getAbsolutePath() + "\"";
        Runtime.getRuntime().exec(command);

    }

    public void permanentlyDeleteDirectory(boolean present, File dir) {
        if (present && dir.exists()) {
            boolean deleted = deleteDirectoryRecursively(dir);
            if (!deleted) {
                throw new DirectoryDeletionException("Failed to delete the existing directory: " + dir.getAbsolutePath());
            }
        }
    }
}
