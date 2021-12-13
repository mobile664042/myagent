package myagent.edit;

import java.awt.geom.IllegalPathStateException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ConfigListener {
	private static final String FILENAME = "methodFilter.properties";
	private static final String WHITE_METHODS = "white.methods";
	private static final String WHITE_METHODS_EXCLUDE = "white.methods.exclude";
	
	
	private static final String BLACK_METHODS = "black.methods";
	private static final String BLACK_METHODS_EXCLUDE = "black.methods.exclude";
	
	private static final String ROLL_POSTFIX  = "log.roll.postfix";
	
	private static final HashSet<String> WHITE_METHODS_SET = new HashSet<String>();
	private static final HashSet<String> WHITE_METHODS_EXCLUDE_SET = new HashSet<String>();
	
	private static final HashSet<String> BLACK_METHODS_SET = new HashSet<String>();
	private static final HashSet<String> BLACK_METHODS_EXCLUDE_SET = new HashSet<String>();;
	
	
	@SuppressWarnings("unused")
	private static long lastAccessTime = 0;
	private static long lastFileSize = 0;
	private static long lastModifiedTime = 0;
	private static String lastPostfix  = "";
	private static Properties lastProp = new Properties();

	static {
		try {
			loadMethodFilter();
		} catch (Exception e) {
			throw new IllegalStateException("加载文件失败退出", e);
		}
		scheduleCheck();
	}
	public static String getLastPostfix() {
		return lastPostfix;
	}
	public static long getLastModifiedTime() {
		return lastModifiedTime;
	}
	public static Object getProp(String key) {
		return lastProp.get(key);
	}
	public static boolean isBlackMethodEmpty() {
		return BLACK_METHODS_SET.size() == 0;
	}
	public static boolean isBlackMethod(String method) {
		return isContains(BLACK_METHODS_SET, method);
	}
	public static boolean isBlackMethodExclude(String method) {
		return isContains(BLACK_METHODS_EXCLUDE_SET, method);
	}
	
	public static boolean isAbsoluteWhiteMethod(String method) {
		return WHITE_METHODS_SET.contains(method);
	}
	public static boolean isWhiteMethod(String method) {
		return isContains(WHITE_METHODS_SET, method);
	}
	public static boolean isWhiteMethodExclude(String method) {
		return isContains(WHITE_METHODS_EXCLUDE_SET, method);
	}
	
	private static boolean isContains(HashSet<String> myset, String method) {
		if(myset.contains(method)){
			return true;
		}
		
		for(String item : myset){
			if(item.endsWith("*")){
				item = item.substring(0, item.length() -1);
				if(method.startsWith(item)){
					return true;
				}
			}
			else if(method.equals(item)){
				return true;
			}
		}
		return false;
	}

	private static File loadFile() {
		File file = new File(FILENAME);
		MyPrint.println("1 ready load " + FILENAME + " : " + file.getAbsolutePath());
		if (!file.exists()) {
			file = new File(OsInfoUtil.getTempDir(), FILENAME);
			MyPrint.println("2 ready load " + FILENAME + " : " + file.getAbsolutePath());
		}
		if (!file.exists()) {
			throw new IllegalPathStateException(FILENAME + ":文件没找到!");
		}
		return file;
	}

	private static void scheduleCheck() {
		Timer timer = new Timer("检测scheduleCheck:" + FILENAME);
		timer.schedule(new TimerTask() {
			public void run() {
				reloadMethodFilter();
			}
		}, 120000, 10000);
	}

	private static void reloadMethodFilter() {
		try {
			File file = loadFile();
			long fileSize = file.length();
			long modifiedTime = file.lastModified();
			
			if (lastModifiedTime != modifiedTime || lastFileSize != fileSize) {
				MyPrint.println(FILENAME + " have changed!!!,  must reload");
				loadMethodFilter();
				lastFileSize = fileSize;
				lastModifiedTime = modifiedTime;
			}
		} catch (Exception e) {
			e.printStackTrace();
			MyPrint.println("定时更新配置失败" + e);
		}
	}

	private static void loadMethodFilter() throws Exception {
		File file = loadFile();
		lastFileSize = file.length();
		lastModifiedTime = file.lastModified();
		lastAccessTime = System.currentTimeMillis();

		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			Properties prop = new Properties();
			prop.load(new InputStreamReader(inputStream, "utf-8"));

			lastProp.clear();
			lastProp.putAll(prop);
			
			String whiteMethodStr = (String)lastProp.get(WHITE_METHODS);
			HashSet<String> whiteMethodSet = loadMethodList(whiteMethodStr);
			WHITE_METHODS_SET.clear();
			WHITE_METHODS_SET.addAll(whiteMethodSet);

			String whiteMethodExcludeStr = (String)lastProp.get(WHITE_METHODS_EXCLUDE);
			HashSet<String> whiteMethodExcludeSet = loadMethodList(whiteMethodExcludeStr);
			WHITE_METHODS_EXCLUDE_SET.clear();
			WHITE_METHODS_EXCLUDE_SET.addAll(whiteMethodExcludeSet);
			
			String blackMethodStr = (String)lastProp.get(BLACK_METHODS);
			HashSet<String> blackMethodSet = loadMethodList(blackMethodStr);
			BLACK_METHODS_SET.clear();
			BLACK_METHODS_SET.addAll(blackMethodSet);

			String blackMethodExcludeStr = (String)lastProp.get(BLACK_METHODS_EXCLUDE);
			HashSet<String> blackMethodExcludeSet = loadMethodList(blackMethodExcludeStr);
			BLACK_METHODS_EXCLUDE_SET.clear();
			BLACK_METHODS_EXCLUDE_SET.addAll(blackMethodExcludeSet);
			
			//刷新日志文件
			String rollPostfixStr = (String)lastProp.get(ROLL_POSTFIX);
			if(rollPostfixStr != null && rollPostfixStr.trim().length() > 0){
				lastPostfix = rollPostfixStr.trim();
			}
			
			Set<String> sets = prop.stringPropertyNames();
			StringBuilder sb = new StringBuilder("\n-----------------------\n");
			for (String key : sets) {
				sb.append(key + "=" + prop.getProperty(key)).append("\n");
			}
			sb.append("----------------\n");
			MyPrint.println(file.getAbsolutePath() + " had loaded !!! " + sb);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	private static HashSet<String> loadMethodList(String methodStr) throws Exception {
		HashSet<String> set = new HashSet<String>();
		if(methodStr != null && methodStr.length() > 0){
			String []array = methodStr.split(",");
			for(String item : array){
				set.add(item.trim());
			}
		}
		return set;
	}
}
