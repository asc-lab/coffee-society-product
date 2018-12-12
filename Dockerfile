FROM openjdk:8-alpine
MAINTAINER ASC-LAB
RUN apk --no-cache add curl
EXPOSE 8080
COPY target/coffee-society-product*.jar product.jar
CMD java ${JAVA_OPTS} -jar product.jar
