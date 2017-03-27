package com.gmail.at.sichyuriyy.netcracker.lab03.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Yuriy on 3/21/2017.
 */
public class ResourceUtil {

    public static File getResourceFile(String path) {
        URL url = ResourceUtil.class.getClassLoader().getResource(path);

        File file;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Bad path" ,e);
        }


        if (file.exists()) {
            return file;
        } else {
            throw new IllegalArgumentException("Cannot load resource");
        }
    }
}
