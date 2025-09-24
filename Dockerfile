FROM    maven:3.9.11-eclipse-temurin-17

RUN     mkdir /app

WORKDIR /app

COPY    pom.xml .
RUN     mvn dependency:resolve

COPY    src ./src

CMD ["mvn", "test", "-Dis_docker=true"]
