package main.java.symboltable.entities.type;

import main.java.model.Token;
import main.java.model.TokenType;
import main.java.symboltable.SymbolTable;
import main.java.symboltable.entities.Class;
import main.java.symboltable.entities.predefined.Object;
import main.java.messages.SemanticErrorMessages;
import main.java.exeptions.SemanticException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ClassType extends Type {
    public static final String INT_WRAPPER = "Integer";
    public static final String FLOAT_WRAPPER = "Float";
    public static final String BOOLEAN_WRAPPER = "Boolean";
    public static final String CHAR_WRAPPER = "Character";
    public static final String STRING = "String";
    public static final String NULL = "null";

    public ClassType(String class_name, Token class_token, List<TypeVar> type_params) {
        super(class_name, class_token, type_params);
    }

    public ClassType(String class_name, Token class_token) {
        super(class_name, class_token);
    }

    @Override
    public void validate() throws SemanticException {
        if (isValidated()) return;
        super.validate();

        if (!SymbolTable.hasClass(getName()) && !SymbolTable.actualClass.hasTypeParameter(getName()))
            SymbolTable.throwException(
                String.format(
                    SemanticErrorMessages.CLASS_NOT_DECLARED,
                    getName()
                ),
                getToken()
            );

        if (SymbolTable.actualClass.hasTypeParameter(getName())) {
            if (getTypeParamsCount() > 0)
                SymbolTable.throwException(SemanticErrorMessages.TYPE_PARAMETER_RECURSIVE, getToken());
        } else if (SymbolTable.hasClass(getName())) {
            if (SymbolTable.getClass(getName()).getTypeParametersCount() != getTypeParamsCount())
                SymbolTable.throwException(
                    String.format(
                        SemanticErrorMessages.INVALID_TYPE_PARAMETERS_COUNT,
                        SymbolTable.getClass(getName()).getTypeParametersCount()
                    ),
                    getToken()
                );
            for (TypeVar typeVar:getTypeParams()) typeVar.validate();
        }
    }

    @Override
    public boolean compatible(Type type) {
        if (type == null) return false;
        if (type == this) return true;
        if (Objects.equals(this.getName(), Object.name)) return true;
        if (Objects.equals(type.getName(), NULL)) return true;

        if (match(type) ||
            (type instanceof ClassType classType &&
            !Objects.equals(classType.getName(), Object.name) &&
            this.compatible(ClassType.getSuperType(type)))
        ) return true;

        if (Objects.equals(this.getName(), INT_WRAPPER))
            return Objects.equals(type.getName(), PrimitiveType.INT) || type.isChar();
        if (Objects.equals(this.getName(), FLOAT_WRAPPER))
            return Objects.equals(type.getName(), PrimitiveType.FLOAT) || type.isInt() || type.isChar();
        if (Objects.equals(this.getName(), CHAR_WRAPPER))
            return Objects.equals(type.getName(), PrimitiveType.CHAR) || type.isInt();
        if (Objects.equals(this.getName(), BOOLEAN_WRAPPER))
            return Objects.equals(type.getName(), PrimitiveType.BOOLEAN);
        if (Objects.equals(this.getName(), STRING))
            return type.isChar() || type.isInt();

        return false;
    }

    private static Type getSuperType(Type type) {
        if (type == null) return null;
        Class myclass = SymbolTable.getClass(type.getName());
        if (myclass == null) return null;

        Map<String, TypeVar> map = new HashMap<>();
        for (int i = 0; i < myclass.getTypeParametersCount(); ++i) {
            if (type.getTypeParam(i) != null)
                map.put(myclass.getTypeParameter(i).getName(), type.getTypeParam(i));
        }

        Type superType = myclass.getSuperType();
        superType.getTypeParams().forEach(typeVar -> {
            if (map.containsKey(typeVar.getName())) {
                typeVar.setInstanceType(map.get(typeVar.getName()).getInstaceType() != null?
                    map.get(typeVar.getName()).getInstaceType():
                    map.get(typeVar.getName())
                );
            }
        });
        return superType;
    }

    public Type getAncestor(String name) {
        if (Objects.equals(getName(), name)) return this;
        if (Objects.equals(getName(), Object.name)) return null;
        return ClassType.getSuperType(this) instanceof ClassType classType? classType.getAncestor(name):null;
    }

    private boolean match(Type type) {
        if (type instanceof TypeVar typeVar) type = typeVar.getInstaceType();
        if (type == null) return false;
        boolean match = Objects.equals(this.getName(), type.getName()) &&
                        this.getTypeParamsCount() == type.getTypeParamsCount();

        if (type instanceof ClassType classType) {
            for (int i = 0; i < this.getTypeParamsCount() && match; ++i) {
                String thisType = getTypeParam(i).getInstaceType() != null? getTypeParam(i).getInstaceType().getName():getTypeParam(i).getName();
                String thatType = classType.getTypeParam(i).getInstaceType() != null? classType.getTypeParam(i).getInstaceType().getName():classType.getTypeParam(i).getName();
                match = Objects.equals(thisType, thatType) || getTypeParam(i).getInstaceType() == null;
            }
            if (Objects.equals(this.getName(), classType.getName()) && classType.getTypeParamsCount() == 0) match = true;
        } else match = false;

        return match;
    }

    public static final Type STRING_TYPE = new ClassType(
        ClassType.STRING,
        new Token(TokenType.idClass, ClassType.STRING, 0, 0)
    );

    public static final Type NULL_TYPE = new ClassType(
        ClassType.NULL,
        new Token(TokenType.nullLiteral, ClassType.NULL, 0, 0)
    );
}
