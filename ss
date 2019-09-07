[1mdiff --git a/src/main/java/com/alon/querydecoder/Expression.java b/src/main/java/com/alon/querydecoder/Expression.java[m
[1mindex 83fa7fe..e22dfa3 100644[m
[1m--- a/src/main/java/com/alon/querydecoder/Expression.java[m
[1m+++ b/src/main/java/com/alon/querydecoder/Expression.java[m
[36m@@ -46,7 +46,7 @@[m [mpublic class Expression implements Decoder {[m
     }[m
     [m
     @Override[m
[31m-    public Expression parse(String expression) {[m
[32m+[m[32m    public final Expression parse(String expression) {[m
         [m
         expression = this.normalize(expression);[m
         [m
[1mdiff --git a/src/main/java/com/alon/querydecoder/Group.java b/src/main/java/com/alon/querydecoder/Group.java[m
[1mindex ea76597..754dafb 100644[m
[1m--- a/src/main/java/com/alon/querydecoder/Group.java[m
[1m+++ b/src/main/java/com/alon/querydecoder/Group.java[m
[36m@@ -31,7 +31,7 @@[m [mpublic class Group implements Decoder {[m
     }[m
     [m
     @Override[m
[31m-    public Group parse(String expression) {[m
[32m+[m[32m    public final Group parse(String expression) {[m
         [m
         this.checkIfStartsWithParetheses(expression);[m
         [m
[36m@@ -143,9 +143,7 @@[m [mpublic class Group implements Decoder {[m
     [m
     @Override[m
     public String toString() {[m
[31m-        String result;[m
[31m-        [m
[31m-        result = String.format("(%s)", decoder);[m
[32m+[m[32m        String result = String.format("(%s)", decoder);[m
         [m
         if (next != null)[m
             result = String.format("%s %s %s", result, this.logicalOperator, this.next);[m
[1mdiff --git a/src/test/java/com/alon/querydecoder/test/QueryDecoderTest.java b/src/test/java/com/alon/querydecoder/test/QueryDecoderTest.java[m
[1mindex 3966dbf..b45644d 100644[m
[1m--- a/src/test/java/com/alon/querydecoder/test/QueryDecoderTest.java[m
[1m+++ b/src/test/java/com/alon/querydecoder/test/QueryDecoderTest.java[m
[36m@@ -59,6 +59,14 @@[m [mpublic class QueryDecoderTest {[m
         assertEquals(query, this.createDecoder(query).decode());[m
     }[m
     [m
[32m+[m[32m    @Test[m
[32m+[m[32m    public void testResolvedStringLevelSeven() {[m
[32m+[m[32m        String query = "nome[CT]:Paulo AND ((sobrenome[CT]:Alonso OR idade[LTE]:40) AND profissao:Programador)";[m
[32m+[m[32m        String expected = "nome[CT]:Paulo AND ((sobrenome[CT]:Alonso OR idade[LTE]:40) AND profissao[EQ]:Programador)";[m
[32m+[m[41m        [m
[32m+[m[32m        assertEquals(expected, this.createDecoder(query).decode());[m
[32m+[m[32m    }[m
[32m+[m[41m    [m
     private QueryDecoder<String> createDecoder(String query) {[m
         return new QueryDecoder<>(query, this::decoder);[m
     }[m
[1mdiff --git a/target/classes/com/alon/querydecoder/Expression.class b/target/classes/com/alon/querydecoder/Expression.class[m
[1mindex c2edd1a..058988e 100644[m
Binary files a/target/classes/com/alon/querydecoder/Expression.class and b/target/classes/com/alon/querydecoder/Expression.class differ
[1mdiff --git a/target/classes/com/alon/querydecoder/Group.class b/target/classes/com/alon/querydecoder/Group.class[m
[1mindex a6c8544..c8e1c59 100644[m
Binary files a/target/classes/com/alon/querydecoder/Group.class and b/target/classes/com/alon/querydecoder/Group.class differ
[1mdiff --git a/target/classes/com/alon/querydecoder/QueryDecoder.class b/target/classes/com/alon/querydecoder/QueryDecoder.class[m
[1mindex 811adf2..db6c75c 100644[m
Binary files a/target/classes/com/alon/querydecoder/QueryDecoder.class and b/target/classes/com/alon/querydecoder/QueryDecoder.class differ
[1mdiff --git a/target/classes/com/alon/querydecoder/impl/SpringJpaSpecificationDecoder.class b/target/classes/com/alon/querydecoder/impl/SpringJpaSpecificationDecoder.class[m
[1mindex a9da460..f8b441b 100644[m
Binary files a/target/classes/com/alon/querydecoder/impl/SpringJpaSpecificationDecoder.class and b/target/classes/com/alon/querydecoder/impl/SpringJpaSpecificationDecoder.class differ
[1mdiff --git a/target/surefire-reports/TEST-com.alon.querydecoder.test.QueryDecoderTest.xml b/target/surefire-reports/TEST-com.alon.querydecoder.test.QueryDecoderTest.xml[m
[1mindex 85438a4..d2a1055 100644[m
[1m--- a/target/surefire-reports/TEST-com.alon.querydecoder.test.QueryDecoderTest.xml[m
[1m+++ b/target/surefire-reports/TEST-com.alon.querydecoder.test.QueryDecoderTest.xml[m
[36m@@ -1,10 +1,10 @@[m
 <?xml version="1.0" encoding="UTF-8" ?>[m
[31m-<testsuite tests="5" failures="1" name="com.alon.querydecoder.test.QueryDecoderTest" time="105.591" errors="0" skipped="0">[m
[32m+[m[32m<testsuite tests="7" failures="0" name="com.alon.querydecoder.test.QueryDecoderTest" time="0.057" errors="0" skipped="0">[m
   <properties>[m
     <property name="java.runtime.name" value="OpenJDK Runtime Environment"/>[m
     <property name="java.vm.version" value="11.0.4+11-post-Ubuntu-1ubuntu218.04.3"/>[m
     <property name="sun.boot.library.path" value="/usr/lib/jvm/java-11-openjdk-amd64/lib"/>[m
[31m-    <property name="maven.multiModuleProjectDirectory" value="/home/paulo/NetBeansProjects/querydecoder"/>[m
[32m+[m[32m    <property name="maven.multiModuleProjectDirectory" value="/home/paulo/Documentos/GitHub/query-decoder"/>[m
     <property name="java.vm.vendor" value="Ubuntu"/>[m
     <property name="java.vendor.url" value="https://ubuntu.com/"/>[m
     <property name="guice.disable.misplaced.annotation.check" value="true"/>[m
[36m@@ -15,7 +15,7 @@[m
     <property name="sun.java.launcher" value="SUN_STANDARD"/>[m
     <property name="test" value="com.alon.querydecoder.test.QueryDecoderTest"/>[m
     <property name="java.vm.specification.name" value="Java Virtual Machine Specification"/>[m
[31m-    <property name="user.dir" value="/home/paulo/NetBeansProjects/querydecoder"/>[m
[32m+[m[32m    <property name="user.dir" value="/home/paulo/Documentos/GitHub/query-decoder"/>[m
     <property name="java.vm.compressedOopsMode" value="32-bit"/>[m
     <property name="java.runtime.version" value="11.0.4+11-post-Ubuntu-1ubuntu218.04.3"/>[m
     <property name="java.awt.graphicsenv" value="sun.awt.X11GraphicsEnvironment"/>[m
[36m@@ -33,9 +33,7 @@[m
     <property name="java.class.version" value="55.0"/>[m
     <property name="java.specification.name" value="Java Platform API Specification"/>[m
     <property name="sun.management.compiler" value="HotSpot 64-Bit Tiered Compilers"/>[m
[31m-    <property name="jpda.listen" value="true"/>[m
[31m-    <property name="os.version" value="5.0.0-25-generic"/>[m
[31m-    <property name="jpda.address" value="58303"/>[m
[32m+[m[32m    <property name="os.version" value="5.0.0-27-generic"/>[m
     <property name="user.home" value="/home/paulo"/>[m
     <property name="user.timezone" value="America/Sao_Paulo"/>[m
     <property name="java.awt.printerjob" value="sun.print.PSPrinterJob"/>[m
[36m@@ -43,15 +41,14 @@[m
     <property name="java.specification.version" value="11"/>[m
     <property name="user.name" value="paulo"/>[m
     <property name="java.class.path" value="/snap/netbeans/10/netbeans/java/maven/boot/plexus-classworlds-2.5.2.jar"/>[m
[31m-    <property name="maven.surefire.debug" value="-agentlib:jdwp=transport=dt_socket,server=n,address=58303"/>[m
     <property name="java.vm.specification.version" value="11"/>[m
     <property name="sun.arch.data.model" value="64"/>[m
[31m-    <property name="sun.java.command" value="org.codehaus.plexus.classworlds.launcher.Launcher -Dtest=com.alon.querydecoder.test.QueryDecoderTest -DforkMode=once -Dmaven.surefire.debug=-agentlib:jdwp=transport=dt_socket,server=n,address=58303 -Djpda.listen=true -Djpda.address=58303 -Dmaven.ext.class.path=/snap/netbeans/10/netbeans/java/maven-nblib/netbeans-eventspy.jar test-compile surefire:test"/>[m
[32m+[m[32m    <property name="sun.java.command" value="org.codehaus.plexus.classworlds.launcher.Launcher -Dtest=com.alon.querydecoder.test.QueryDecoderTest -Dmaven.ext.class.path=/snap/netbeans/10/netbeans/java/maven-nblib/netbeans-eventspy.jar surefire:test"/>[m
     <property name="java.home" value="/usr/lib/jvm/java-11-openjdk-amd64"/>[m
     <property name="user.language" value="pt"/>[m
     <property name="java.specification.vendor" value="Oracle Corporation"/>[m
     <property name="awt.toolkit" value="sun.awt.X11.XToolkit"/>[m
[31m-    <property name="java.vm.info" value="mixed mode"/>[m
[32m+[m[32m    <property name="java.vm.info" value="mixed mode, sharing"/>[m
     <property name="java.version" value="11.0.4"/>[m
     <property name="securerandom.source" value="file:/dev/./urandom"/>[m
     <property name="java.vendor" value="Ubuntu"/>[m
[36m@@ -59,24 +56,16 @@[m
     <property name="file.separator" value="/"/>[m
     <property name="java.version.date" value="2019-07-16"/>[m
     <property name="java.vendor.url.bug" value="https://bugs.launchpad.net/ubuntu/+source/openjdk-lts"/>[m
[31m-    <property name="forkMode" value="once"/>[m
     <property name="sun.io.unicode.encoding" value="UnicodeLittle"/>[m
     <property name="sun.cpu.endian" value="little"/>[m
     <property name="sun.desktop" value="gnome"/>[m
     <property name="sun.cpu.isalist" value=""/>[m
   </properties>[m
[31m-  <testcase classname="com.alon.querydecoder.test.QueryDecoderTest" name="com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelOne" time="0.062"/>[m
[31m-  <testcase classname="com.alon.querydecoder.test.QueryDecoderTest" name="com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelTwo" time="0.001"/>[m
[31m-  <testcase classname="com.alon.querydecoder.test.QueryDecoderTest" name="com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelThree" time="0.002"/>[m
[31m-  <testcase classname="com.alon.querydecoder.test.QueryDecoderTest" name="com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelFour" time="0.008"/>[m
[31m-  <testcase classname="com.alon.querydecoder.test.QueryDecoderTest" name="com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelFive" time="105.518">[m
[31m-    <failure message="expected: &lt;nome[CT]:Paulo AND (sobrenome[CT]:Alonso OR idade[LTE]:40) AND profissao[EQ]:Programador&gt; but was: &lt;nome[CT]:Paulo AND (sobrenome[CT]:Alonso OR idade[LTE]:40 AND profissao[EQ]:Programador)&gt;" type="org.opentest4j.AssertionFailedError">org.opentest4j.AssertionFailedError: expected: &lt;nome[CT]:Paulo AND (sobrenome[CT]:Alonso OR idade[LTE]:40) AND profissao[EQ]:Programador&gt; but was: &lt;nome[CT]:Paulo AND (sobrenome[CT]:Alonso OR idade[LTE]:40 AND profissao[EQ]:Programador)&gt;[m
[31m-	at org.junit.jupiter.api.AssertionUtils.fail(AssertionUtils.java:54)[m
[31m-	at org.junit.jupiter.api.AssertEquals.failNotEqual(AssertEquals.java:195)[m
[31m-	at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:184)[m
[31m-	at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:179)[m
[31m-	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:508)[m
[31m-	at com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelFive(QueryDecoderTest.java:74)[m
[31m-</failure>[m
[31m-  </testcase>[m
[32m+[m[32m  <testcase classname="com.alon.querydecoder.test.QueryDecoderTest" name="com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelOne" time="0.028"/>[m
[32m+[m[32m  <testcase classname="com.alon.querydecoder.test.QueryDecoderTest" name="com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelTwo" time="0"/>[m
[32m+[m[32m  <testcase classname="com.alon.querydecoder.test.QueryDecoderTest" name="com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelThree" time="0.001"/>[m
[32m+[m[32m  <testcase classname="com.alon.querydecoder.test.QueryDecoderTest" name="com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelFour" time="0.003"/>[m
[32m+[m[32m  <testcase classname="com.alon.querydecoder.test.QueryDecoderTest" name="com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelFive" time="0.014"/>[m
[32m+[m[32m  <testcase classname="com.alon.querydecoder.test.QueryDecoderTest" name="com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelSix" time="0.006"/>[m
[32m+[m[32m  <testcase classname="com.alon.querydecoder.test.QueryDecoderTest" name="com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelSeven" time="0.005"/>[m
 </testsuite>[m
\ No newline at end of file[m
[1mdiff --git a/target/surefire-reports/com.alon.querydecoder.test.QueryDecoderTest.txt b/target/surefire-reports/com.alon.querydecoder.test.QueryDecoderTest.txt[m
[1mindex eba185f..2684c6c 100644[m
[1m--- a/target/surefire-reports/com.alon.querydecoder.test.QueryDecoderTest.txt[m
[1m+++ b/target/surefire-reports/com.alon.querydecoder.test.QueryDecoderTest.txt[m
[36m@@ -1,13 +1,4 @@[m
 -------------------------------------------------------------------------------[m
 Test set: com.alon.querydecoder.test.QueryDecoderTest[m
 -------------------------------------------------------------------------------[m
[31m-Tests run: 5, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 105.591 sec <<< FAILURE![m
[31m-com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelFive()  Time elapsed: 105.518 sec  <<< FAILURE![m
[31m-org.opentest4j.AssertionFailedError: expected: <nome[CT]:Paulo AND (sobrenome[CT]:Alonso OR idade[LTE]:40) AND profissao[EQ]:Programador> but was: <nome[CT]:Paulo AND (sobrenome[CT]:Alonso OR idade[LTE]:40 AND profissao[EQ]:Programador)>[m
[31m-	at org.junit.jupiter.api.AssertionUtils.fail(AssertionUtils.java:54)[m
[31m-	at org.junit.jupiter.api.AssertEquals.failNotEqual(AssertEquals.java:195)[m
[31m-	at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:184)[m
[31m-	at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:179)[m
[31m-	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:508)[m
[31m-	at com.alon.querydecoder.test.QueryDecoderTest.testResolvedStringLevelFive(QueryDecoderTest.java:74)[m
[31m-[m
[32m+[m[32mTests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.06 sec[m
[1mdiff --git a/target/test-classes/com/alon/querydecoder/test/QueryDecoderTest.class b/target/test-classes/com/alon/querydecoder/test/QueryDecoderTest.class[m
[1mindex 6bf6edf..9668f6e 100644[m
Binary files a/target/test-classes/com/alon/querydecoder/test/QueryDecoderTest.class and b/target/test-classes/com/alon/querydecoder/test/QueryDecoderTest.class differ
