# 使用JDK8作为基础镜像
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制jar包到容器中
COPY target/*.jar app.jar

# 暴露端口
EXPOSE 8080

# 设置时区为上海
ENV TZ=Asia/Shanghai

# 启动命令
ENTRYPOINT ["java","-jar","/app/app.jar"] 