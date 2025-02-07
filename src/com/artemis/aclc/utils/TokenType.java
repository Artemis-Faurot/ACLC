package com.artemis.aclc.utils;

public enum TokenType {
    // Keywords
    Library,
    Class,
    Private,
    Public,

    Let,
    Def,

    If,
    While,
    For,

    Exit,
    Print,
    Return,

    This,

    // Literals
    Identifier,             // MyVariable, MyClass, MyMethod
    ShortLiteral,           // -32,768 to 32,767
    IntegerLiteral,         // -2,147,483,648 to 2,147,483,647
    LongLiteral,            // -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807
    FloatLiteral,           // 6 decimal places
    DoubleLiteral,          // 15 decimal places

    CharLiteral,            // 'A'
    StringLiteral,          // "Hello, World!"

    BooleanLiteral,         // True | False

    DataType,               // short, int, long, float, double, char, string, bool

    // Operators & Symbols
    Equals,                 // =

    UnaryOperator,          // -x, +x, !x, &x, *x, +=x, -=x, *=x, /=x, ^=x, %=x, ++x, --x
    BinaryOperator,         // x+x, x-x, x*x, x/x, x^x, x%x
    ComparativeOperator,    // x<x, x>x, x==x, x<=x, x>=x, x!=x, x&&x, x||x

    OpenParen,              // (
    CloseParen,             // )
    OpenBracket,            // [
    CloseBracket,           // ]
    OpenBrace,              // {
    CloseBrace,             // }

    Comma,                  // ,
    Dot,                    // .
    Colon,                  // :
    SemiColon,               // ;

    EOF
}
