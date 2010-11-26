package edu.atilim.acma.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public final class Log {
	private static Logger logger = Logger.getLogger("a-cma");
	
	static {
		logger.setUseParentHandlers(false);
	}
	
	public static void info(String info)
	{
		logger.info(info);
	}
	
	public static void info(String format, Object... args)
	{
		info(String.format(format, args));
	}
	
	public static void warning(String info)
	{
		logger.warning(info);
	}
	
	public static void warning(String format, Object... args)
	{
		warning(String.format(format, args));
	}
	
	public static void addOutput(OutputStream stream) {
		StreamHandler sh = new StreamHandler(stream, new SimpleFormatter());
		logger.addHandler(sh);
	}
	
	public static void addOutput(String filename) {
		try {
			FileHandler fh = new FileHandler(filename);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
