 * 打印代码执行堆栈使用demo
 * 
 * -javaagent:{相对路径或绝对路径}/myagent.jar
 * 
 * 默认是不可以对自身、jdk、javassist、native方法、getter setter等监听
 * 
 * 在运行的根目录下或d:/tmp(linux为/tmp) 创建methodFilter.properties配置文件
  * 
 * white.methods 为白名单,一定监听的方法,支持末尾用*匹配,多个用,分隔, 格式:{包名}.{类名}.{方法名}, 例如:org.apache.skywalking.*,mytest.*,testdemo*,com.xdd.*
 * white.methods.exclude 为白名单排除的方法,用法与white.methods一样
 * black.methods 为黑名单,一定不监听的方法,支持末尾用*匹配,多个用,分隔, 格式:{包名}.{类名}.{方法名}, 例如:mytest.MyPerson.testSimple*
 * black.methods.exclude 为黑名单排除的方法,用法与white.methods一样
 * 
 * log.roll.postfix 更新日志文件后缀
 * 输出的日志路径为d:/tmp/myagent.log.1639232014093(linux为/tmp/myagent.log.1639232014093)
 * 输出的日志格式为，线程id线程名，_表示一个栈深度, 包名用首字母缩写,方法的开始有时间戳,方法的结束有时间耗时,空行表示存在并行调用,例如:
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