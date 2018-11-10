#!/usr/bin/env bash
export JAVA_HOME=/root/software/jdk1.8
export JAVA_LIB=${JAVA_HOME}/jre/lib
export CLASSPATH=.:$JAVA_LIB/tools.jar:$JAVA_LIB/dt.jar:$JAVA_LIB/rt.jar:$JAVA_LIB/resources.jar:${JAVA_HOME}/lib
pid=`lsof -ti:6060`
if [ ! -n "$pid" ]; then
kill -9 $pid
echo 'kill pid:'$pid' successfully!'
fi
nohup java -Djna.library.path=/root/project/SliceCore/cmake-build-debug -jar WebService/target/WebService-1.0-SNAPSHOT.jar &
