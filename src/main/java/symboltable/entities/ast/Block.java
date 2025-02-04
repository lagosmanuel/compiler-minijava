package main.java.symboltable.entities.ast;

import main.java.model.Token;
import main.java.symboltable.SymbolTable;
import main.java.symboltable.entities.Constructor;
import main.java.symboltable.entities.ast.primary.SuperAccess;
import main.java.codegen.Comment;
import main.java.codegen.Instruction;
import main.java.messages.SemanticErrorMessages;
import main.java.exeptions.SemanticException;

import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

public class Block extends Statement {
    private final List<Statement> statements = new ArrayList<>();
    private final List<LocalVar> localVars = new ArrayList<>();
    private String labelEnd;
    private int allocated_vars_count;

    public Block(Token identifier) {
        super(identifier);
    }

    public void addStatement(Statement statement) {
        if (statement == null) return;
        statements.add(statement);
        statement.setParent(this);
    }

    public Statement getStatement(int index) {
        return index >= 0 && index < statements.size()? statements.get(index):null;
    }

    public boolean hasLocalVar(String varName) {
        return localVars.stream().anyMatch(
            localVar -> Objects.equals(localVar.getIdentifier().getLexeme(), varName));
    }

    public LocalVar getLocalVar(String varName) {
        return localVars.stream().filter(
            localVar -> Objects.equals(localVar.getIdentifier().getLexeme(), varName))
                .findFirst()
                .orElse(null);
    }

    public void addLocalVar(LocalVar localVar) {
        if (localVar == null) return;
        localVar.setOffset(-localVars.size());
        localVars.add(localVar);
    }

    public List<LocalVar> getLocalVars() {
        return localVars;
    }

    public int getAllocatedVarsCount() {
        return allocated_vars_count;
    }

    public void allocateVars(int count) {
        allocated_vars_count += count;
    }

    public String getLabelEnd() {
        return labelEnd;
    }

    public void setLabelEnd(String labelEnd) {
        this.labelEnd = labelEnd;
    }

    public int ownLocalVarCount() {
        return getParent() != null?
            getAllocatedVarsCount()-getParent().getAllocatedVarsCount():
            getAllocatedVarsCount();
    }

    @Override
    public void check() throws SemanticException {
        if (checked()) return;
        super.check();
        SymbolTable.actualBlock = this;
        if (getParent() != null) getParent().getLocalVars().forEach(this::addLocalVar);
        for (Statement statement:statements) {
            if (hasReturn()) {
                SymbolTable.throwException(
                    SemanticErrorMessages.UNREACHABLE_CODE,
                    statement.getIdentifier()
                );
            }
            statement.check();
            checkSuper(statement);
            if (statement.hasReturn()) setReturnable();
        }
        SymbolTable.actualBlock = getParent();
    }

    public void generate() {
        if (getParent() != null) allocateVars(getParent().getAllocatedVarsCount());
        statements.forEach(Statement::generate);
        SymbolTable.getGenerator().write(
            Instruction.FMEM.toString(),
            getParent() != null?
                String.valueOf(getAllocatedVarsCount()-getParent().getAllocatedVarsCount()):
                String.valueOf(getAllocatedVarsCount()),
            Comment.BLOCK_RET
        );
    }

    private void checkSuper(Statement statement) throws SemanticException{
        if (statement instanceof ExpressionStatement expressionStatement &&
            expressionStatement.getExpression() instanceof SuperAccess superAccess &&
            superAccess.isConstructorCall())
        {
            if (!(SymbolTable.actualUnit instanceof Constructor)) {
                SymbolTable.throwException(
                    SemanticErrorMessages.SUPER_OUTSIDE_CONSTRUCTOR,
                    superAccess.getIdentifier()
                );
            } else if (statements.getFirst() != statement || getParent() != null) {
                SymbolTable.throwException(
                    SemanticErrorMessages.SUPER_NOT_FIRST_STATEMENT,
                    superAccess.getIdentifier()
                );
            }
        }
    }
}
