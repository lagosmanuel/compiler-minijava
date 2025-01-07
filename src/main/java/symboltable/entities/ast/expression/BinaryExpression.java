package main.java.symboltable.entities.ast.expression;

import main.java.exeptions.SemanticException;
import main.java.messages.SemanticErrorMessages;
import main.java.messages.CodegenErrorMessages;
import main.java.model.Token;
import main.java.model.TokenType;
import main.java.symboltable.SymbolTable;
import main.java.symboltable.entities.type.Type;
import main.java.symboltable.entities.type.PrimitiveType;
import main.java.codegen.Instruction;
import main.java.codegen.Comment;
import main.java.codegen.Labeler;
import main.java.config.CodegenConfig;

public class BinaryExpression extends CompositeExpression {
    private final CompositeExpression left;
    private final BasicExpression right;
    private final Token operator;

    public BinaryExpression(CompositeExpression left, BasicExpression right, Token operator) {
        super(operator);
        this.left = left;
        this.right = right;
        this.operator = operator;
    }
    
    @Override
    public Type checkType() throws SemanticException {
        Type leftType = left.checkType();
        Type rightType = right.checkType();

        return switch (operator.getType()) {
            case opPlus, opMinus, opTimes, opDiv, opMod -> {
                leftType = leftType != null? leftType.convertNumeric():null;
                rightType = rightType != null? rightType.convertNumeric():null;

                if (leftType != null && rightType != null && leftType.isNumeric() && rightType.isNumeric()) {
                    yield leftType.isFloat()? leftType:rightType;
                } else {
                    SymbolTable.throwException(
                        String.format(
                            SemanticErrorMessages.BINARY_OPERAND_NOT_NUMERIC,
                            operator.getLexeme(),
                            leftType != null? leftType.getName():"null",
                            rightType != null? rightType.getName():"null"
                        ),
                        getIdentifier()
                    );
                    yield null;
                }
            }
            case opAnd, opOr -> {
                if (leftType != null && rightType != null && leftType.isBoolean() && rightType.isBoolean()) {
                    yield leftType;
                } else {
                    SymbolTable.throwException(
                        String.format(
                            SemanticErrorMessages.BINARY_OPERAND_NOT_BOOLEAN,
                            operator.getLexeme(),
                            leftType != null? leftType.getName():"null",
                            rightType != null? rightType.getName():"null"
                        ),
                        getIdentifier()
                    );
                    yield null;
                }
            }
            case opEqual, opNotEqual -> {
                if (leftType != null && rightType != null && (leftType.compatible(rightType) || rightType.compatible(leftType))) {
                    yield Type.createType(
                        new Token(TokenType.kwBoolean, PrimitiveType.BOOLEAN, 0, 0),
                        null
                    );
                } else {
                    SymbolTable.throwException(
                        String.format(
                            SemanticErrorMessages.BINARY_OPERAND_NOT_COMPATIBLE,
                            operator.getLexeme(),
                            leftType != null? leftType.getName():"null",
                            rightType != null? rightType.getName():"null"
                        ),
                        getIdentifier()
                    );
                    yield null;
                }
            }
            case opLess, opGreater, opLessEqual, opGreaterEqual -> {
                if (leftType!= null) leftType = leftType.convertNumeric();
                if (rightType != null) rightType = rightType.convertNumeric();

                if (leftType != null && rightType != null && leftType.isNumeric() && rightType.isNumeric()) {
                    yield Type.createType(
                            new Token(TokenType.kwBoolean, PrimitiveType.BOOLEAN, 0, 0),
                            null
                    );
                } else {
                    SymbolTable.throwException(
                        String.format(
                            SemanticErrorMessages.BINARY_OPERAND_NOT_NUMERIC,
                            operator.getLexeme(),
                            leftType != null? leftType.getName():"null",
                            rightType != null? rightType.getName():"null"
                        ),
                        getIdentifier()
                    );
                    yield null;
                }
            }
            default -> {
                SymbolTable.throwException(
                    String.format(
                        SemanticErrorMessages.BINARYOP_INVALID,
                        operator.getLexeme()
                    ),
                    getIdentifier()
                );
                yield null;
            }
        };
    }

