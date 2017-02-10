package com.gmail.at.sichyuriyy.netcracker.lab03;

import com.gmail.at.sichyuriyy.netcracker.lab03.entity.Role;
import javafx.beans.binding.IntegerExpression;
import javafx.util.Pair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Yuriy on 25.01.2017.
 */

public class Main {



    public static void main(String[] args) {
        //printBits((byte)-2);

        printBits(~(Integer.MIN_VALUE >>> 3));
        printBits((Integer.MIN_VALUE >>> 3));

        printBits(Integer.MIN_VALUE);
        printBits(Integer.MIN_VALUE >>> 1);
        printBits(Integer.MIN_VALUE >>> 2);
        printBits(Integer.MIN_VALUE >>> 3);

    }

    public static void printBits(int val) {
        int tempMask = Integer.MIN_VALUE >>> 1;
        if (val < 0) {
            System.out.print(1);
        } else {
            System.out.print(0);
        }

        for (int i = 1; i < 32; i++) {
            if ((val & tempMask) != 0) {
                System.out.print(1);
            } else {
                System.out.print(0);
            }
            if ((i + 1) % 8 == 0) {
                System.out.print(" ");
            }
            tempMask >>= 1;


        }
        System.out.println();
    }
}
