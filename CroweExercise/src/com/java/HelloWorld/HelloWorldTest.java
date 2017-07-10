package com.java.HelloWorld;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class HelloWorldTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}

	public void setDatabase() throws Exception {

		String str = "output:database\nargs:connection,table,values";
		BufferedWriter writer = new BufferedWriter(new FileWriter("config.txt"));
		writer.write(str);

		writer.close();
	}

	@Test
	public void expectDatabaseError() throws Exception {
		setDatabase();
		HelloWorld.main(null);
		assertEquals("Error converting class\r\n", outContent.toString());
	}

	@Test
	public void testPrintToConsole() throws Exception {
		setConsole("Hello World");
		HelloWorld.main(null);
		assertEquals("Hello World\r\n", outContent.toString());
	}

	public void setConsole(String s) throws Exception {
		String str = "output:console\nargs:" + s;
		BufferedWriter writer = new BufferedWriter(new FileWriter("config.txt"));
		writer.write(str);

		writer.close();

	}

}
