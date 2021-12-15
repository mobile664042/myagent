 * Print code execution stack
 * In some complex method calls (especially open source or unfamiliar projects), We don’t know the execution process (call stack) of some methods, and it’s difficult to debug (such as using interfaces, proxies, asynchrony, reactor, lambda, but We don’t know yet. Where to set the breakpoint; or there is no source code); The execution process of the method can be easily printed through my myagent.jar.
 * 
 * The usage is as follows: *java -javaagent:{relative path or absolute path}/myagent.jar -jar xxxx.jar
 * 
 * The default is not to monitor itself, jdk, javassist, native method, getter setter, etc.
 * Create a methodFilter.properties configuration file in the current directory of the operation or d:/tmp (/tmp for linux)
 * 
 * white.methods is a whitelist, which must be monitored. It supports asterisk matching at the end, multiple use comma to separate, format: {packageName}.{className}.{methodName}, for example: org.apache.skywalking.,mytest .,testdemo,com.xdd.*
 * white.methods.exclude is the method of whitelist exclusion, the usage is the same as white.methods
 * black.methods is a blacklist, which must not be monitored. It supports asterisk matching at the end, multiple usescomma to separate, format: {packageName}.{className}.{methodName}, for example: mytest.MyPerson.testSimple
 * black.methods.exclude is the method of blacklist exclusion, the usage is the same as white.methods
 * log.roll.postfix change the suffix of the log file (check whether the content of the file has changed every 5 seconds)
 * 
 * The output log path is d:/tmp/myagent.log.1639232014093.22 (Linux os MacOs is /tmp/myagent.log.1639232014093) (.22 is to use log.roll.postfix=22)
 * The output log format is thread id and thread name, _ indicates a stack depth, the package name uses acronyms, the beginning of the method has a timestamp, the end of the method is time-consuming, and a blank line indicates that there are parallel calls, for example:

<pre>
1main_____m.MyPerson.testStatic.1639364334110
1main______m.MyPerson$TestInnerClazz.MyPerson$TestInnerClazz.1639364334120
1main______m.MyPerson$TestInnerClazz.MyPerson$TestInnerClazz:tMS:0

1main______m.MyPerson.MyPerson.1639364334120
1main______m.MyPerson.MyPerson:tMS:0

1main______m.MyPerson.testSimple.1639364334120
1main_______m.MyPerson$TestInnerClazz.homework.1639364334120
1main________m.MyPerson$TestInnerClazz.run.1639364334121
1main________m.MyPerson$TestInnerClazz.run:tMS:0
1main_______m.MyPerson$TestInnerClazz.homework:tMS:0
1main______m.MyPerson.testSimple:tMS:0
1main_____m.MyPerson.testStatic:tMS:0
</pre>
