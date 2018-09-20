#! /usr/bin/env bash
JAVACPP_JAR=~/Downloads/javacpp-1.4.2.jar
javac -cp $JAVACPP_JAR NativeLibrary.java
java -jar $JAVACPP_JAR NativeLibrary
java -cp ~/Downloads/javacpp-1.4.2.jar:. NativeLibrary
