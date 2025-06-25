FROM gradle:jdk24-ubi-minimal AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon --refresh-dependencies

FROM eclipse-temurin:24-jdk-ubi9-minimal

ARG PORT
ARG ADMIN_PORT
ENV PORT $PORT
ENV ADMIN_PORT $ADMIN_PORT
EXPOSE $PORT $ADMIN_PORT

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/favorite-countries* /app/favorite-countries.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=2000", \
"-XX:+UseStringDeduplication", "-XX:+ParallelRefProcEnabled", "-XX:+HeapDumpOnOutOfMemoryError", \
 "-Xlog:gc*:file=gc.log:time,uptime,level,tags", \
"-Djava.security.egd=file:/dev/./urandom","-jar","/app/favorite-countries.jar"]