package com.projektER.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.projektER.service.TwistedTransistor;

@RestController
@RequestMapping("/api")
public class FileController {

    private File processedFile; // Stores the processed file reference

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> handleFileUpload(@RequestPart("file") MultipartFile file) throws IOException {
        // Save the uploaded file to a temporary location
        Path tempFilePath = Paths.get(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());
        file.transferTo(tempFilePath.toFile());

        // Process the file
        TwistedTransistor tw = new TwistedTransistor();
        processedFile = tw.transfer(10, 11, tempFilePath.toString());

        Map<String, String> response = new HashMap<>();
        response.put("message", "File processed successfully");
        response.put("fileName", processedFile.getName()); // Include the filename
        return ResponseEntity.ok(response); // Returns JSON
    }
    
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            // Resolve the file path using the system's temp directory
            Path filePath = Paths.get(System.getProperty("java.io.tmpdir"), fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
