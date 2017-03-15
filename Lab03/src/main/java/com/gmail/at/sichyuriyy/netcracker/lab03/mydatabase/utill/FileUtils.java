package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.utill;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by Yuriy on 02.03.2017.
 */
public class FileUtils {

    public static void deleteDirRecursively(Path root) throws IOException {
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException
            {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e)
                    throws IOException
            {
                if (e == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    throw e;
                }
            }
        });
    }

    public static void deleteDirHard(Path root) throws IOException {
        while (true) {
            try {
                deleteDirRecursively(root);
                return;
            } catch (IOException e) {
            }
        }
    }

    public static void deleteFile(Path path) throws IOException {
        while (true) {
            try {
                Files.delete(path);
                return;
            } catch (IOException e) {

            }
        }


    }


}
