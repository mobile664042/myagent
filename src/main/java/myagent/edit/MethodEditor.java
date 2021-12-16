package myagent.edit;

import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;

public class MethodEditor {

	
	public static void modify(CtBehavior method) {
		try {
			String prefixStr = getPrefix(method);
			String insertCode = "String methodTag = \"" + prefixStr + "\"; ";

			method.insertBefore(insertCode + "myagent.edit.MethodEnhancer.insertBefore(methodTag);");
			method.insertAfter(insertCode + "myagent.edit.MethodEnhancer.insertAfter(methodTag);");
			
			CtClass etype = ClassPool.getDefault().get("java.lang.Exception");
			method.addCatch("{ myagent.edit.MethodEnhancer.addCatch(); throw $e; }", etype);
		} catch (Exception e) {
			MyPrint.println("add method fail !!!! " + method.getLongName());
			throw new IllegalArgumentException("增加方法失败", e);
		} 
	}
	
	private static String getPrefix(CtBehavior method){
		String []array = method.getDeclaringClass().getName().replace('.', ';').split(";");
		StringBuilder prefix = new StringBuilder();
		for(int i=0; i<array.length-1; i++){
			prefix.append(array[i].charAt(0)).append(".");
		}
		prefix.append(array[array.length-1]).append(".");
		prefix.append(method.getName());
		return prefix.toString();
	}
	
	
	
}
