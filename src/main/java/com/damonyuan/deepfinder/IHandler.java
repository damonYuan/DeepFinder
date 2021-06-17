package com.damonyuan.deepfinder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface IHandler {
    void handle(Path file, String[] stringsToFind);

    default boolean find(String s, InputStream stream) {
        try {
            char[] needle = s.toCharArray();
            int c;
            int i = 0;
            while ((c = stream.read()) != -1) {
                if ((char) c == needle[i]) {
                    if (++i == needle.length) {
                        return true;
                    }
                } else {
                    i = 0;
                }
            }
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
