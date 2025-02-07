package com.artemis.aclc;

import com.artemis.aclc.utils.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if(args.length != 1) throw new IllegalArgumentException("Improper use of compiler");
        String filepath = args[0];
        StringBuilder contents = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while((line = br.readLine()) != null) {
                contents.append(line).append("\n");
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        Lexer lexer = new Lexer(contents.toString());
        List<Token> tokens = lexer.runLexer();

        try {
            ObjectMapper tokenMapper = new ObjectMapper();
            tokenMapper.enable(SerializationFeature.INDENT_OUTPUT);
            tokenMapper.writeValue(new File("tokens.json"), tokens);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
