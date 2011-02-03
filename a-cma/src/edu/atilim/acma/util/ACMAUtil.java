package edu.atilim.acma.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

public final class ACMAUtil {
	public static final Random RANDOM = new Random();
	
	public static String splitCamelCase(String in) {
		StringBuilder sb = new StringBuilder();
		
		for (char c : in.toCharArray()) {
			if (c >= 'A' && c <= 'Z' && sb.length() > 0)
				sb.append(' ');
			sb.append(c);
		}
		
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T deepCopy(T item) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(item);
			oos.flush();
			
			byte[] rawData = baos.toByteArray();
			
			ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (T)ois.readObject();
		} catch(Exception e) {
		}
		
		return null;
	}
}
