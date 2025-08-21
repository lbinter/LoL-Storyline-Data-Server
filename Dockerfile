
FROM python:3.10.12-alpine

RUN apk add --no-cache bash build-base pkgconfig linux-headers openjdk17
RUN python -m pip install --upgrade pip

VOLUME /tmp
COPY build/libs/league.parser-0.0.1-SNAPSHOT.jar /league.parser.jar
COPY requirements.txt /requirements.txt
RUN pip install -r requirements.txt

ENTRYPOINT ["java", "-jar", "/league.parser.jar" ]