#!/bin/bash
if [ -n "$JAVA_HOME" ] ; then
  echo "$JAVA_HOME"
else
  export JAVA_HOME=/usr/lib/jvm/jdk-17
fi

cd ../../
./mvnw clean install -Ptest -DskipTests

cd neo-reward || exit
rm -rf bin/lib bin/neo-reward-1.0.1-SNAPSHOT.jar
mv target/neo-reward-1.0.1-SNAPSHOT.jar bin/neo-reward-1.0.1-SNAPSHOT.jar
mv target/lib bin/

cd bin || exit
if command -v docker >/dev/null 2>&1; then
  docker build --tag neo-reward:latest -f ./Dockerfile
else
  podman build --tag neo-reward:latest -f ./Dockerfile
fi
rm -rf lib/ neo-reward-1.0.1-SNAPSHOT.jar