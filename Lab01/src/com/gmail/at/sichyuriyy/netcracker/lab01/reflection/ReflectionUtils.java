package com.gmail.at.sichyuriyy.netcracker.lab01.reflection;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * @author Sych
 *
 */
public class ReflectionUtils {

    private ClassLoader classLoader;
    private Logger logger;

    public ReflectionUtils() {
        classLoader = Thread.currentThread().getContextClassLoader();
        logger = Logger.getLogger(ReflectionUtils.class);
    }

    /**
     * @param packageName root package for searching implementations of an interface
     * @param parent interface
     * @return list of classes which implement parent interface
     */
    public List<Class<?>> findAllSubTypes(String packageName, Class<?> parent) {
        List<Class<?>> classes = null;
        try {
            classes = findAllClasses(packageName);
        } catch (ClassNotFoundException | IOException e) {
            logger.error(e);
        }
        if (classes == null) {
            return null;
        }

        List<Class<?>> result = new LinkedList<>();
        for (Class<?> clazz : classes) {
            Type[] interfaces = clazz.getInterfaces();
            for (Type interfaze : interfaces) {
                if (interfaze.equals(parent)) {
                    result.add(clazz);
                    break;
                }
            }
        }

        return result;
    }
    
    /**
     * @param packageName root package for searching annotated classes
     * @param annotationClass class of annotation
     * @return list of classes which are annotated with defined annotation
     */
    public List<Class<?>> findAllAnnotatedClasses(String packageName, Class<? extends Annotation> annotationClass) {
        List<Class<?>> classes = null;
        try {
            classes = findAllClasses(packageName);
        } catch (ClassNotFoundException | IOException e) {
        }
        if (classes == null) {
            return null;
        }

        List<Class<?>> result = new LinkedList<>();
        for (Class<?> clazz : classes) {
            if (clazz.getAnnotation(annotationClass) != null) {
                result.add(clazz);
            }
        }

        return result;
    }

    /**
     * @param packageName root package for searching classes
     * @return list of all classes in the package
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private List<Class<?>> findAllClasses(String packageName) throws IOException, ClassNotFoundException {
        String resourcesName = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(resourcesName);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {

            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        List<Class<?>> classes = new LinkedList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    /**
     * @param directory root directory for searching classes
     * @param packageName
     * @return list of all classes in the directory
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException, IOException {
        List<Class<?>> classes = new LinkedList<>();
        if (!directory.exists()) {
            return classes;
        }

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(
                        Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

}
