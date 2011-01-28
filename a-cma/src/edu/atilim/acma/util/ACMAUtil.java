package edu.atilim.acma.util;

public final class ACMAUtil {
	public static String splitCamelCase(String in) {
		StringBuilder sb = new StringBuilder();
		
		for (char c : in.toCharArray()) {
			if (c >= 'A' && c <= 'Z' && sb.length() > 0)
				sb.append(' ');
			sb.append(c);
		}
		
		return sb.toString();
	}
}
