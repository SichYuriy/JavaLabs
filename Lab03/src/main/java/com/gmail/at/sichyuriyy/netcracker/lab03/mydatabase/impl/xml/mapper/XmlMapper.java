package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl.xml.mapper;

import javax.xml.stream.XMLOutputFactory;

/**
 * Created by Yuriy on 05.03.2017.
 */
public interface XmlMapper<T> {

    String toXml(XMLOutputFactory factory, T obj);
}
