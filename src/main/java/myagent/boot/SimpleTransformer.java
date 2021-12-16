package myagent.boot;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;
import myagent.edit.MethodEditor;
import myagent.edit.MethodFilter;
import myagent.edit.MyPrint;

public class SimpleTransformer implements ClassFileTransformer {

	/***
	 * loader - 定义要转换的类加载器；如果是引导加载器，则为 null className - 完全限定类内部形式的类名称和 The Java
	 * Virtual Machine Specification 中定义的接口名称。例如，"java/util/List"。
	 * classBeingRedefined - 如果是被重定义或重转换触发，则为重定义或重转换的类；如果是类加载，则为 null
	 * protectionDomain - 要定义或重定义的类的保护域 classfileBuffer - 类文件格式的输入字节缓冲区（不得修改）
	 */
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		return transformClass(className, classfileBuffer);
	}

	private byte[] transformClass(String className, byte[] classFileData) {
		ClassPool pool = ClassPool.getDefault();
		CtClass cl = null;
		try {
			//不可以使用 cl = pool.get(className.replaceAll("/", "."));
			cl = pool.makeClass(new java.io.ByteArrayInputStream(classFileData));
			if(cl.isInterface()){
				return cl.toBytecode();
			}
			CtBehavior[] methods = cl.getDeclaredBehaviors();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].isEmpty() == false) {
					changeMethod(methods[i]);
				}
			}
			byte[] data = cl.toBytecode();
			return data;
		} catch (Throwable t) {
			throw new IllegalStateException(t);
		} finally {
			if (cl != null) {
				cl.detach();
			}
		}
	}

	private void changeMethod(CtBehavior method) throws NotFoundException, CannotCompileException {
		if (MethodFilter.isPass(method)) {
			MethodEditor.modify(method);
			MyPrint.println(method.getLongName() + ",had been changed!!!!");
		}
	}
	

}
