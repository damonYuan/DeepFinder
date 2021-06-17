package com.damonyuan.deepfinder;

import java.io.FileInputStream;
import java.nio.file.Path;

public class FileHandler implements IHandler {
    @Override
    public void handle(Path file, String[] stringsToFind) {
        for (String s : stringsToFind) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file.toAbsolutePath().toString());
                if (find(s, fileInputStream)) {
                    System.out.printf("%s contains %s%n", file.toAbsolutePath(), s);
                }
                fileInputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
