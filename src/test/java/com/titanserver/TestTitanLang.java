package com.titanserver;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import com.titanserver.engine.lang.ArrayInitLexer;
import com.titanserver.engine.lang.ArrayInitParser;

public class TestTitanLang {

	@Test
	public void testAntlr() {
		try {
			System.out.println("test la");
			String inputFile = "lang-examples/ArrayInit.txt";
			InputStream is = new FileInputStream(inputFile);
			ANTLRInputStream input = new ANTLRInputStream(is);
			ArrayInitLexer lexer = new ArrayInitLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			ArrayInitParser parser = new ArrayInitParser(tokens);
			ParseTree tree = parser.init();
			System.out.println(tree.toStringTree());
		} catch (Exception ex) {
			ex.printStackTrace();
			assertTrue(false);
		} finally {
			assertTrue(true);
		}
	}
}