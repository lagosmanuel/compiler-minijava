package main.java.symboltable.entities;

import main.java.model.Token;

import main.java.model.TokenType;
import main.java.symboltable.SymbolTable;
import main.java.symboltable.entities.type.Type;
import main.java.symboltable.entities.type.TypeVar;
import main.java.symboltable.entities.ast.Block;
import main.java.symboltable.entities.predefined.Object;
import main.java.symboltable.entities.predefined.Wrapper;
import main.java.codegen.Instruction;
import main.java.codegen.Labeler;
import main.java.codegen.Comment;
import main.java.config.CodegenConfig;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Comparator;
import java.util.stream.Collectors;

import main.java.messages.SemanticErrorMessages;
import main.java.exeptions.SemanticException;

public class Class extends Entity {
    protected final Map<String, Method> methods = new HashMap<>();
    protected final Map<String, Constructor> constructors = new HashMap<>();
    protected final Map<String, AbstractMethod> abstractMethods = new HashMap<>();
    protected final Map<String, Attribute> attributes = new HashMap<>();
    protected final List<TypeVar> type_parameters = new ArrayList<>();

    protected final List<Attribute> own_attributes = new ArrayList<>();
    protected final List<Attribute> instance_attributes = new ArrayList<>();
    protected final List<Attribute> class_attributes = new ArrayList<>();
    protected final List<Method> static_methods_list = new ArrayList<>();
    protected final List<Method> dynamic_methods_list = new ArrayList<>();

    protected Type super_type = Object.type;
    protected boolean is_abstract;
    protected boolean is_consolidated;
    protected final String vt_label;

    public Class(String class_name, Token class_token, List<TypeVar> type_params) {
        super(class_name, class_token);
        type_params.forEach(this::addTypeParameter);
        this.vt_label = Labeler.getLabel(true, CodegenConfig.VT_FORMAT, class_name);
    }

    public Class(String class_name, Token class_token) {
        super(class_name, class_token);
        this.vt_label = Labeler.getLabel(true, CodegenConfig.VT_FORMAT, class_name);
    }

    @Override
    public void validate() throws SemanticException {
        if (isValidated()) return;
        super.validate();

        super_type.validate();
        cyclicInheritance(new HashSet<>(Set.of(getName())));

        for (Constructor constructor:constructors.values()) constructor.validate();
        for (Method method:methods.values()) method.validate();
        for (AbstractMethod abstractMethod:abstractMethods.values()) abstractMethod.validate();
        for (Attribute attribute:attributes.values()) attribute.validate();

        if (constructors.isEmpty()) {
            Constructor constructor = SymbolTable.getNewDefaultConstructor();
            constructor.validate();
            addConstructor(constructor);
        }
    }

    public void consolidate() {
        if (Objects.equals(name, Object.name) || is_consolidated) return;

        Class superClass = SymbolTable.getClass(super_type.getName());
        superClass = superClass == null? Object.Class():superClass;

        SymbolTable.actualClass = superClass;
        superClass.consolidate();
        SymbolTable.actualClass = this;

        inheritAttributes(superClass);
        inheritMethods(superClass);
        if(!isAbstract() && !abstractMethods.isEmpty()) abstractMethodsNotImplemented();

        setMethodsOffsets();
        setAttributesOffsets();

        is_consolidated = true;
    }

    public void check() throws SemanticException {
        for (Constructor constructor:constructors.values()) constructor.check();
        checkAttributeInitialization();
        for (Method method:methods.values()) method.check();
    }

    public void generate() {
        generateVT();
        alloc_attr_static();
        generateCode();
    }

    private void alloc_attr_static() {
        if (own_attributes.stream().noneMatch(Attribute::isStatic)) return;
        SymbolTable.getGenerator().write(CodegenConfig.DATA, Comment.CLASS_ATTR_STATIC_ALLOC.formatted(getName()));
        own_attributes.forEach(attribute -> {
            if (attribute.isStatic()) {
                SymbolTable.getGenerator().write(
                    Labeler.getLabel(CodegenConfig.LABEL, attribute.getLabel()),
                    Instruction.DW.toString(), "0",
                    Comment.ATTR_STATIC_ALLOC.formatted(attribute.getLabel())
                );
            }
        });
        SymbolTable.getGenerator().write(CodegenConfig.LINE_SEPARATOR);
    }

    public void init_attr_static() {
        own_attributes.forEach(attribute -> {
            if (attribute.isStatic() && attribute.getExpression() != null) {
                SymbolTable.getGenerator().write(
                    Instruction.PUSH.toString(),
                    attribute.getLabel(),
                    Comment.ATTRIBUTE_LOAD.formatted(attribute.getLabel())
                );
                attribute.getExpression().generate();
                Wrapper.wrap(attribute.getType());
                SymbolTable.getGenerator().write(
                    Instruction.STOREREF.toString(), "0",
                    Comment.ATTRIBUTE_STORE.formatted(attribute.getLabel())
                );
                SymbolTable.getGenerator().write(CodegenConfig.LINE_SPACE);
            }
        });
    }

