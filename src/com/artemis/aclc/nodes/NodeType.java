package com.artemis.aclc.nodes;

//@SuppressWarnings("unused")
public enum NodeType {
    Program,
    Library,

    // Statements
    ReturnStatement,            // return x + y;

    // Declarations
    VariableDeclaration,        // let x: short = 5;
    MethodDeclaration,          // def add(x: int, y: int): int { return x + y; }
    ClassDeclaration,           // class person {}

    // Expressions
    CallExpression,             // x / add(y, z)
    UnaryExpression,            // ++x
    BinaryExpression,           // x + y
    ComparativeExpression,      // x > y

}
