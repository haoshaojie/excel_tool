FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER xhcj

RUN mkdir -p /cnfic/contract-manage

WORKDIR /cnfic/contract-manage

EXPOSE 8300

ADD ./target/cnfic-contract-manage.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]
