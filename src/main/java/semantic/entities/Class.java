package main.java.semantic.entities;

import main.java.model.Token;
import main.java.semantic.SymbolTable;
import main.java.semantic.entities.model.Type;
import main.java.semantic.entities.predefined.Object;
import main.java.messages.SemanticErrorMessages;
import main.java.exeptions.SemanticException;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class Class extends Type {
    protected final Map<String, Method> methods = new HashMap<>();
    protected final Map<String, AbstractMethod> abstractMethods = new HashMap<>();
    protected final Map<String, List<Attribute>> attributes = new HashMap<>();
    protected final Map<String, Token> generic_types = new HashMap<>();
    protected final Map<String, Token> super_generic_types = new HashMap<>();

    protected final List<Attribute> instance_attributes = new ArrayList<>();
    protected final List<Attribute> class_attributes = new ArrayList<>();
    protected final List<Method> methods_list = new ArrayList<>();

    protected Constructor constructor;
    protected Token super_token = Object.token;
    protected boolean is_abstract;
    protected boolean is_consolidated = false;

    public Class(String class_name, Token class_token) {
        super(class_name, class_token);
    }

    @Override
    public void validate() throws SemanticException {
        super.validate();

        superNotDeclared();
        cyclicInheritance(Stream.of(token.getLexeme()).collect(Collectors.toSet()));

        if (constructor != null) constructor.validate();
        for (Method method:methods.values()) method.validate();
        for (AbstractMethod abstractMethod:abstractMethods.values()) abstractMethod.validate();
        for (List<Attribute> attribute_list:attributes.values()) attribute_list.getLast().validate();

        consolidate();
    }

    public void consolidate() {
        if (Objects.equals(name, Object.name) || is_consolidated) return;
        Class superClass = SymbolTable.getClass(super_token.getLexeme());
        superClass = superClass == null? Object.Class():superClass; // TODO: check
        superClass.consolidate();
        inheritAttributes(superClass);
        inheritMethods(superClass);
        is_consolidated = true;
    }

    private void inheritAttributes(Class superClass) {
        superClass.getAttributes().forEach(this::addPublicAttributes);
        superClass.getInstanceAttributes().reversed().forEach(instance_attributes::addFirst);
        superClass.getClassAttributes().reversed().forEach(class_attributes::addFirst);
    }

    private void inheritMethods(Class superClass) {
        superClass.getMethods().reversed().forEach(method -> {
            if (!methods.containsKey(method.getName())) {
                if (!method.isPrivate()) methods.put(method.getName(), method);
                methods_list.addFirst(method);
            } else {
                Method redefined = methods.get(method.getName());

                if (!method.isCompatible(redefined))
                    SymbolTable.saveError(SemanticErrorMessages.METHOD_BAD_REDEFINED, redefined.getToken());

                if (!method.isPrivate()) {
                    methods_list.remove(redefined);
                    methods_list.addFirst(redefined);
                } else {
                    methods_list.addFirst(method);
                }
            }
        });
    }

    public void setSuperToken(Token super_token) {
        if (hasGenericType(super_token.getLexeme()))
            SymbolTable.saveError(SemanticErrorMessages.SUPERCLASS_GENERIC_TYPE, super_token);
        else this.super_token = super_token;
    }

    public boolean isAbstract() {
        return is_abstract;
    }

    public void setAbstract() {
        this.is_abstract = true;
    }

// ---------------------------------------- Methods -------------------------------------------------------------------

    public Method getMethod(String method_name) {
        return methods.get(method_name);
    }

    public List<Method> getMethods() {
        return methods_list;
    }

    public void addMethod(String method_name, Method method) {
        if (methodNameAlreadyDefined(method_name)) {
            SymbolTable.saveError(SemanticErrorMessages.METHOD_ALREADY_DEFINED, method.getToken());
        } else {
            methods.put(method_name, method);
            methods_list.add(method);
        }
    }

// ------------------------------------- Constructors  ----------------------------------------------------------------

    public Constructor getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor constructor) {
        if (this.constructor != null)
            SymbolTable.saveError(SemanticErrorMessages.CONSTRUCTOR_ALREADY_DEFINED, constructor.getToken());
        else this.constructor = constructor;
    }

