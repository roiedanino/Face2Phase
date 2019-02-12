package com.insta2phase.utils;

public class StringUtils {
    public static String toPascalCase(String input){
        if(input.length() < 2){
            return input.toUpperCase();
        }
        return input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
