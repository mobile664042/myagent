package myagent.boot;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.security.CodeSource;
import java.util.jar.JarFile;

public class SimpleMain {
	public static void premain(String agentArguments, Instrumentation instrumentation) {
		try {
			loadAgent(instrumentation);
		} catch (Exception e) {
			e.printStackTrace();
			myagent.edit.MyPrint.println("add agent myself to classpath failed !!!!");
		}
		instrumentation.addTransformer(new SimpleTransformer());
	}
	
	private static void loadAgent(Instrumentation instrumentation) throws Exception {
		CodeSource codeSource = myagent.edit.MyPrint.class.getProtectionDomain().getCodeSource();
		File agentJarFile = new File(codeSource.getLocation().toURI().getSchemeSpecificPart());
		instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(agentJarFile));
		myagent.edit.MyPrint.println("add agent myself to classpath " + agentJarFile.getAbsolutePath());
        }
}
