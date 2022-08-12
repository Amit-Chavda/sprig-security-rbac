package com.springsecurity.rbac.springsecurityrbac.util;


public class Console {

    public static void println(String message, Class aClass) {
        System.out.println(aClass.getName() + ":  " + message);
    }
}
