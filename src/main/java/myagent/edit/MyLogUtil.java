package myagent.edit;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/***
 * 写入日志文件
 * 
 * @author zhangzhibo
 *
 */
public class MyLogUtil {
	private static final String FILENAME = "myagent.log." + System.currentTimeMillis();
	private static String lastPostfix  = ConfigListener.getLastPostfix();

	private static BufferedOutputStream bos = null;

	static {
		try {
			initLogFile(FILENAME);
		} catch (Exception e) {
			throw new IllegalStateException("加载文件失败退出", e);
		}
	}

	private static void initLogFile(String filename) throws Exception {
		File parent = new File(OsInfoUtil.getTempDir());
		if (!parent.exists()) {
			parent.mkdirs();
		}
		File file = new File(parent, filename);

		MyPrint.println("ready write log to " + filename + " --> " + file.getAbsolutePath());
		FileOutputStream fos = new FileOutputStream(file);
		bos = new BufferedOutputStream(fos);
	}

	public static synchronized void log(String str) {
		try {
			byte []data = (str + "\n").getBytes("utf-8");
			
			//重新刷文件
			if(!lastPostfix.equals(ConfigListener.getLastPostfix())){
				lastPostfix = ConfigListener.getLastPostfix();
				bos.flush();
				bos.close();
				
				initLogFile(FILENAME + "." + lastPostfix);
			}
			
			bos.write(data);
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
			MyPrint.println(str);
		}
	}
}
