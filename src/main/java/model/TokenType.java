package main.java.model;

public enum TokenType {
    idMetVar,
    idClassVar,
    intLiteral,
    floatLiteral,
    charLiteral,
    stringLiteral,
    trueLiteral,
    falseLiteral,
    nullLiteral,
    opGreater,
    opLess,
    opGreaterEqual,
    opLessEqual,
    opEqual,
    opNotEqual,
    opPlus,
    opPlusAssign,
    opMinus,
    opMinusAssign,
    opTimes,
    opDiv,
    opAnd,
    opOr,
    opNot,
    opMod,
    kwClass,
    kwBoolean,
    kwIf,
    kwSwitch,
    kwThis,
    kwExtends,
    kwChar,
    kwElse,
    kwCase,
    kwNew,
    kwPublic,
    kwInt,
    kwFloat,
    kwWhile,
    kwBreak,
    kwStatic,
    kwReturn,
    kwVoid,
    kwVar,
    EOF,
    colon,
    semicolon,
    comma,
    dot,
    leftParenthesis,
    rightParenthesis,
    leftBrace,
    rightBrace,
    opAssign,
}