// -------------------------------------- Abstract Methods  -----------------------------------------------------------

    public AbstractMethod getAbstractMethod(String method_name) {
        return abstractMethods.get(method_name);
    }

    public void addAbstractMethod(String method_name, AbstractMethod method) {
        if (methodNameAlreadyDefined(method_name))
            SymbolTable.saveError(SemanticErrorMessages.ABSTRACT_METHOD_ALREADY_DEFINED, method.getToken());
        else abstractMethods.put(method_name, method);
    }

    // ------------------------------------- Attributes  --------------------------------------------------------------

    public Attribute getAttribute(String attr_name) {
        return attributes.containsKey(attr_name)? attributes.get(attr_name).getFirst():null;
    }

    public Map<String, List<Attribute>> getAttributes() {
        return attributes;
    }

    public Attribute addAttribute(String attr_name, Attribute attribute) {
        if (attributes.containsKey(attr_name))
            SymbolTable.saveError(SemanticErrorMessages.ATTRIBUTE_ALREADY_DEFINED, attribute.getToken());
        else {
            attributes.put(attr_name, new ArrayList<>(List.of(attribute)));
            if (attribute.isStatic()) class_attributes.addLast(attribute);
            else instance_attributes.addLast(attribute);
        }
        return attribute;
    }

    public void addAttributes(String attr_name, List<Attribute> attr_list) {
        if (attr_list.isEmpty()) return;
        if (attributes.containsKey(attr_name)) attributes.get(attr_name).addAll(attr_list);
        else attributes.put(attr_name, attr_list);
    }

    public List<Attribute> getInstanceAttributes() {
        return instance_attributes;
    }

    public List<Attribute> getClassAttributes() {
        return class_attributes;
    }

    private void addPublicAttributes(String attr_name, List<Attribute> attr_list) {
        addAttributes(attr_name, attr_list.stream().filter(attr -> !attr.isPrivate()).toList());
    }
// ------------------------------------- Generics --------------------------------------------------------------------

    public boolean hasGenericType(String generic_type_name) {
        return generic_types.containsKey(generic_type_name);
    }

    public Token getGenericType(String generic_type_name) {
        return generic_types.get(generic_type_name);
    }

    public void addGenericType(String generic_type_name, Token type_token) {
        if (classNameAlreadyDefined(generic_type_name))
            SymbolTable.saveError(SemanticErrorMessages.GENERIC_TYPE_ALREADY_DEFINED, type_token);
        else generic_types.put(generic_type_name, type_token);
    }

    public boolean hasSuperGenericType(String super_generic_type_name) {
        return super_generic_types.containsKey(super_generic_type_name);
    }

    public Token getSuperGenericType(String super_generic_type_name) {
        return super_generic_types.get(super_generic_type_name);
    }

    public void addSuperGenericType(String super_generic_type_name, Token type_token) {
        if (classNameAlreadyDefined(super_generic_type_name))
            SymbolTable.saveError(SemanticErrorMessages.SUPER_GENERIC_TYPE_ALREADY_DEFINED, type_token);
        else super_generic_types.put(super_generic_type_name, type_token);
    }

// -------------------------------------- Errors ---------------------------------------------------------------------
    private boolean methodNameAlreadyDefined(String unitName) {
        return (methods.containsKey(unitName) || abstractMethods.containsKey(unitName));
    }

    private boolean classNameAlreadyDefined(String className) {
        return (generic_types.containsKey(className) ||
                super_generic_types.containsKey(className) ||
                Objects.equals(token.getLexeme(), className) ||
                Objects.equals(super_token.getLexeme(), className));
    }

    private void cyclicInheritance(Set<String> visited) throws SemanticException {
        if (Objects.equals(super_token.getLexeme(), Object.name)) return;

        if (visited.contains(super_token.getLexeme())) {
            SymbolTable.throwException(SemanticErrorMessages.CYCLIC_INHERITANCE, super_token);
        } else {
            visited.add(super_token.getLexeme());
            if (SymbolTable.hasClass(super_token.getLexeme()))
                SymbolTable.getClass(super_token.getLexeme()).cyclicInheritance(visited);
        }
    }

    private void superNotDeclared() throws SemanticException {
        if (!SymbolTable.hasClass(super_token.getLexeme()))
            SymbolTable.throwException(SemanticErrorMessages.CLASS_NOT_DECLARED, super_token);
    }
}