package me.tomasetti.mpsantlr.parser;

import me.tomassetti.antlr4.parser.ANTLRv4Lexer;
import me.tomassetti.antlr4.parser.ANTLRv4Parser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Antlr4ParserFacade {

    public ANTLRv4Parser.GrammarSpecContext parseString(String code) {
        InputStream inputStream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
        try {
            ANTLRv4Lexer lexer = new ANTLRv4Lexer(new org.antlr.v4.runtime.ANTLRInputStream(inputStream));
            TokenStream tokens = new CommonTokenStream(lexer);
            ANTLRv4Parser parser = new ANTLRv4Parser(tokens);
            return parser.grammarSpec();
        } catch (IOException e) {
            throw new RuntimeException("That is unexpected", e);
        }
    }

}
