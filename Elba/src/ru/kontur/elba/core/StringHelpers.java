package ru.kontur.elba.core;

public class StringHelpers {
	public static String join(String delimeter, Iterable<String> args) {
		StringBuilder result = new StringBuilder();
		for (String s : args)
			result.append(s + delimeter);
		return result.toString().substring(0, result.length() - delimeter.length());
	}
}
