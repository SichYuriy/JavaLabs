package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.utill;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 02.03.2017.
 */
public class FileUtilsTest {

    @Test
    public void deleteDirRecursively() throws Exception {
        String root = "fileUtilTestDirectory";
        Files.createDirectories(Paths.get(root, "dir1\\dir1_1"));
        Files.createDirectories(Paths.get(root,"fileUtilTestDirectory\\dir1\\dir1_2"));
        Files.createDirectories(Paths.get(root, "fileUtilTestDirectory\\dir2\\dir2_1"));
        FileUtils.deleteDirRecursively(Paths.get(root));
        assertFalse(Files.exists(Paths.get(root)));
    }

}