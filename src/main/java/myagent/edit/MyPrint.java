package myagent.edit;

import java.util.Date;

public class MyPrint {

	public static void println(String str){
		String msg = new Date() + " " + Thread.currentThread().getName() + "    " + str;
		System.err.println(msg);
	}
}
