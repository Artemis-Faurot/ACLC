package com.artemis.aclc.nodes;

import com.artemis.aclc.utils.Token;
import java.util.List;

@SuppressWarnings("unused")
public class Node {
    private NodeType nodeType;

    public Node() {}
    public Node(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public static class Program extends Node {
        private List<Statement> body;

        public Program() {}
        public Program(List<Statement> body) {
            super(NodeType.Program);
            this.body = body;
        }

        public void setBody(List<Statement> body) {
            this.body = body;
        }

        public List<Statement> getBody() {
            return body;
        }
    }

    public static class Library extends Node {
        private List<Declaration> body;

        public Library() {}
        public Library(List<Declaration> body) {
            super(NodeType.Library);
            this.body = body;
        }

        public void setBody(List<Declaration> body) {
            this.body = body;
        }

        public List<Declaration> getBody() {
            return body;
        }
    }

    public static class Statement extends Node {
        public Statement() {}
        public Statement(NodeType nodeType) {
            super(nodeType);
        }
    }

    public static class Declaration extends Statement {
        private Token name;

        public Declaration() {}
        public Declaration(NodeType nodeType, Token name) {
            super(nodeType);
            this.name = name;
        }

        public void setName(Token name) {
            this.name = name;
        }

        public Token getName() {
            return name;
        }
    }

    public static class Expression extends Node {
        public Expression() {}
        public Expression(NodeType nodeType) {
            super(nodeType);
        }
    }

    public static class VariableDeclaration extends Declaration {
        private Token dataType;
        private Expression value;

        public VariableDeclaration() {}
        public VariableDeclaration(Token name, Token dataType, Expression value) {
            super(NodeType.VariableDeclaration, name);
            this.dataType = dataType;
            this.value = value;
        }

        public void setDataType(Token dataType) {
            this.dataType = dataType;
        }
        public void setValue(Expression value) {
            this.value = value;
        }

        public Token getDataType() {
            return dataType;
        }
        public Expression getValue() {
            return value;
        }
    }

    public static class CallExpression extends Expression {
        private Token callee;

        public CallExpression() {}
        public CallExpression(Token callee) {
            super(NodeType.CallExpression);
            this.callee = callee;
        }

        public void setCallee(Token callee) {
            this.callee = callee;
        }

        public Token getCallee() {
            return callee;
        }
    }

    public static class UnaryExpression extends Expression {
        private Token unaryOperator;
        private Expression expression;

        public UnaryExpression() {}
        public UnaryExpression(Token unaryOperator, Expression expression) {
            super(NodeType.UnaryExpression);
            this.unaryOperator = unaryOperator;
            this.expression = expression;
        }

        public void setUnaryOperator(Token unaryOperator) {
            this.unaryOperator = unaryOperator;
        }
        public void setExpression(Expression expression) {
            this.expression = expression;
        }

        public Token getUnaryOperator() {
            return unaryOperator;
        }
        public Expression getExpression() {
            return expression;
        }
    }
}