    private void generateCode() {
        SymbolTable.getGenerator().write(CodegenConfig.CODE, Comment.CLASS_CODE.formatted(getName()));
        constructors.values().forEach(constructor -> {
            SymbolTable.actualUnit = constructor;
            constructor.generate();
        });
        methods.values().forEach(method -> {
            SymbolTable.actualUnit = method;
            method.generate();
        });
        SymbolTable.getGenerator().write(CodegenConfig.LINE_SEPARATOR);
    }

    private void generateVT() {
        SymbolTable.getGenerator().write(CodegenConfig.DATA, Comment.CLASS_VT.formatted(getName()));
        SymbolTable.getGenerator().write(
            Labeler.getLabel(CodegenConfig.LABEL, vt_label),
            Instruction.DW.toString(),
            dynamic_methods_list.stream()
                .filter(method -> !(method instanceof AbstractMethod))
                .sorted(Comparator.comparingInt(Method::getOffset))
                .map(Method::getLabel)
                .collect(Collectors.joining(", ")
            ) + (dynamic_methods_list.stream().allMatch(method -> method instanceof AbstractMethod)? "0":"")
        );
        SymbolTable.getGenerator().write(CodegenConfig.LINE_SEPARATOR);
    }

    public String getVTLabel() {
        return vt_label;
    }

    private void inheritAttributes(Class superClass) {
        superClass.getAttributes().forEach(this::addPublicAttribute);
        superClass.getInstanceAttributes().reversed().forEach(instance_attributes::addFirst);
    }

    private void inheritMethods(Class superClass) {
        superClass.getStaticMethods().reversed().forEach(this::inheritMethod);
        superClass.getDynamicMethods().reversed().forEach(this::inheritMethod);
    }

    private void inheritMethod(Method method) {
        if (!methods.containsKey(method.getName()) && !abstractMethods.containsKey(method.getName())) {
            if (!method.isPrivate())  {
                if (method instanceof AbstractMethod abstractMethod) abstractMethods.put(abstractMethod.getName(), abstractMethod);
                else methods.put(method.getName(), method);
            }
            if (!method.isStatic()) dynamic_methods_list.addFirst(method);
            else static_methods_list.addFirst(method);
        } else {
            Method redefined = methods.containsKey(method.getName())?
                methods.get(method.getName()):
                abstractMethods.get(method.getName());

            if (!redefined.isCompatible(method) && !method.isPrivate())
                SymbolTable.saveError(SemanticErrorMessages.METHOD_BAD_REDEFINED, redefined.getToken());

            if (!method.isPrivate() && !method.isStatic()) {
                dynamic_methods_list.remove(redefined);
                dynamic_methods_list.addFirst(redefined);
            } else if (!method.isStatic()) {
                dynamic_methods_list.addFirst(method);
            }
        }
    }

    public Type getSuperType() {
        return super_type;
    }

    public void setSuperType(Type super_type) {
        if (hasTypeParameter(super_type.getName()))
            SymbolTable.saveError(SemanticErrorMessages.SUPERCLASS_GENERIC_TYPE, super_type.getToken());
        else this.super_type = super_type;
    }

    public boolean isAbstract() {
        return is_abstract;
    }

    public void setAbstract() {
        this.is_abstract = true;
    }

// ---------------------------------------- Methods -------------------------------------------------------------------

    public List<Method> getStaticMethods() {
        return static_methods_list;
    }

    public List<Method> getDynamicMethods() {
        return dynamic_methods_list;
    }

    public boolean hasMethod(String method_name) {
        return methods.containsKey(method_name);
    }

    public Method getMethod(String method_name) {
        return methods.get(method_name);
    }

    public void addMethod(Method method) {
        if (method == null) return;
        if (methodNameAlreadyDefined(method.getName())) {
            SymbolTable.saveError(
                String.format(SemanticErrorMessages.METHOD_DUPLICATE, method.getToken().getLexeme()),
                method.getToken()
            );
        } else {
            methods.put(method.getName(), method);
            if (method.isStatic()) static_methods_list.addLast(method);
            else dynamic_methods_list.addLast(method);
        }
    }

// ------------------------------------- Constructors  ----------------------------------------------------------------

    public boolean hasConstructor(String constructor_name) {
        return constructors.containsKey(constructor_name);
    }

    public Constructor getConstructor(String constructor_name) {
        return constructors.get(constructor_name);
    }

    public void addConstructor(Constructor constructor) {
        if (constructor == null) return;
        if (constructors.containsKey(constructor.getName()))
            SymbolTable.saveError(SemanticErrorMessages.CONSTRUCTOR_DUPLICATE, constructor.getToken());
        else constructors.put(constructor.getName(), constructor);
    }

// -------------------------------------- Abstract Methods  -----------------------------------------------------------

    public Collection<AbstractMethod> getAbstractMethods() {
        return abstractMethods.values();
    }

    public AbstractMethod getAbstractMethod(String method_name) {
        return abstractMethods.get(method_name);
    }

