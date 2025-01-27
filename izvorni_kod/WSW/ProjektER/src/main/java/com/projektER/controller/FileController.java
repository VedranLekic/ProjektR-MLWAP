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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.projektER.service.CSVmanipulator;
import com.projektER.service.ExcelManipulator;
import com.projektER.service.JsonManipulator;
import com.projektER.service.TwistedTransistor;

@RestController
@RequestMapping("/api")
public class FileController {

    private File processedFile;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> handleFileUpload(@RequestPart("file") MultipartFile file, @RequestParam("userNumber") int number) throws IOException {
      
        Path tempFilePath = Paths.get(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());
        file.transferTo(tempFilePath.toFile());
        
      
        TwistedTransistor tw = new TwistedTransistor();
        CSVmanipulator csvm= new CSVmanipulator();
        ExcelManipulator em= new ExcelManipulator();
        JsonManipulator jm= new JsonManipulator();
        
        
        if(tempFilePath.toString().endsWith("txt")){
        processedFile = tw.transfer(number, 11, tempFilePath.toString());
        
        }
        else if(tempFilePath.toString().endsWith("csv")) {
        	
        	processedFile = csvm.transfer(number, 11, tempFilePath.toString());
        }
        else if(tempFilePath.toString().contains("xlsx")) {
        	
        	processedFile = em.transfer(number, 11, tempFilePath.toString());
        }
         else if(tempFilePath.toString().contains("json")) {
        	
        	processedFile = jm.transfer(number, 11, tempFilePath.toString());
        }
        else{
        	System.out.println("Proslo"+tempFilePath.toString());
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "File processed successfully");
        response.put("fileName", processedFile.getName()); 
        return ResponseEntity.ok(response); // Returns JSON
    }
    
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) {
        try {
           
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
