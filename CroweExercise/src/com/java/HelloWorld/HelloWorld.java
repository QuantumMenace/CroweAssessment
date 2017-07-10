package com.java.HelloWorld;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class HelloWorld {

	static HashMap<String, Method> commands;
	static String command;
	static Object[] arguments;

	public static void main(String[] args) {
		readFile();
		setupCommands();
		try {
			commands.get(command).invoke(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void readFile() {
		try (BufferedReader br = new BufferedReader(new FileReader("config.txt"))) {
			String line = br.readLine();

			while (line != null) {
				if (line.startsWith("output:")) {
					command = line.substring(7);
				} else if (line.startsWith("args:")) {
					String[] args = line.substring(5).split(",");
					arguments = new Object[args.length];
					for (int i = 0; i < args.length; i++) {
						arguments[i] = Array.get(args, i);
					}
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void setupCommands() {
		commands = new HashMap<>();
		try {
			commands.put("console", HelloWorld.class.getMethod("writeConsole"));
			commands.put("database", HelloWorld.class.getMethod("writeDB"));
		} catch (Exception e) {
			System.out.println("Error setting up map of methods");
			e.printStackTrace();
		}
	}

	public static void addCommand(String command, Method method) {
		commands.put(command, method);
	}

	public static void writeConsole() {
		System.out.println(arguments[0].toString());
	}

	public static void writeDB() {
		try {
			Connection conn = (Connection) arguments[0];
			String table = arguments[1].toString();
			Statement st = conn.createStatement();
			String query = "INSERT INTO " + table + " VALUES (";
			for (int i = 2; i < arguments.length; i++) {
				query = query + arguments[i].toString() + ", ";
			}
			query = query.substring(0, query.length() - 2) + ")";
			st.executeUpdate(query);
			conn.close();
		} catch (ClassCastException e) {
			System.out.println("Error converting class");
		} catch (SQLException e) {
			System.out.println("Error writing to database: ");
			e.printStackTrace();
		}
	}

}
