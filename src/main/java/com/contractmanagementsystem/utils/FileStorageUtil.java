package com.contractmanagementsystem.utils;

import com.contractmanagementsystem.exception.ContractException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileStorageUtil {

    private static final String
            UPLOAD_DIR = "uploads/";

    public String saveFile(
            MultipartFile file
    ) throws IOException {

        validateFile(file);

        createUploadDirectory();

        String fileName =
                UUID.randomUUID()
                        + "_"
                        + file
                        .getOriginalFilename();

        Path filePath =
                Paths.get(
                        UPLOAD_DIR,
                        fileName
                );

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption
                        .REPLACE_EXISTING
        );

        return filePath.toString();
    }

    private void validateFile(
            MultipartFile file
    ) {

        String contentType =
                file.getContentType();

        boolean isPdf =
                "application/pdf"
                        .equals(contentType);

        boolean isDocx =
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        .equals(contentType);

        if (!isPdf && !isDocx) {

            throw new ContractException(
                    "Only PDF and DOCX files are allowed"
            );
        }
    }

    private void createUploadDirectory() {

        File directory =
                new File(UPLOAD_DIR);

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void deleteFile(
            String filePath
    ) {

        if (filePath == null) {
            return;
        }

        File file =
                new File(filePath);

        if (file.exists()) {
            file.delete();
        }
    }
}
