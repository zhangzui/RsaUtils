# RsaUtils
RsaUtils

#maven录入
```
<dependency>
    <groupId>org.eclipse.core.commands</groupId>
    <artifactId>eclipse_core_commands</artifactId>
    <version>1.0.0</version>
</dependency>
org.eclipse.core.commands
org.eclipse.core.runtime
org.eclipse.equinox.common
org.eclipse.equinox.event
org.eclipse.jface

mvn install:install-file -Dfile=D:/learn-source/alipay/secret_key_tools_RSA_win/RSA签名验签工具windows_V1.4/zz_jar/org.eclipse.core.runtime_3.8.0.v20120912-155025.jar -DgroupId=org.eclipse.core.runtime -DartifactId=eclipse_core_runtime -Dversion=1.0.0 -Dpackaging=jar

mvn install:install-file -Dfile=D:/learn-source/alipay/secret_key_tools_RSA_win/RSA签名验签工具windows_V1.4/zz_jar/org.eclipse.equinox.common_3.6.100.v20120522-1841.jar -DgroupId=org.eclipse.equinox.common -DartifactId=eclipse_core_equinox_common -Dversion=1.0.0 -Dpackaging=jar
mvn install:install-file -Dfile=D:/learn-source/alipay/secret_key_tools_RSA_win/RSA签名验签工具windows_V1.4/zz_jar/org.eclipse.equinox.event_1.2.200.v20120522-2049.jar -DgroupId=org.eclipse.equinox.event -DartifactId=eclipse_core_equinox_event -Dversion=1.0.0 -Dpackaging=jar
mvn install:install-file -Dfile=D:/learn-source/alipay/secret_key_tools_RSA_win/RSA签名验签工具windows_V1.4/zz_jar/org.eclipse.jface_3.8.102.v20130123-162658.jar -DgroupId=org.eclipse.jface -DartifactId=eclipse_core_jface -Dversion=1.0.0 -Dpackaging=jar
mvn install:install-file -Dfile=D:/learn-source/alipay/secret_key_tools_RSA_win/RSA签名验签工具windows_V1.4/zz_jar/swt_util_windows_x86.jar -DgroupId=swt.util.windows.x86 -DartifactId=swt_util_windows_x86 -Dversion=1.0.0 -Dpackaging=jar
mvn install:install-file -Dfile=D:/learn-source/alipay/secret_key_tools_RSA_win/RSA签名验签工具windows_V1.4/zz_jar/xfire-all-1.2.6.jar -DgroupId=xfire.all -DartifactId=xfire-all -Dversion=1.2.6 -Dpackaging=jar

mvn install:install-file -Dfile=D:/learn-source/alipay/secret_key_tools_RSA_win/RSA签名验签工具windows_V1.4/zz_jar/KeyUtilApp.jar -DgroupId=KeyUtilApp -DartifactId=KeyUtilApp -Dversion=1.0.0 -Dpackaging=jar
mvn install:install-file -Dfile=D:/learn-source/alipay/secret_key_tools_RSA_win/RSA签名验签工具windows_V1.4/zz_jar/alipay_sign.jar -DgroupId=alipay_sign -DartifactId=alipay_sign -Dversion=1.0.0 -Dpackaging=jar

```
#打包 可执行java
参考https://blog.csdn.net/songylwq/article/details/52172908
1.project structure 中Artifacts
2. 在弹出的窗口中左侧选中"Artifacts"，点击"+"选择jar，然后选择"from modules with dependencies"。
3.在配置窗口中配置"Main Class"，输入目录名：libs
4.Build module