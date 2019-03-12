FROM adoptopenjdk/openjdk11:latest
RUN apk --no-cache add curl
COPY build/libs/*-all.jar micronaut-server-filter-demo.jar
CMD java ${JAVA_OPTS} -jar micronaut-server-filter-demo.jar
