package main.java.semantic.entities.predefined;

import main.java.model.Token;
import main.java.model.TokenType;
import main.java.semantic.entities.Class;

public class Object {
    private static boolean initialized = false;
    public static java.lang.String name = "Object";

    public static Token token = new Token(
        TokenType.idClass,
        name,
        0,
        0
    );

    private static final Class object = new Class(name, token);

    public static Class Class() {
        if (!initialized) init();
        return object;
    }

    private static void init() {
        initialized = true;
    }
}