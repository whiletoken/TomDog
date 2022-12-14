#!/bin/bash
cd /root/CodeProjects/TomDog
git pull
./mvnw clean install -Ptest -DskipTests
cd neo-reward
mv target/neo-reward-1.0.1-SNAPSHOT.jar bin/neo-reward-1.0.1-SNAPSHOT.jar
mv target/lib bin/
cd bin || exit

if command -v docker >/dev/null 2>&1; then
  docker stop neo-reward
  echo "docker stoped neo-reward"

  docker rm neo-reward
  echo "docker removed neo-reward"

  docker rmi neo-reward
  echo "docker removed image neo-reward"

  docker build . -t neo-reward:latest -f Dockerfile
  rm -rf lib/ neo-reward-1.0.1-SNAPSHOT.jar

  docker run -itd --name=neo-reward \
  	--restart=always \
  	-p 8081:8081 \
  	-v /root/logs:/logs/ \
  	neo-reward:latest
else
  podman stop neo-reward
  echo "podman stoped neo-reward"

  podman rm neo-reward
  echo "podman removed neo-reward"

  podman rmi neo-reward
  echo "podman removed image neo-reward"

  podman build . -t neo-reward:latest -f Dockerfile
  rm -rf lib/ neo-reward-1.0.1-SNAPSHOT.jar

  podman run -itd --name=neo-reward \
  	--restart=always \
  	-p 8081:8081 \
  	-v /root/logs:/logs/ \
  	neo-reward:latest
fi