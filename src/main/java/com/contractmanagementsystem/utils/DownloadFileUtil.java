package com.contractmanagementsystem.utils;

import com.contractmanagementsystem.exception.ContractException;
import com.contractmanagementsystem.model.Contract;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class DownloadFileUtil {
    public ResponseEntity<Resource> downloadContract(Contract contract) throws MalformedURLException {

        Path path = Paths.get(contract.getContractPath());

        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new ContractException("File not found");
        }

        String fileName = resource.getFilename();
        MediaType mediaType;

        if (fileName != null && fileName.endsWith(".pdf")) {
            mediaType = MediaType.APPLICATION_PDF;
        }
        else if (fileName != null && fileName.endsWith(".docx")) {
            mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        }
        else {
            throw new ContractException("Only PDF and DOCX files are allowed");
        }

        return ResponseEntity.ok().contentType(mediaType).
                header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").
                body(resource);
    }
}
