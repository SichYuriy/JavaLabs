package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.impl;

import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.DataType;
import com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.exception.RequestValidationException;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Yuriy on 11.02.2017.
 */
public class RequestValidator {

    public void validateCreateTableRequest(List<Pair<String, DataType>> properties)
            throws RequestValidationException {
        Set<String> propertySet = new HashSet<>();
        for (Pair<String, DataType> pair: properties) {
            String propertyName = pair.getKey();
            DataType propertyType = pair.getValue();
            if (propertyName == "id") {
                throw new RequestValidationException("id property is auto-generated");
            }
            if (propertySet.contains(propertyName)) {
                throw new RequestValidationException("property " + propertyName +
                        " is already declared");
            }
            if (propertyType == null) {
                throw new RequestValidationException("property type of " + propertyName +
                        " must be defined(not null)");
            }
        }
    }
}
