package com.example.tttao.calculator;

import java.util.Arrays;

public class Modify {

    public static String test() {
        String args = "123+675*7-88*3-1";
        String resultString = Arrays.toString(args.split("((?<=[^0-9])|(?=[^0-9]))"));
        return resultString.replaceAll(" ", "");
    }
}
