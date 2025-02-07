package com.artemis.aclc;

import com.artemis.aclc.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    public static class LexingException extends RuntimeException {
        public LexingException(String message) { super(message); }
        public LexingException(char character) { super("Unrecognized character found while lexing: " + character); }
    }

    public static boolean isAlnum(char character) { return Character.isAlphabetic(character) || Character.isDigit(character) || character == '_'; }

    private static final Map<String, TokenType> keywords = new HashMap<>();
    private static final Map<String, TokenType> unaryOperators = new HashMap<>();
    private static final Map<String, TokenType> binary_comparativeOperators = new HashMap<>();
    private static final Map<Character, TokenType> symbols = new HashMap<>();

    private static final List<String> booleanValues = new ArrayList<>();
    private static final List<String> dataTypes = new ArrayList<>();

    static {
        final List<String> keywordStrings = List.of("Library", "class", "Private", "Public", "let",
                "def", "if", "while", "for", "exit", "print", "return", "this");

        final List<String> unaryOperatorStrings = List.of("-", "+", "!", "&", "*", "+=", "-=", "*=", "/=",
                "^=", "%=", "++", "--");

        final List<String> binary_comparativeOperatorStrings = List.of("+", "-", "*", "/", "^", "%", "<",
                ">", "==", "<=", ">=", "!=", "&&", "||");

        final List<Character> symbolStrings = List.of('(', ')', '[', ']', '{', '}', ',', '.', ':', ';', '=');

        final List<TokenType> keywordTypes = List.of(TokenType.Library, TokenType.Class, TokenType.Private,
                TokenType.Public, TokenType.Let, TokenType.Def, TokenType.If, TokenType.While, TokenType.For,
                TokenType.Exit, TokenType.Print, TokenType.Return, TokenType.This);

        final List<TokenType> binary_comparativeOperatorTypes = List.of(TokenType.BinaryOperator,
                TokenType.BinaryOperator, TokenType.BinaryOperator, TokenType.BinaryOperator, TokenType.BinaryOperator,
                TokenType.BinaryOperator, TokenType.ComparativeOperator, TokenType.ComparativeOperator,
                TokenType.ComparativeOperator, TokenType.ComparativeOperator, TokenType.ComparativeOperator,
                TokenType.ComparativeOperator, TokenType.ComparativeOperator, TokenType.ComparativeOperator);

        final List<TokenType> symbolTypes = List.of(TokenType.OpenParen, TokenType.CloseParen, TokenType.OpenBracket,
                TokenType.CloseBracket, TokenType.OpenBrace, TokenType.CloseBrace, TokenType.Comma, TokenType.Dot,
                TokenType.Colon, TokenType.SemiColon, TokenType.Equals);

        final List<String> booleanStrings = List.of("True", "true", "False", "false");

        final List<String> dataTypeStrings = List.of("short", "int", "long", "float", "double", "char", "string", "boolean");

        for(String string : keywordStrings) {
            keywords.put(string, keywordTypes.get(keywordStrings.indexOf(string)));
        }

        for(String string : unaryOperatorStrings) {
            unaryOperators.put(string, TokenType.UnaryOperator);
        }

        for(String string : binary_comparativeOperatorStrings) {
            binary_comparativeOperators.put(string, binary_comparativeOperatorTypes.get(binary_comparativeOperatorStrings.indexOf(string)));
        }

        for(Character character : symbolStrings) {
            symbols.put(character, symbolTypes.get(symbolStrings.indexOf(character)));
        }

        booleanValues.addAll(booleanStrings);
        dataTypes.addAll(dataTypeStrings);
    }

    private String src;

    private int index = 0;
    private int currentLine = 1;
    private int currentColumn = 0;
    private int holdPastColumn;
    private String buffer = "";

    private List<Token> tokens = new ArrayList<>();

    public Lexer() {}
    public Lexer(String src) { this.src = src; }

    private char peek(int offset) {
        int position = index + offset;
        if(position < src.length()) return src.charAt(position);
        return '\0';
    }

    private char consume(int amount) {
        char returnCharacter = '\0';
        for(int i = 0; i < amount; ++i) {
            if(peek(0) == '\n') {
                ++currentLine;
                holdPastColumn = currentColumn;
                currentColumn = 0;
            } else ++currentColumn;

            if(i == amount - 1) returnCharacter = src.charAt(index);
            ++index;

            if(index >= src.length()) return '\0';
        }

        return returnCharacter;
    }

    private char peek() { return this.peek(0); }
    private char consume() { return this.consume(1); }

    private void add_token(TokenType type, String value, Position[] locationPositions, int[] range) {
        Token token = new Token(type, value, locationPositions);
        token.setRange(range[0], range[1]);
        tokens.add(token);
    }

    private void add_token(TokenType type, Position[] locationPositions, int[] range) {
        add_token(type, "", locationPositions, range);
    }

    private void lexAlpha() {
        Position[] TokenLocation = new Position[2];
        int[] TokenRange = new int[2];

        TokenLocation[0].setLine(currentLine);
        TokenLocation[0].setColumn(currentColumn);
        TokenRange[0] = index;

        while(isAlnum(peek())) buffer += String.valueOf(peek());

        if(currentColumn == 0) {
            TokenLocation[1].setLine(currentLine - 1);
            TokenLocation[1].setColumn(holdPastColumn);
        } else {
            TokenLocation[1].setLine(currentLine);
            TokenLocation[1].setColumn(currentColumn - 1);
        }

        TokenRange[1] = index - 1;

        if(keywords.containsKey(buffer)) add_token(keywords.get(buffer), TokenLocation, TokenRange);
        else if(booleanValues.contains(buffer)) add_token(TokenType.BooleanLiteral, buffer, TokenLocation, TokenRange);
        else if((dataTypes.contains(buffer))) add_token(TokenType.DataType, buffer, TokenLocation, TokenRange);
        else add_token(TokenType.Identifier, buffer, TokenLocation, TokenRange);

        buffer = "";
    }

    private void lexDigit() {
        Position[] TokenLocation = new Position[2];
        int[] TokenRange = new int[2];

        TokenLocation[0].setLine(currentLine);
        TokenLocation[0].setColumn(currentColumn);
        TokenRange[0] = index;

        String numberStatus = "";
        boolean isFloat = false;
        boolean isDouble = false;
        int decimalCount = 0;

        while(Character.isDigit(peek()) || peek() == '.') {
            if(isFloat) ++decimalCount;

            if(peek() == '.' && !isFloat) isFloat = true;
            else if (peek() == '.' && isFloat) throw new LexingException("Floating point number may only have one decimal point");

            if(decimalCount == 7) isDouble = true;
            else if(decimalCount == 16) throw new LexingException("Double value is too large");

            buffer += String.valueOf(this.consume());
        }

        long number = Integer.parseInt(buffer);

        if(number > -32768 && number < 32767) numberStatus = "short";
        else if(number > -2147483648 && number < 2147483647) numberStatus = "int";
        else if(number > -9223372036854775808L && number < 9223372036854775807L) numberStatus = "long";
        else throw new LexingException("Number value too large");

        if(isDouble) numberStatus = "double";
        else if(isFloat) numberStatus = "float";

        if(currentColumn == 0) {
            TokenLocation[1].setLine(currentLine - 1);
            TokenLocation[1].setColumn(holdPastColumn);
        } else {
            TokenLocation[1].setLine(currentLine);
            TokenLocation[1].setColumn(currentColumn - 1);
        }

        TokenRange[1] = index - 1;

        switch(numberStatus) {
            case "short" -> add_token(TokenType.ShortLiteral, buffer, TokenLocation, TokenRange);
            case "int" -> add_token(TokenType.IntegerLiteral, buffer, TokenLocation, TokenRange);
            case "long" -> add_token(TokenType.LongLiteral, buffer, TokenLocation, TokenRange);
            case "float" -> add_token(TokenType.FloatLiteral, buffer, TokenLocation, TokenRange);
            case "double" -> add_token(TokenType.DoubleLiteral, buffer, TokenLocation, TokenRange);
            default -> throw new LexingException("Major internal error with digit lexing !");
        }

        buffer = "";
    }

    private void lexCharacter() {
        Position[] TokenLocation = new Position[2];
        int[] TokenRange = new int[2];

        TokenLocation[0].setLine(currentLine);
        TokenLocation[0].setColumn(currentColumn);
        TokenRange[0] = index;

        consume();
        if(peek() != '\0' && peek() != '\'') buffer = String.valueOf(consume());
        else throw new LexingException("Char variable must contain a value");

        if(peek() != '\'') throw new LexingException("Expected ' to end char value");

        if(currentColumn == 0) {
            TokenLocation[1].setLine(currentLine - 1);
            TokenLocation[1].setColumn(holdPastColumn);
        } else {
            TokenLocation[1].setLine(currentLine);
            TokenLocation[1].setColumn(currentColumn - 1);
        }

        TokenRange[1] = index - 1;

        this.add_token(TokenType.CharLiteral, buffer, TokenLocation, TokenRange);

        buffer = "";
    }

    private void lexString() {}

    private void lexComment() {}

    private void lexLineComment() {}

    private void lexBlockComment() {}

    private void lexSymbol() {}

    private void lexUnarySymbol() {}

    private void lexBinarySymbol() {}

    private void lexBaseSymbol() {}

    private void lex() {
        if(Character.isAlphabetic(peek()) || peek() == '_') lexAlpha();
        else if(Character.isDigit(peek())) lexDigit();
        else if(peek() == '\'') lexCharacter();
        else if(peek() == '"') lexString();
        else if(peek() == '/') lexComment();
        else if(symbols.containsKey(peek()) ||
            unaryOperators.containsKey(String.valueOf(peek())) ||
            binary_comparativeOperators.containsKey(String.valueOf(peek()))) lexSymbol();
        else if(Character.isWhitespace(peek())) consume();
        else throw new LexingException(peek());
    }

    public List<Token> runLexer() {
        while(peek() != '\0') { lex(); }

        Position[] positions = new Position[2];
        int[] range = new int[2];

        positions[0].setLine(currentLine);
        positions[1].setLine(currentLine);
        positions[0].setColumn(currentColumn);
        positions[1].setColumn(currentColumn);

        range[0] = range[1] = index;

        add_token(TokenType.EOF, positions, range);

        return tokens;
    }
}