    @Override
    public void generate() {
        if (left == null || right == null || operator == null) return;

        left.generate();

        switch (operator.getType()) {
            case opAnd -> {
                String labelEnd = Labeler.getLabel(true, CodegenConfig.AND_END);
                SymbolTable.getGenerator().write(
                    Instruction.DUP.toString()
                );
                SymbolTable.getGenerator().write(
                    Instruction.BF.toString(), labelEnd
                );
                right.generate();
                SymbolTable.getGenerator().write(
                    Instruction.AND.toString(),
                    Comment.OP_BINARY.formatted(operator.getLexeme())
                );
                SymbolTable.getGenerator().write(
                    Labeler.getLabel(CodegenConfig.LABEL, labelEnd),
                    Instruction.NOP.toString()
                );
                return;
            }
            case opOr -> {
                String labelEnd = Labeler.getLabel(true, CodegenConfig.OR_END);
                SymbolTable.getGenerator().write(
                    Instruction.DUP.toString()
                );
                SymbolTable.getGenerator().write(
                    Instruction.BT.toString(), labelEnd
                );
                right.generate();
                SymbolTable.getGenerator().write(
                    Instruction.OR.toString(),
                    Comment.OP_BINARY.formatted(operator.getLexeme())
                );
                SymbolTable.getGenerator().write(
                    Labeler.getLabel(CodegenConfig.LABEL, labelEnd),
                    Instruction.NOP.toString()
                );
                return;
            }
        }

        right.generate();
        switch (operator.getType()) {
            case opPlus -> SymbolTable.getGenerator().write(
                Instruction.ADD.toString(),
                Comment.OP_BINARY.formatted(operator.getLexeme())
            );
            case opMinus -> SymbolTable.getGenerator().write(
                Instruction.SUB.toString(),
                Comment.OP_BINARY.formatted(operator.getLexeme())
            );
            case opTimes -> SymbolTable.getGenerator().write(
                Instruction.MUL.toString(),
                Comment.OP_BINARY.formatted(operator.getLexeme())
            );
            case opDiv -> SymbolTable.getGenerator().write(
                Instruction.DIV.toString(),
                Comment.OP_BINARY.formatted(operator.getLexeme())
            );
            case opMod -> SymbolTable.getGenerator().write(
                Instruction.MOD.toString(),
                Comment.OP_BINARY.formatted(operator.getLexeme())
            );
            case opEqual -> SymbolTable.getGenerator().write(
                Instruction.EQ.toString(),
                Comment.OP_BINARY.formatted(operator.getLexeme())
            );
            case opNotEqual -> SymbolTable.getGenerator().write(
                Instruction.NE.toString(),
                Comment.OP_BINARY.formatted(operator.getLexeme())
            );
            case opLess -> SymbolTable.getGenerator().write(
                Instruction.LT.toString(),
                Comment.OP_BINARY.formatted(operator.getLexeme())
            );
            case opGreater -> SymbolTable.getGenerator().write(
                Instruction.GT.toString(),
                Comment.OP_BINARY.formatted(operator.getLexeme())
            );
            case opLessEqual -> SymbolTable.getGenerator().write(
                Instruction.LE.toString(),
                Comment.OP_BINARY.formatted(operator.getLexeme())
            );
            case opGreaterEqual -> SymbolTable.getGenerator().write(
                Instruction.GE.toString(),
                Comment.OP_BINARY.formatted(operator.getLexeme())
            );
            default -> throw new RuntimeException(
                CodegenErrorMessages.INVALID_BINARY_OP.formatted(operator.getLexeme())
            );
        }
    }
}
