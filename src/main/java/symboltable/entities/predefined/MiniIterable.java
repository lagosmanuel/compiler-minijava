package main.java.symboltable.entities.predefined;

import main.java.model.Token;
import main.java.model.TokenType;
import main.java.symboltable.SymbolTable;
import main.java.symboltable.entities.type.Type;
import main.java.symboltable.entities.type.ClassType;
import main.java.symboltable.entities.type.PrimitiveType;
import main.java.symboltable.entities.type.TypeVar;
import main.java.symboltable.entities.Class;
import main.java.symboltable.entities.AbstractMethod;

import java.util.List;
import java.lang.String;

public class MiniIterable {
    private static boolean initialized = false;
    public static final java.lang.String name = "MiniIterable";

    public final static String METHOD_START_NAME = "start";
    public final static String METHOD_HASNEXT_NAME = "hasNext";
    public final static String METHOD_NEXT_NAME = "next";

    public final static int METHOD_START_OFFSET = 0;
    public final static int METHOD_HASNEXT_OFFSET = 1;
    public final static int METHOD_NEXT_OFFSET = 2;

    public static final Token token = new Token(
        TokenType.idClass,
        name,
        0,
        0
    );

    public static final TypeVar T = new TypeVar(
        "T",
        new Token(
            TokenType.idClass,
            "T",
            0,
            0
        ),
        null,
        0
    );

    public static final Type type = new ClassType(name, token, List.of(T));
    private static final Class object = new Class(name, token);

    public static Class Class() {
        if (!initialized) init();
        return object;
    }

    private static void init() {
        initialized = true;
        Class actualClass = SymbolTable.actualClass;
        SymbolTable.actualClass = object;
        object.setAbstract();
        addTypeVar();
        addStart();
        addHasNext();
        addNext();
        SymbolTable.actualClass = actualClass;
    }

    private static void addTypeVar() {
        object.addTypeParameter(T);
    }

    private static void addStart() {
        AbstractMethod method = new AbstractMethod(
            METHOD_START_NAME,
            new Token(
                TokenType.idMetVar,
                METHOD_START_NAME,
                0,
                0
            )
        );
        method.setReturnType(PrimitiveType.VOID_TYPE);
        object.addAbstractMethod(method);
    }

    private static void addNext() {
        AbstractMethod method = new AbstractMethod(
            METHOD_NEXT_NAME,
            new Token(
                TokenType.idMetVar,
                METHOD_NEXT_NAME,
                0,
                0
            )
        );
        method.setReturnType(T);
        object.addAbstractMethod(method);
    }

    private static void addHasNext() {
        AbstractMethod method = new AbstractMethod(
            METHOD_HASNEXT_NAME,
            new Token(
                TokenType.idMetVar,
                METHOD_HASNEXT_NAME,
                0,
                0
            )
        );
        method.setReturnType(PrimitiveType.BOOLEAN_TYPE);
        object.addAbstractMethod(method);
    }
}
