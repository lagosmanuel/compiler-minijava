package main.java.symboltable.entities;

import main.java.model.Token;
import main.java.symboltable.SymbolTable;
import main.java.symboltable.entities.ast.expression.Expression;
import main.java.config.CodegenConfig;
import main.java.codegen.Labeler;
import main.java.messages.SemanticErrorMessages;
import main.java.exeptions.SemanticException;
import main.java.symboltable.entities.type.Type;

import java.util.Objects;

public class Attribute extends Variable {
    protected final boolean is_private;
    protected final boolean is_static;
    protected Expression expression;
    protected String label;

    public Attribute(String attr_name, Token attr_token, Type attr_type, boolean is_static, boolean is_private) {
        super(attr_name, attr_token, attr_type);
        this.is_static = is_static;
        this.is_private = is_private;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isStatic() {
        return is_static;
    }

    public boolean isPrivate() {
        return is_private;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void validate() throws SemanticException {
        if (isValidated()) return;
        super.validate();
        if (Objects.equals(type.getName(), "void"))
            SymbolTable.throwException(SemanticErrorMessages.ATTRIBUTE_VOID, type.getToken());
        setLabel(Labeler.getLabel(
            CodegenConfig.ATTRIBUTE_NAME_FORMAT,
            Labeler.getLabel(true, name),
            SymbolTable.actualClass.getName()
        ));
    }
}
