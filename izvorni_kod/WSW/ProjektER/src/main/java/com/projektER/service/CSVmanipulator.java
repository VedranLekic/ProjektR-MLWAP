package com.projektER.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.springframework.stereotype.Service;

@Service
public class CSVmanipulator {
	 File outputFile = new File("src/main/resources/static/output.csv");
    public CSVmanipulator() {
        super();
    }

    private static File writeArray(File outputFile, String[] poNovomRedu) throws IOException {
        try (FileWriter fw = new FileWriter(outputFile)) { 
            for (String redPodatka : poNovomRedu) {
                fw.write(redPodatka + "\n");
                fw.flush();
            }
        } 
        return outputFile;
    }

   
    private static File unscramble(int inJeSad, int inMoraBiti, String[] poRedovima) throws IOException {
        
    	 
    	 Path izl= Files.createTempFile("myapp-", ".csv");
    	 File outputFile = izl.toFile();
        if (inJeSad != inMoraBiti) {
            String pom = poRedovima[inJeSad];
            poRedovima[inJeSad] = poRedovima[inMoraBiti];
            poRedovima[inMoraBiti] = pom;
        }

        return writeArray(outputFile, poRedovima);
    }


    public File transfer(int indUlaz, int indIzlaz, String s) throws IOException {
        Scanner sc = new Scanner(Path.of(s));
        StringBuilder mr = new StringBuilder(); 

        while (sc.hasNext()) {
            mr.append(sc.nextLine()).append("\n");
        }
        sc.close();

        String[] listed = mr.toString().split("\n");
        return unscramble(indUlaz, indIzlaz, listed);
    }
}