package com.gmail.at.sichyuriyy.netcracker.lab03;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Yuriy on 25.01.2017.
 */

public class Main {

    public static void main(String[] args) {
        try {
            Files.createFile(Paths.get("testFile.bin"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream writer = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("testFile.bin")))) {
            writer.writeObject(new Integer(1));
            writer.writeObject(new Integer(2));
            writer.writeObject(new Integer(3));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream reader = new ObjectInputStream(new BufferedInputStream(new FileInputStream("testFile.bin")))) {
            while (true) {
                System.out.println(reader.readObject());
            }
        } catch (EOFException e) {

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream writer = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("testFile.bin")))) {
            writer.writeObject(new Integer(1));
            writer.writeObject(new Integer(2));
            writer.writeObject(new Integer(3));
            writer.writeObject(new Integer(4));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.delete(Paths.get("testFile.bin"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

