FROM eclipse-temurin:17-jdk-ubi9-minimal as jre-build

# Create a custom Java runtime
RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,java.desktop,java.instrument,java.management,java.naming,java.rmi,java.scripting,java.security.sasl,java.sql,jdk.compiler,jdk.httpserver,jdk.sctp,jdk.unsupported,jdk.crypto.ec \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

# Define your base image
FROM debian:buster-slim
ENV LANG C.UTF-8
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"

# 添加时区环境变量，亚洲，上海
ENV TimeZone=Asia/Shanghai
# 使用软连接，并且将时区配置覆盖/etc/timezone
RUN ln -snf /usr/share/zoneinfo/$TimeZone /etc/localtime && echo $TimeZone > /etc/timezone

COPY --from=jre-build /javaruntime $JAVA_HOME

ADD neo-reward-1.0.1-SNAPSHOT.jar /neo-reward.jar
ADD lib lib
ENV JAVA_OPTS="-server -Xms128m -Xmx256m"
ENTRYPOINT exec $JAVA_HOME/bin/java $JAVA_OPTS  -jar /neo-reward.jar

