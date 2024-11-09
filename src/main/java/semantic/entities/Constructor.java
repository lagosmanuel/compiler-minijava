package main.java.semantic.entities;

import main.java.exeptions.SemanticException;
import main.java.messages.SemanticErrorMessages;
import main.java.model.Token;
import main.java.semantic.SymbolTable;
import main.java.semantic.entities.model.Unit;
import main.java.semantic.entities.model.Statement;
import main.java.semantic.entities.model.statement.ExpressionStatement;
import main.java.semantic.entities.model.statement.primary.SuperAccess;
import main.java.codegen.Instruction;
import main.java.codegen.Comment;
import main.java.config.CodegenConfig;

import java.util.Objects;

public class Constructor extends Unit {
    public Constructor(String cons_name, Token cons_token) {
        super(cons_name, cons_token);
    }

    @Override
    public void validate() throws SemanticException {
        if (isValidated()) return;
        super.validate();
        if (!Objects.equals(getToken().getLexeme(), SymbolTable.actualClass.getName()))
            SymbolTable.throwException(SemanticErrorMessages.CONSTRUCTOR_NAME_MISMATCH, getToken());
    }

    @Override
    public void generate() {
        if (!isMyOwn() || is_generated) return;
        super.generate();
        super_call();
        body.generate();
        epilogue();
        SymbolTable.getGenerator().write(CodegenConfig.LINE_SPACE);
    }

    private void super_call() {
        if (getName().equals(main.java.semantic.entities.predefined.Object.name)) return;
        if (!isConstructorCall(body.getStatement(0))) {
            SymbolTable.getGenerator().write(
                Instruction.LOAD.toString(),
                CodegenConfig.OFFSET_THIS,
                Comment.LOAD_THIS
            );
            SymbolTable.getGenerator().write(
                Instruction.LOAD.toString(),
                getSuperCallLabel(),
                Comment.SUPER_LOAD
            );
            SymbolTable.getGenerator().write(
                Instruction.CALL.toString(),
                Comment.SUPER_CALL
            );
            SymbolTable.getGenerator().write(
                Instruction.POP.toString(),
                Comment.SUPER_DROP_REF
            );
        }
    }

    private boolean isConstructorCall(Statement statement) {
        return statement instanceof ExpressionStatement expressionStatement &&
               expressionStatement.getExpression() instanceof SuperAccess superAccess &&
               superAccess.isConstructorCall();
    }

    private String getSuperCallLabel() {
        Class superClass = SymbolTable.getClass(SymbolTable.actualClass.getSuperType().getName());
        if (!superClass.hasConstructor(superClass.getName())) {
            SymbolTable.saveError(String.format(
                SemanticErrorMessages.CONSTRUCTOR_WITHOUT_PARAMS_NOT_FOUND,
                superClass.getName()
            ), getToken());
        } return superClass.hasConstructor(superClass.getName())?
            superClass.getConstructor(superClass.getName()).getLabel():null;
    }
}
