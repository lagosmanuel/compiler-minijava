package main.java.symboltable;

import main.java.model.Error;
import main.java.model.ErrorType;
import main.java.model.Pair;
import main.java.model.Token;
import main.java.model.TokenType;
import main.java.symboltable.entities.Class;
import main.java.symboltable.entities.Unit;
import main.java.symboltable.entities.Constructor;
import main.java.codegen.Generator;
import main.java.codegen.Labeler;
import main.java.codegen.Comment;
import main.java.codegen.Instruction;
import main.java.codegen.Strings;
import main.java.config.CodegenConfig;
import main.java.config.SemanticConfig;
import main.java.exeptions.SemanticException;
import main.java.messages.SemanticErrorMessages;
import main.java.symboltable.entities.ast.Block;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class SymbolTable {
    public static Class actualClass;
    public static Unit actualUnit;
    public static Block actualBlock;
    public static Map<Integer, Pair<List<Error>, String>> errors;
    private final static Map<String, Class> classes = new HashMap<>();
    private static boolean hasMain = false;
    private static Token EOF;
    private static Generator generator;

    public static void init(Map<Integer, Pair<List<Error>, String>> errors_map) {
        errors = errors_map;
        classes.clear();
        actualClass = null;
        actualUnit = null;
        actualBlock = null;
        addPrimitiveClasses();
        hasMain = false;
        EOF = null;
        Labeler.reset();
        Strings.reset();
    }

    public static void validate() throws SemanticException {
        for (Class myClass:classes.values()) {
            actualClass = myClass;
            myClass.validate();
        }
        if (!hasMain) throwException(SemanticErrorMessages.MAIN_NOT_FOUND, EOF);
    }

    public static void consolidate() {
        if (errors.isEmpty()) for (Class myClass:classes.values()) {
            actualClass = myClass;
            myClass.consolidate();
        }
    }

    public static void check() throws SemanticException {
        if (errors.isEmpty()) for (Class myClass:classes.values()) {
            actualClass = myClass;
            myClass.check();
        }
    }

    public static void generate() {
        if (errors.isEmpty()) {
            generator.write(CodegenConfig.CODE);
            generator.write(CodegenConfig.LINE_SPACE);
            callHeapInit();
            initStaticAttributes();
            callMain();
            loadHeapFunctions();
            for (Class myClass:classes.values()) {
                actualClass = myClass;
                myClass.generate();
            }
            Strings.generate();
        }
        generator.close();
    }

    private static void callHeapInit() {
        generator.write(Instruction.PUSH.toString(), CodegenConfig.HEAP_INIT_LABEL, Comment.HEAP_INIT_CALL);
        generator.write(Instruction.CALL.toString());
        generator.write(CodegenConfig.LINE_SPACE);
    }

    private static void initStaticAttributes() {
        for (Class myClass:classes.values()) {
            actualClass = myClass;
            myClass.init_attr_static();
        }
    }

    private static void callMain() {
        generator.write(Instruction.PUSH.toString(), CodegenConfig.MAIN_LABEL, Comment.MAIN_CALL);
        generator.write(Instruction.CALL.toString());
        generator.write(Instruction.HALT.toString());
        generator.write(CodegenConfig.LINE_SPACE);
    }

    private static void loadHeapFunctions() {
        loadHeapInit();
        generator.write(CodegenConfig.LINE_SPACE);
        loadMalloc();
        generator.write(CodegenConfig.LINE_SEPARATOR);
    }

    private static void loadHeapInit() {
        generator.write(
            Labeler.getLabel(CodegenConfig.LABEL, CodegenConfig.HEAP_INIT_LABEL),
            Instruction.RET.toString(), "0",
            Comment.HEAP_INIT
        );
    }

    private static void loadMalloc() {
        generator.write(
            Labeler.getLabel(CodegenConfig.LABEL, CodegenConfig.MALLOC_LABEL),
            Instruction.LOADFP.toString(),
            Comment.MALLOC
        );
        generator.write(Instruction.LOADSP.toString());
        generator.write(Instruction.STOREFP.toString());
        generator.write(Instruction.LOADHL.toString());
        generator.write(Instruction.DUP.toString());
        generator.write(Instruction.PUSH.toString(), "1");
        generator.write(Instruction.ADD.toString());
        generator.write(Instruction.STORE.toString(), "4");
        generator.write(Instruction.LOAD.toString(), "3");
        generator.write(Instruction.ADD.toString());
        generator.write(Instruction.STOREHL.toString());
        generator.write(Instruction.STOREFP.toString());
        generator.write(Instruction.RET.toString(), "1");
    }

    public static Generator getGenerator() {
        return generator;
    }

    public static boolean hasMain() {
        return hasMain;
    }

    public static void foundMain() {
        hasMain = true;
    }

    public static boolean hasClass(String class_name) {
        return classes.containsKey(class_name);
    }

    public static Class getClass(String class_name) {
        return classes.get(class_name);
    }

    public static void addClass(Class newClass) {
        if (newClass == null) return;
        if (classes.containsKey(newClass.getName())) saveError(
            String.format(
                SemanticErrorMessages.CLASS_DUPLICATE,
                newClass.getName()
            ),
            newClass.getToken()
        );
        else classes.put(newClass.getName(), newClass);
    }

    public static void saveEOF(Token EOF) {
        SymbolTable.EOF = EOF;
    }

    public static Constructor getNewDefaultConstructor() {
        Constructor constructor = new Constructor(
            actualClass.getName(),
            new Token(
                TokenType.idClass,
                actualClass.getName(),
                0,
                0
            )
        );
        constructor.setBody(new Block(
            new Token(
                TokenType.leftBrace,
                "{",
                0,
                0
            )
        ));
        return constructor;
    }

// --------------------------------------------------------------------------------------------------------------------

    private static void addPrimitiveClasses() {
        addClass(main.java.symboltable.entities.predefined.Object.Class());
        addClass(main.java.symboltable.entities.predefined.String.Class());
        addClass(main.java.symboltable.entities.predefined.System.Class());
        addClass(main.java.symboltable.entities.predefined.MiniIterable.Class());
        addClass(main.java.symboltable.entities.predefined.Boolean.Class());
        addClass(main.java.symboltable.entities.predefined.Character.Class());
        addClass(main.java.symboltable.entities.predefined.Integer.Class());
        addClass(main.java.symboltable.entities.predefined.Float.Class());
    }

    public static void saveError(String message, Token token) {
        if (token == null) return;

        if (!errors.containsKey(token.getLine()))
            errors.put(token.getLine(), new Pair<>(new ArrayList<>(), ""));

        errors.get(token.getLine()).getFirst().add(new Error(
            message,
            token.getLexeme(),
            token.getLine(),
            token.getColumn(),
            ErrorType.Semantic
        ));
    }

    public static void throwException(String message, Token token) throws SemanticException {
        saveError(message, token);
        if (!SemanticConfig.CONTINUE_ON_ERROR) throw new SemanticException(message);
    }
}
