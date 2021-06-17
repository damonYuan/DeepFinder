package com.damonyuan.deepfinder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BoundedInputStream;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipHandler implements IHandler {
    @Override
    public void handle(Path file, String[] stringsToFind) {
        for (String s : stringsToFind) {
            try {
                FileInputStream inputStream = new FileInputStream(file.toAbsolutePath().toString());
                FileInputStream sentinelInputStream = new FileInputStream(file.toAbsolutePath().toString());
                char first = (char) sentinelInputStream.read();
                char second = (char) sentinelInputStream.read();
                if (first == 'P' && second == 'K') {
                    sentinelInputStream.close();
                } else if (first == '#' && second == '!') {
                    int bytesToSkip = bytesToSkip(sentinelInputStream, 2);
                    sentinelInputStream.close();
                    while (bytesToSkip-- > 0) {
                        inputStream.read();
                    }
                } else {
                    sentinelInputStream.close();
                    inputStream.close();
                    System.out.printf("Unknown type: first char is %c and the second char is %c", first, second);
                    return;
                }
                zipFind(file.toAbsolutePath().toString(), s, new ZipInputStream(inputStream));
                inputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void zipFind(String path, String s, ZipInputStream zi) throws IOException {
        ZipEntry zipEntry = null;
        while ((zipEntry = zi.getNextEntry()) != null) {
            BoundedInputStream boundedInputStream = new BoundedInputStream(zi, zipEntry.getSize());
            if (zipEntry.getName().endsWith(".jar") || zipEntry.getName().endsWith(".war") || zipEntry.getName().endsWith(".zip") || zipEntry.getName().endsWith(".ear")) {
                byte[] byteArray = IOUtils.toByteArray(boundedInputStream);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                zipFind(path + "$" + zipEntry.getName(), s, new ZipInputStream(byteArrayInputStream));
                byteArrayInputStream.close();
            } else {
                if (find(s, boundedInputStream)) {
                    System.out.printf("%s contains %s%n", path + "$" + zipEntry.getName(), s);
                    return;
                }
            }
        }
    }

    private int bytesToSkip(InputStream inputStream, int offset) throws IOException {
        char[] needle = "PK\003\004".toCharArray();
        int c;
        int i = 0;
        int bytesToSkip = 0;
        while ((c = inputStream.read()) != -1) {
            bytesToSkip++;
            if ((char) c == needle[i]) {
                if (++i == needle.length) {
                    return bytesToSkip - 4 + offset;
                }
            } else {
                i = 0;
            }
        }
        return 0;
    }
}