    public void addAbstractMethod(AbstractMethod method) {
        if (method == null) return;
        if (methodNameAlreadyDefined(method.getName()))
            SymbolTable.saveError(
                String.format(SemanticErrorMessages.ABSTRACT_METHOD_DUPLICATE, method.getToken().getLexeme()),
                method.getToken()
            );
        else {
            abstractMethods.put(method.getName(), method);
            if (method.isStatic()) static_methods_list.addLast(method);
            else dynamic_methods_list.addLast(method);
        }
    }

    // ------------------------------------- Attributes  --------------------------------------------------------------

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public boolean hasAttribute(String attr_name) {
        return attributes.containsKey(attr_name);
    }

    public Attribute getAttribute(String attr_name) {
        return attributes.getOrDefault(attr_name, null);
    }


    public void addAttribute(Attribute attribute) {
        if (attribute == null) return;
        if (attributes.containsKey(attribute.getName()))
            SymbolTable.saveError(
                String.format(
                    SemanticErrorMessages.ATTRIBUTE_DUPLICATE,
                    attribute.getName()
                ),
                attribute.getToken()
            );
        else {
            own_attributes.addLast(attribute);
            attributes.put(attribute.getName(), attribute);
            if (attribute.isStatic()) class_attributes.addLast(attribute);
            else instance_attributes.addLast(attribute);
        }
    }

    public List<Attribute> getInstanceAttributes() {
        return instance_attributes;
    }

    public List<Attribute> getOwnAttributes() {
        return own_attributes;
    }

    private void addPublicAttribute(String attr_name, Attribute attribute) {
        if (!attribute.isPrivate() && !attributes.containsKey(attr_name))
            attributes.put(attr_name, attribute);
    }

    private void checkAttributeInitialization() throws SemanticException {
        SymbolTable.actualBlock = new Block(new Token(TokenType.leftBrace, "{", 0, 0));
        SymbolTable.actualUnit = new Method("", new Token(TokenType.idMetVar, "", 0, 0));

        for (Attribute attribute:own_attributes)
            attributes.remove(attribute.getName());

        for (Attribute attribute:own_attributes) {
            if (attribute.isStatic()) SymbolTable.actualUnit.setStatic();
            else SymbolTable.actualUnit.unsetStatic();

            if (attribute.getExpression() != null) {
                Type expressionType = attribute.getExpression().checkType();
                if (expressionType != null && !attribute.getType().compatible(expressionType)) {
                    SymbolTable.throwException(
                        String.format(
                            SemanticErrorMessages.TYPE_NOT_COMPATIBLE,
                            attribute.getType().getName(),
                            expressionType.getName()
                        ),
                        attribute.getToken()
                    );
                }
            }

            attributes.put(attribute.getName(), attribute);
        }

        SymbolTable.actualBlock = null;
        SymbolTable.actualUnit = null;
    }

// ------------------------------------- Generics --------------------------------------------------------------------

    public List<TypeVar> getTypeParameters() {
       return type_parameters;
    }


    public boolean hasTypeParameter(String type_param_name) {
        return type_parameters.stream().anyMatch(type_var -> Objects.equals(type_var.getName(), type_param_name));
    }

    public int getTypeParametersCount() {
        return type_parameters.size();
    }

    public TypeVar getTypeParameter(String type_param_name) {
        return type_parameters.stream().filter(type_var -> Objects.equals(type_var.getName(), type_param_name))
            .findFirst()
            .orElse(null);
    }

    public TypeVar getTypeParameter(int i) {
        return i >= 0 && i < type_parameters.size()? type_parameters.get(i):null;
    }

    public void addTypeParameter(TypeVar type_var) {
        if (hasTypeParameter(type_var.getName()))
            SymbolTable.saveError(
                String.format(
                    SemanticErrorMessages.GENERIC_TYPE_DUPLICATE,
                    type_var.getName()
                ),
                type_var.getToken()
            );
        else type_parameters.add(type_var);
    }

// -------------------------------------- Errors ---------------------------------------------------------------------
    private boolean methodNameAlreadyDefined(String methodName) {
        return (methods.containsKey(methodName) || abstractMethods.containsKey(methodName));
    }

    private void cyclicInheritance(Set<String> visited) throws SemanticException {
        if (Objects.equals(super_type.getName(), Object.type.getName())) return;

        if (visited.contains(super_type.getName())) {
            SymbolTable.throwException(SemanticErrorMessages.CYCLIC_INHERITANCE, super_type.getToken());
        } else {
            visited.add(super_type.getName());
            if (SymbolTable.hasClass(super_type.getName()))
                SymbolTable.getClass(super_type.getName()).cyclicInheritance(visited);
        }
    }

    private void abstractMethodsNotImplemented() {
        abstractMethods.values().forEach(method -> SymbolTable.saveError(
            String.format(
                SemanticErrorMessages.ABSTRACT_METHOD_NOT_IMPLEMENTED,
                this.getName(),
                method.getToken().getLexeme()
            ),
            this.getToken()
        ));
    }

    private void setMethodsOffsets() {
        for (int i = 0; i < dynamic_methods_list.size(); ++i) {
            dynamic_methods_list.get(i).setOffset(i);
        }
    }

    private void setAttributesOffsets() {
        for (int i = 0; i < instance_attributes.size(); ++i)
            instance_attributes.get(i).setOffset(i+1);
    }
}
