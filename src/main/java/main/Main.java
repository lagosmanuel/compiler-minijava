package main.java.main;

import main.java.exeptions.SemanticException;
import main.java.lexer.Lexer;
import main.java.lexer.LexerImpl;
import main.java.config.LexerConfig;
import main.java.parser.Parser;
import main.java.parser.ParserImpl;
import main.java.model.Token;
import main.java.model.TokenType;
import main.java.model.Pair;
import main.java.model.Error;
import main.java.symboltable.SymbolTable;
import main.java.utils.sourcemanager.SourceManager;
import main.java.utils.sourcemanager.SourceManagerImpl;
import main.java.utils.Formater;
import main.java.messages.ErrorMessages;
import main.java.exeptions.LexicalException;
import main.java.exeptions.SyntacticException;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Main {
    private static Map<Integer, Pair<List<Error>, String>> errors;
    private static SourceManager sourceManager;
    private static Lexer lexer;
    private static Parser parser;

    public static void main(String[] args) {
        init();

        if (args.length == 1) {
            if (loadFile(args[0])) {
                //showTokens();
                compile();
                closeFile();
            }
        } else {
            System.out.println(ErrorMessages.BAD_USAGE);
        }
    }

    private static void init() {
        errors = new HashMap<>();
        sourceManager = new SourceManagerImpl();
        lexer = new LexerImpl(sourceManager, errors);
        parser = new ParserImpl(lexer, errors);
    }

    private static boolean loadFile(String filename) {
        boolean loaded = false;
        try {
            sourceManager.open(filename);
            loaded = true;
        } catch (FileNotFoundException error) {
            System.out.println(error.getMessage());
        }
        return loaded;
    }

    private static void closeFile() {
        try {
            sourceManager.close();
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }

    private static void showTokens() {
        Token token;
        boolean onRecovery = false;

        do {
            token = null;

            try {
                token = lexer.nextToken();
                if (token != null) System.out.println(token);
            } catch (LexicalException exception) {
                if (LexerConfig.CONTINUE_ON_ERROR) onRecovery = true;
                else System.out.print(exception.getMessage());
            }
        } while ((token != null && token.getType() != TokenType.EOF) || (token == null && onRecovery));

        if (!onRecovery && token != null && token.getType() == TokenType.EOF)
            System.out.println(ErrorMessages.SUCCESS);
    }

    private static void compile() {
        try {
            SymbolTable.init(errors);
            parser.parse();
        } catch (SyntacticException error) {
            System.out.println(error.getMessage());
        } finally {
            if (errors.isEmpty()) System.out.println(ErrorMessages.SUCCESS);
            else showErrors(errors);
        }
    }

    private static void showErrors(Map<Integer, Pair<List<Error>, String>> errors) {
        errors.keySet().stream().sorted().forEach(line ->
            errors.get(line).getFirst().forEach(error ->
                System.out.println("\n" + Formater.formatError(
                    error,
                    sourceManager.getLineText(line))
                )
            )
        );
    }
}