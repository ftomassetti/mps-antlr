package me.tomasetti.mpsantlr.parser;

import junit.framework.Assert;
import me.tomassetti.antlr4.parser.ANTLRv4Parser;
import org.antlr.v4.parse.ANTLRLexer;
import org.antlr.v4.parse.ANTLRParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class Antlr4ParserFacadeTest {

    @Test
    public void parseEmptyLexer() {
        String code = "lexer grammar LexBasic;";
        Antlr4ParserFacade facade = new Antlr4ParserFacade();
        ANTLRv4Parser.GrammarSpecContext root = facade.parseString(code);
        assertEquals("lexer", new ParseTreeWrapper(root).byType("grammarType").terminal(0));
        assertEquals("LexBasic", new ParseTreeWrapper(root).byType("identifier").terminal(0));
    }

    @Test
    public void parseVerySimpleLexer() {
        String code = "lexer grammar LexBasic;\nINT: DecimalNumeral\n;\n";
        Antlr4ParserFacade facade = new Antlr4ParserFacade();
        ANTLRv4Parser.GrammarSpecContext root = facade.parseString(code);
        assertEquals("lexer", new ParseTreeWrapper(root).byType("grammarType").terminal(0));
        assertEquals("LexBasic", new ParseTreeWrapper(root).byType("identifier").terminal(0));
    }

    class ParseTreeWrapper {
        private ParseTree parseTree;

        public ParseTreeWrapper(ParseTree parseTree) {
            this.parseTree = parseTree;
        }

        public ParseTreeWrapper byType(String type) {
            if (parseTree instanceof ParserRuleContext) {
                ParserRuleContext parserRuleContext = (ParserRuleContext) parseTree;
                for (int i = 0; i < parseTree.getChildCount(); i++) {
                    if (parseTree.getChild(i) instanceof ParserRuleContext) {
                        ParserRuleContext child = (ParserRuleContext) parseTree.getChild(i);
                        if (ANTLRv4Parser.ruleNames[child.getRuleIndex()].equals(type)) {
                            return new ParseTreeWrapper(child);
                        }
                    }

                }
                fail("Child of type " + type + " not found");
                return null;
            } else {
                throw new UnsupportedOperationException();
            }
        }

        public String terminal(int index) {
            if (parseTree instanceof ParserRuleContext) {
                ParserRuleContext parserRuleContext = (ParserRuleContext) parseTree;
                return ((TerminalNode)parserRuleContext.getChild(index)).getText();
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    @Test
    public void parseSimpleParser() {
        String code = "parser grammar ANTLRv4Parser;\n" +
                "\n" +
                "options {\n" +
                "\ttokenVocab = ANTLRv4Lexer ;\n" +
                "}\n" +
                "\n" +
                "// The main entry point for parsing a v4 grammar.\n" +
                "grammarSpec\n" +
                "\t:\tDOC_COMMENT?\n" +
                "\t\tgrammarType identifier SEMI\n" +
                "\t\tprequelConstruct*\n" +
                "\t\trules\n" +
                "\t\tmodeSpec*\n" +
                "\t\tEOF\n" +
                "\t;";
        Antlr4ParserFacade facade = new Antlr4ParserFacade();
        ANTLRv4Parser.GrammarSpecContext root = facade.parseString(code);
        assertEquals("parser", new ParseTreeWrapper(root).byType("grammarType").terminal(0));
        assertEquals("ANTLRv4Parser", new ParseTreeWrapper(root).byType("identifier").terminal(0));
    }

}
