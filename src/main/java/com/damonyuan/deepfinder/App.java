package com.damonyuan.deepfinder;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.EnumSet;

public class App {

    public static void main(String[] args) {
        final String scanningPath = new File(args[0]).getAbsolutePath();
        final String[] stringsToFind = Arrays.copyOfRange(args, 1, args.length);
        System.out.printf("Scanning path is: %s%nStrings to find are: %s%n", scanningPath, Arrays.toString(stringsToFind));

        try {
            Files.walkFileTree(Paths.get(scanningPath), EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String extension = FilenameUtils.getExtension(file.getFileName().toString());
                    ZipHandler zipHandler = new ZipHandler();
                    FileHandler fileHandler = new FileHandler();
                    if (extension.equals("jar") || extension.equals("war") || extension.equals("zip") || extension.equals("ear")) {
                        zipHandler.handle(file, stringsToFind);
                    } else {
                        fileHandler.handle(file, stringsToFind);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    System.out.printf("Visit %s failed; reasons: %s", file.toString(), exc.getMessage());
                    if (Paths.get(scanningPath).equals(file)) {
                        throw new IOException("Scanning failed");
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (Paths.get(scanningPath).equals(dir)) {
                        System.out.printf("Scanning %s finished successfully", scanningPath);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
