#!/bin/bash 

javac -cp "src/lib/*:." -d out/ $(find ./ -name "*.java")
java -cp "src/lib/*" org.junit.platform.console.ConsoleLauncher \
     --classpath out/ \
     --scan-classpath
