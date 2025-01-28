package com.projektER.service;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.springframework.stereotype.Service;

@Service
public class RowTransistor {

    public RowTransistor() {
        super();
    }

    private static File writeArray(File tmpF, String[] lines) throws IOException {
        FileWriter fw = new FileWriter(tmpF);
        for (String line : lines) {
            fw.write(line + "\n");
            fw.flush();
        }
        fw.close();
        return tmpF;
    }

    private static File unscrambleColumns(int colIndInput, int colIndOutput, String[] lines) throws IOException {
        for (int i = 0; i < lines.length; i++) {
            String[] columns = lines[i].split(" "); // Use appropriate delimiter
            if (colIndInput < columns.length && colIndOutput < columns.length) {
                String temp = columns[colIndInput];
                columns[colIndInput] = columns[colIndOutput];
                columns[colIndOutput] = temp;
                lines[i] = String.join(" ", columns); // Rejoin with the same delimiter
            }
        }
        Path outputPath = Files.createTempFile("myapp-", ".txt");
        return writeArray(outputPath.toFile(), lines);
    }

    public File transfer(int indUlaz, int indIzlaz, String filePath) throws IOException {
        Scanner sc = new Scanner(Path.of(filePath));
        StringBuilder content = new StringBuilder();
        while (sc.hasNext()) {
            content.append(sc.nextLine()).append("\n");
        }
        String[] lines = content.toString().split("\n");
        return unscrambleColumns(indUlaz, indIzlaz, lines);
    }
}