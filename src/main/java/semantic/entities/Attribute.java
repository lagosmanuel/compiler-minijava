package main.java.semantic.entities;

import main.java.model.Token;
import main.java.semantic.SymbolTable;
import main.java.semantic.entities.model.Variable;
import main.java.messages.SemanticErrorMessages;
import main.java.exeptions.SemanticException;

import java.util.Objects;

public class Attribute extends Variable {
    protected boolean is_private = false;
    protected boolean is_static = false;

    public Attribute(String attr_name, Token attr_token, Token type_token, boolean is_static, boolean is_private) {
        super(attr_name, attr_token, type_token);
        this.is_static = is_static;
        this.is_private = is_private;
    }

    public boolean isStatic() {
        return is_static;
    }

    public boolean isPrivate() {
        return is_private;
    }

    @Override
    public void validate() throws SemanticException {
        super.validate();
        if (Objects.equals(type.getName(), "void"))
            SymbolTable.throwException(SemanticErrorMessages.ATTRIBUTE_VOID, type.getToken());
    }
}
