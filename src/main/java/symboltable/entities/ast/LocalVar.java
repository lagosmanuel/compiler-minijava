package main.java.symboltable.entities.ast;

import main.java.exeptions.SemanticException;
import main.java.messages.SemanticErrorMessages;
import main.java.model.Token;
import main.java.model.TokenType;
import main.java.symboltable.SymbolTable;
import main.java.symboltable.entities.type.Type;
import main.java.symboltable.entities.type.TypeVar;
import main.java.symboltable.entities.ast.expression.Expression;
import main.java.codegen.Comment;
import main.java.codegen.Instruction;
import main.java.symboltable.entities.predefined.Wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocalVar extends Statement {
    private Type type;
    private Type valueType;
    private final Token identifier;
    private final Expression value;
    private final List<LocalVar> localVars = new ArrayList<>();
    private int offset;

    public LocalVar(Type type, List<Token> identifiers, Expression value) {
        super(type.getToken());
        this.type = type;
        this.identifier = type.getToken();
        this.value = value;
        identifiers.forEach(identifier -> localVars.add(new LocalVar(type, identifier, value)));
    }

    public LocalVar(Type type, List<Token> identifiers) {
        super(type.getToken());
        this.type = type;
        this.identifier = type.getToken();
        this.value = null;
        identifiers.forEach(identifier -> localVars.add(new LocalVar(type, identifier, null)));
    }

    private LocalVar(Type type, Token identifier, Expression value) {
        super(identifier);
        this.type = type;
        this.identifier = identifier;
        this.value = value;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Type getType() {
        return type;
    }

    private void setType(Type type) {
        this.type = type;
        localVars.forEach(localVar -> localVar.setType(type));
    }

    @Override
    public void check() throws SemanticException {
        if (checked()) return;
        super.check();
        if (type == null) return;

        type.validate();
        valueType = value != null? value.checkType():null;
        for (TypeVar typeVar:type.getTypeParams()) typeVar.check();

        if (valueType != null && !type.compatible(valueType)) {
            SymbolTable.throwException(
                String.format(
                    SemanticErrorMessages.TYPE_NOT_COMPATIBLE,
                    type.getName(),
                    valueType.getName()
                ),
                getIdentifier()
            );
        }

        for (LocalVar localVar:localVars) {
            if (alreadyDeclared(localVar.getIdentifier())) continue;
            localVar.setParent(getParent());
            if (Objects.equals(type.getToken().getType(), TokenType.kwVar)) localVar.setType(valueType);
            if (getParent() != null) getParent().addLocalVar(localVar);
        }
    }

    private boolean alreadyDeclared(Token identifier) throws SemanticException {
        boolean declared = true;
        if (SymbolTable.actualUnit.getParameter(identifier.getLexeme()) != null) {
            SymbolTable.throwException(
                String.format(
                    SemanticErrorMessages.VARIABLE_ALREADY_DECLARED_PARAMETER,
                    identifier.getLexeme()
                ),
                identifier
            );
        } else if (getParent() != null && getParent().hasLocalVar(identifier.getLexeme())) {
            SymbolTable.throwException(
                String.format(
                    SemanticErrorMessages.VARIABLE_ALREADY_DECLARED,
                    identifier.getLexeme()
                ),
                identifier
            );
        } else declared = false;
        return declared;
    }

    @Override
    public void generate() {
        if (value != null) {
            value.generate();
            Wrapper.wrap(valueType);
            for (int i = 0; i < localVars.size()-1; ++i) {
                SymbolTable.getGenerator().write(
                    Instruction.DUP.toString(),
                    Comment.VAR_ALLOC.formatted(localVars.get(i+1).getIdentifier().getLexeme())
                );
            }
        } else {
            SymbolTable.getGenerator().write(
                Instruction.RMEM.toString(),
                String.valueOf(localVars.size()),
                Comment.VAR_ALLOC.formatted(names())
            );
        }
        if (getParent() != null) getParent().allocateVars(localVars.size());
    }

    private String names() {
        if (localVars.isEmpty()) return "";
        StringBuilder names = new StringBuilder();
        for (LocalVar localVar:localVars)
            names.append(localVar.getIdentifier().getLexeme())
                 .append(", ");
        return names.substring(0, names.length()-2);
    }
}
