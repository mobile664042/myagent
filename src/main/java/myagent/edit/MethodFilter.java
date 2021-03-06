package myagent.edit;

import javassist.CtBehavior;
import java.lang.reflect.Modifier;

/***
 * 方法过滤
 * @author zhangzhibo
 *
 */
public class MethodFilter {

	
	/***
	 * 是否通过
	 * 通过后，就需要修改
	 * @param method
	 * @return
	 */
	public static boolean isPass(CtBehavior method) {
		if(isAgentSelf(method)){
			return false;
		}
		if(isNativeMethod(method)){
			return false;
		}
		if(Modifier.isAbstract(method.getModifiers())){
			return false;
		}
		if(isLangPackageMethod(method)){
			return false;
		}
		if(isJavassistMethod(method)){
			return false;
		}
		if(isEnhancerByOther(method)){
			return false;
		}
		
		String methodName = method.getLongName();
		if(ConfigListener.isAbsoluteWhiteMethod(methodName)){
			return true;
		}
		if(isJdkMethod(method)){
			return false;
		}
		if(isLombokMethod(method)){
			return false;
		}
		
		if(ConfigListener.isWhiteMethod(methodName)){
			if(ConfigListener.isWhiteMethodExclude(methodName)){
				return false;
			}
			return true;
		}

		if(ConfigListener.isBlackMethodEmpty()){
			return false;
		}
		if(ConfigListener.isBlackMethod(methodName)){
			if(ConfigListener.isBlackMethodExclude(methodName)){
				return true;
			}
			return false;
		}
		return true;
	}
	
	
	
	/***
	 * 判断是否是java.lang包下的方法
	 * @param method
	 * @return
	 */
	private static boolean isLangPackageMethod(CtBehavior method){
		return method.getLongName().startsWith("java.lang.");
	}
	
	/***
	 * 判断是否是本地方法
	 * @param method
	 * @return
	 */
	private static boolean isNativeMethod(CtBehavior method){
		return Modifier.isNative(method.getModifiers());
	}
	
	/***
	 * 判断是否是javassist方法
	 * 
	 * @param method
	 * @return
	 */
	private static boolean isJavassistMethod(CtBehavior method){
		return method.getLongName().startsWith("javassist.");
	}
	
	/***
	 * 是否已被增强
	 */
	public static boolean isEnhancerByOther(CtBehavior method) {
		String methodName = method.getLongName();
		boolean result = methodName.contains("$$EnhancerBySpringCGLIB$$") || methodName.contains("$$EnhancerByCGLIB$$");
		if(result){
			return true;
		}

		//is asm?

		return result;
	}	
	
	/***
	 * 判断是否是jdk方法
	 * @param method
	 * @return
	 */
	private static boolean isJdkMethod(CtBehavior method){
		if(method.getLongName().startsWith("com.oracle.")){
			return true;
		}
		if(method.getLongName().startsWith("com.sun.")){
			return true;
		}
		if(method.getLongName().startsWith("java.")){
			return true;
		}
		if(method.getLongName().startsWith("javax.")){
			return true;
		}
		if(method.getLongName().startsWith("jdk.")){
			return true;
		}
		if(method.getLongName().startsWith("org.ietf.")){
			return true;
		}
		if(method.getLongName().startsWith("org.jcp.")){
			return true;
		}
		if(method.getLongName().startsWith("org.omg.")){
			return true;
		}
		if(method.getLongName().startsWith("org.w3c.")){
			return true;
		}
		if(method.getLongName().startsWith("org.xml.")){
			return true;
		}
		if(method.getLongName().startsWith("sun.")){
			return true;
		}
		return false;
	}
	
	/***
	 * 判断是否是getter setter 及Object方法
	 * @param method
	 * @return
	 */
	private static boolean isLombokMethod(CtBehavior method){
		String methodName = method.getName();
		
		if(methodName.startsWith("set") && methodName.length() > 3 && !isSpecialModifier(method)){
			return true;
		}
		if(methodName.startsWith("get") && methodName.length() > 3  && !isSpecialModifier(method)){
			try {
				if (method.getParameterTypes().length == 0) {
					return true;
				}
				return false;
			} catch (NotFoundException e) {
				return false;
			}
		}
		if(methodName.startsWith("is") && methodName.length() > 2  && !isSpecialModifier(method)){
			try {
				if (method.getParameterTypes().length == 0) {
					return true;
				}
				return false;
			} catch (NotFoundException e) {
				return false;
			}
		}
		if(methodName.equals("equals")){
			return true;
		}
		if(methodName.equals("getClass")){
			return true;
		}
		if(methodName.equals("hashCode")){
			return true;
		}
		if(methodName.equals("toString")){
			return true;
		}
		if(methodName.equals("notify")){
			return true;
		}
		if(methodName.equals("notifyAll")){
			return true;
		}
		if(methodName.equals("wait")){
			return true;
		}
		return false;
	}
	private static boolean isSpecialModifier(CtBehavior method){
		if(Modifier.isStatic(method.getModifiers())){
			return true;
		}
		else if(Modifier.isSynchronized(method.getModifiers())){
			return true;
		}
		else if(Modifier.isPrivate(method.getModifiers())){
			return true;
		}
		return false;
	}
	
	/***
	 * 判断是否自身 
	 * @param method
	 * @return
	 */
	private static boolean isAgentSelf(CtBehavior method){
		return method.getLongName().startsWith("myagent.") || method.getName().equals("main");
	}
	
}
