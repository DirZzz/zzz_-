FROM 10.10.20.167/project/tomcat-base:latest
MAINTAINER cisco46589
ADD ./target/app.jar /
EXPOSE 10100
ENTRYPOINT ["java","-jar","/app.jar"]