package myagent.edit;

/***
 * 方法增强
 * @author zhangzhibo
 *
 */
public class MethodEnhancer {
	private static long LASTTTIME;
	private static ThreadLocal<String> CACHED = new ThreadLocal<String>();
	
	private static String getLogPrefix(String methodTag, boolean before){
		int level = Thread.currentThread().getStackTrace().length;
		StringBuilder sb = new StringBuilder();
		if (before && CACHED.get() == null) {
			CACHED.set("putted");
			sb.append("\n");
		}
		sb.append(Thread.currentThread().getId() + Thread.currentThread().getName());
		for(int i=0; i<level; i++){
			sb.append("_");
		}
		return sb.toString() + methodTag;
	}
	
	public static void insertBefore(String methodTag){
		String logPrefix = getLogPrefix(methodTag, true);
		LASTTTIME = System.currentTimeMillis();
		String bStr = logPrefix + "." + LASTTTIME; 
		myagent.edit.MyLogUtil.log(bStr);
	}
	
	public static void insertAfter(String methodTag){
		String logPrefix = getLogPrefix(methodTag, false);
		long time = System.currentTimeMillis() - LASTTTIME; 
		String aStr = logPrefix + ":tMS:" + time; 
		CACHED.remove();
		myagent.edit.MyLogUtil.log(aStr);
	}
	
	public static void addCatch(){
		String cStr = Thread.currentThread().getId() + Thread.currentThread().getName()+":ERR"; 
		CACHED.remove();
		myagent.edit.MyLogUtil.log(cStr);
	}
	
}
