package com.gmail.at.sichyuriyy.netcracker.lab01;

import java.util.Arrays;
import java.util.List;

import com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory.AbstractIntArrayFactory;
import com.gmail.at.sichyuriyy.netcracker.lab01.intarrayfactory.IntArrayFactory;
import com.gmail.at.sichyuriyy.netcracker.lab01.reflection.ReflectionUtils;
import com.gmail.at.sichyuriyy.netcracker.lab01.sorter.IntSorter;

public class Main {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {

        ReflectionUtils reflectionUtils = new ReflectionUtils();

        List<Class<?>> clazzes = reflectionUtils.findAllSubTypes("com.gmail.at.sichyuriyy.netcracker.lab01",
                IntSorter.class);
        for (Class<?> clazz : clazzes) {
            int[] arr = new int[] { 3, 1, 2 };
            IntSorter sorter = (IntSorter) clazz.newInstance();
            sorter.sort(arr);
            System.out.println(clazz.getName() + ": " + Arrays.toString(arr));
        }
        
        List<Class<?>> factories = reflectionUtils.findAllAnnotatedClasses("com.gmail.at.sichyuriyy.netcracker.lab01",
                IntArrayFactory.class);
        for (Class<?> clazz : factories) {
            AbstractIntArrayFactory factory = (AbstractIntArrayFactory)clazz.newInstance();
            int []arr = factory.create(7);
            System.out.println(clazz.getName() + ": " + Arrays.toString(arr) );
        }
        
        

    }
    
   
}
