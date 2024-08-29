package com.AtosReady.DocumentManagementSystem.Services;

import com.AtosReady.DocumentManagementSystem.Creators.DocumentCreator;
import com.AtosReady.DocumentManagementSystem.DTO.DocumentMoveRequest;
import com.AtosReady.DocumentManagementSystem.Exceptions.EmptyFileException;
import com.AtosReady.DocumentManagementSystem.Exceptions.InvalidSignatureException;
import com.AtosReady.DocumentManagementSystem.Exceptions.ResourceExistsException;
import com.AtosReady.DocumentManagementSystem.Exceptions.ResourceNotFoundException;
import com.AtosReady.DocumentManagementSystem.Models.Directories;
import com.AtosReady.DocumentManagementSystem.Models.Documents;
import com.AtosReady.DocumentManagementSystem.Repositories.DocumentRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepo repo;

    @Autowired
    private DirectoriesService directoriesService;

    @Autowired
    private DocumentCreator creator;

    public ResponseEntity<String> createDocument(ObjectId directoryId, MultipartFile file) throws IOException {

        HashMap<String, Object> responseMap = new HashMap<>();
        if (file.isEmpty()) {
            throw new EmptyFileException("No file sent, file cannot be empty, exception was raised in the createDocument" +
                    "method of the Document service.");
        }
        boolean willReplace = false;

        Directories directory =
                directoriesService.repo.findByIdAndUserIdAndDeletedFalse(directoryId, directoriesService.getUserId()).
                        orElseThrow(() -> new ResourceNotFoundException("Directory was not found, exception was thrown in" +
                                "while trying to extract the parent directory in the createDocument method of the " +
                                "DocumentService class"));

        String sanitizedFileName = Objects.requireNonNull(file.getOriginalFilename()).replace(".", "_");


        if (directory.getDocuments().containsKey(sanitizedFileName)) {
            Documents existingDocument = directory.getDocuments().get(sanitizedFileName);
            if (!existingDocument.isDeleted()) {
                throw new ResourceExistsException("a document with the same name already exists in " + directoryId +
                        "exception was thrown in the createDocument method of the DocumentService class");
            }
            willReplace = true;
        }
        Documents newDocument = new Documents();
        newDocument.setName(sanitizedFileName);
        newDocument.setPath(directory.getPath() + "\\" + newDocument.getName());
        newDocument.setSize(file.getSize());
        if (willReplace) {
            directory.getDocuments().replace(newDocument.getName(), newDocument);
        } else {
            directory.getDocuments().put(newDocument.getName(), newDocument);
        }
        directoriesService.repo.save(directory);
        return creator.CreateNewFile(file, directory.getPath(), willReplace);
    }

    public ResponseEntity<String> renameDocument(ObjectId parentId, String oldName, String newName) throws IOException {
        if (newName.isEmpty()) {
            throw new InvalidSignatureException("Enter a new name for your file");
        }
        if (oldName.equals(newName)) {
            ResponseEntity.ok("No visible update happened, you entered the same name.");
        }
        Directories parentDirectory = directoriesService.repo.findByIdAndUserIdAndDeletedFalse
                        (parentId, directoriesService.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("couldn't find file's parent directory, " +
                        "exception was raised in the renameDocument method in the DocumentService class"));

        String sanitizedOldName = Objects.requireNonNull(oldName).replace(".", "_");
        String sanitizedNewName = Objects.requireNonNull(newName).replace(".", "_");
        boolean deletedPresent = false;
        if (parentDirectory.getDocuments().containsKey(sanitizedOldName)) {

            if (parentDirectory.getDocuments().containsKey(sanitizedNewName)) {
                Documents foundDocument = parentDirectory.getDocuments().get(sanitizedNewName);
                if (foundDocument.isDeleted()) {
                    parentDirectory.getDocuments().remove(sanitizedNewName);
                    deletedPresent = true;
                }
                throw new ResourceExistsException("A file with this new name already exists in the directory.");
            }

            Documents documents = parentDirectory.getDocuments().get(sanitizedOldName);
            if(documents.isDeleted()){
                throw new ResourceNotFoundException("file was not found in the directory. Exception was raised in the " +
                        "renameDocument method in the DocumentService class");
            }

            if (getFileExtension(sanitizedOldName).equals(getFileExtension(sanitizedNewName))) {
                documents.setName(sanitizedNewName);
                documents.setPath(parentDirectory.getPath() + "\\" + sanitizedNewName);
                parentDirectory.getDocuments().remove(sanitizedOldName);
                parentDirectory.getDocuments().put(sanitizedNewName, documents);

                directoriesService.repo.save(parentDirectory);

            } else {
                throw new InvalidSignatureException("Extension cannot be changed for the same file");
            }
        } else {
            throw new ResourceNotFoundException("file was not found in the directory. Exception was raised in the " +
                    "renameDocument method in the DocumentService class");
        }

        String oldPath = parentDirectory.getPath() + "\\" + oldName;
        String newPath = parentDirectory.getPath() + "\\" + newName;

        return creator.renameFile(deletedPresent, oldPath, newPath);
    }

    private String getFileExtension(String fileName) {
        int Index = fileName.lastIndexOf('_'); //the index right before the extension
        if (Index == -1)
            return "";
        else
            return fileName.substring(Index + 1);
    }

    public ResponseEntity<String> moveDocument(DocumentMoveRequest documentMoveRequest) throws IOException {

        Directories oldParentDirectory = directoriesService
                .repo.findByIdAndUserIdAndDeletedFalse(documentMoveRequest.getOldParentId(), directoriesService.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("The directory where the file supposedly is cannot be found," +
                        " exception was thrown in the moveDocument method in the DocumentService class"));

        Directories newParentDirectory = directoriesService
                .repo.findByIdAndUserIdAndDeletedFalse(documentMoveRequest.getNewParentId(), directoriesService.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("The directory where the file should be moved cannot be found," +
                        " exception was thrown in the moveDocument method in the DocumentService class"));

        String sanitizedName = Objects.requireNonNull(documentMoveRequest.getFileName()).replace(".", "_");

        if (!oldParentDirectory.getDocuments().containsKey(sanitizedName)) {
            throw new ResourceNotFoundException("Couldn't find the file to be moved" +
                    " exception was thrown in the moveDocument method in the DocumentService class");
        }

        boolean deletedPresent=false;

        if(newParentDirectory.getDocuments().containsKey(sanitizedName)){
            Documents deletedDocument=newParentDirectory.getDocuments().get(sanitizedName);
            if(deletedDocument.isDeleted()){
                deletedPresent=true;
                newParentDirectory.getDocuments().remove(deletedDocument.getName());
            }
            else{
                throw new ResourceExistsException("A file with the same name already exists in the destination folder" +
                        " exception was thrown in the moveDocument method in the DocumentService class");
            }
        }
        Documents document=oldParentDirectory.getDocuments().get(sanitizedName);
        if(document.isDeleted()){
            throw new ResourceNotFoundException("Couldn't find the file to be moved" +
                    " exception was thrown in the moveDocument method in the DocumentService class");
        }
        document.setPath(newParentDirectory.getPath()+"\\"+document.getName());
        oldParentDirectory.getDocuments().remove(document.getName());
        newParentDirectory.getDocuments().put(document.getName(), document);

        return creator.moveFile(deletedPresent,oldParentDirectory.getPath()+"\\"+documentMoveRequest.getFileName(),
                newParentDirectory.getPath()+"\\"+documentMoveRequest.getFileName());

    }

    public ResponseEntity<String> deleteDocument(ObjectId parentId, String name) throws IOException {
        Directories parentDirectory=
                directoriesService.repo.findByIdAndUserIdAndDeletedFalse(parentId, directoriesService.getUserId())
                        .orElseThrow(()->new ResourceNotFoundException("Parent directory not found" +
                                "exception was thrown in the deleteDocument method in the DocumentService class"));

        String sanitizedName = Objects.requireNonNull(name).replace(".", "_");

        if (!parentDirectory.getDocuments().containsKey(sanitizedName)){
            throw new ResourceNotFoundException("document not found" +
                    "exception was thrown in the deleteDocument method in the DocumentService class");
        }
        Documents document=parentDirectory.getDocuments().get(sanitizedName);
        if(document.isDeleted()){
            throw new ResourceNotFoundException("document not found" +
                    "exception was thrown in the deleteDocument method in the DocumentService class");
        }
        document.setDeleted(true);
        return creator.deleteDocument(parentDirectory.getPath()+"\\"+name);
    }
}
