package com.me.remenber.services;

import java.util.*;

public class PasswordGenerator{

    public PasswordGenerator() {
    }

    public static char[] greek_password(int len){
        System.out.println(" Generating password using ramdom");
        System.out.println("You new password is: ");
        String capitalChars ="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String smallChars = "abcdefghijklmnoprstuvwxyx";
        String numbers = "0123456789";
        String symbols = "*!@#$%^&*()_+=/.?<>";
        String values = capitalChars + smallChars +numbers+symbols;
        Random rndm_method = new Random();
        char[] password = new char[len];

        for(int i =0; i<len; i++){
            password [i] = values.charAt(rndm_method.nextInt(values.length()));
        }
        return password;
    }
}
