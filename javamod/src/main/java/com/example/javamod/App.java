package com.example.javamod;

import com.google.common.base.Joiner;

public class App {

    private static final Joiner joiner = Joiner.on(" ");

    public static void main(String[] args) {

        System.out.println(joiner.join("Hello", "World!"));

        System.out.println("Args:");
        for (int i = 0; i < args.length; i++) {
            System.out.printf("%d: %s\n", i, args[i]);
        }
    }
}
