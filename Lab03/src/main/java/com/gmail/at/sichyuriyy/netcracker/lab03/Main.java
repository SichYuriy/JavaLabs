package com.gmail.at.sichyuriyy.netcracker.lab03;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Role;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.beans.binding.IntegerExpression;
import javafx.util.Pair;

import javax.xml.stream.*;
import java.beans.XMLEncoder;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Date;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Yuriy on 25.01.2017.
 */

public class Main {



    public static void main(String[] args) throws IOException, XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
        StringWriter out = new StringWriter();
        XMLStreamWriter sw = xmlOutputFactory.createXMLStreamWriter(out);

        XmlMapper mapper = new XmlMapper(xmlInputFactory);

        sw.writeStartElement("root");
        sw.writeCharacters("123");
        sw.writeEndElement();
        sw.close();
        System.out.println(out.toString());

        XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new StringReader("<root>123</root>"));
        System.out.println(reader.getEventType());
        reader.next();
        String name = reader.getName().toString();
        reader.next();
        String value = reader.getText();

        System.out.println(name + " " + value);



        reader.close();
    }


}
