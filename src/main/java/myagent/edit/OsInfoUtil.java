package myagent.edit;

import java.io.File;

public class OsInfoUtil {
	private static String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isLinux() {
		return OS.indexOf("linux") >= 0;
	}

	public static boolean isMacOS() {
		return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") < 0;
	}

	public static boolean isMacOSX() {
		return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") > 0;
	}

	public static boolean isWindows() {
		return OS.indexOf("windows") >= 0;
	}

	public static String getTempDir() {
		if (isLinux()) {
			return "/tmp";
		} else if (isMacOS() || isMacOSX()) {
			return "/tmp";
		} else {
			String dir = "d:/tmp";
			File file = new File(dir);
			if (file.exists()) {
				file.mkdir();
			}
			return dir;
		}
	}

}