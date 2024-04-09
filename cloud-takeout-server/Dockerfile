FROM openjdk:17
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 拷贝jar包
COPY cloud-takeout-server-1.0-SNAPSHOT.jar /app.jar
# 入口
ENTRYPOINT ["java", "-jar", "/app.jar"]


