package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.xml.parser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * Created by Yuriy on 05.03.2017.
 */
public interface XmlParser<T> {

    T fromXml(XMLInputFactory factory, String str) throws XMLStreamException;
}
