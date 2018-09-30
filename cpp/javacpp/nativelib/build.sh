#! /usr/bin/env bash
JAVACPP_JAR=~/.gradle/caches/modules-2/files-2.1/org.bytedeco/javacpp/1.4.2/cfa6a0259d98bff5aa8d41ba11b4d1dad648fbaa/javacpp-1.4.2.jar
javac -cp $JAVACPP_JAR NativeLibrary.java
java -jar $JAVACPP_JAR NativeLibrary -nodelete
java -cp $JAVACPP_JAR:. NativeLibrary
