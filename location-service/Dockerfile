FROM 	maven:3.5-jdk-8 as dependencies

ARG 	build_artefact=location-service-1.0-SNAPSHOT.jar

COPY 	pom.xml /usr/src/myapp/pom.xml

RUN 	mvn -f /usr/src/myapp/pom.xml clean verify --fail-never

FROM 	maven:3.5-jdk-8 as build
COPY 	--from=dependencies /root/.m2 /root/.m2

#COPY 	pom.xml /usr/src/myapp/
#COPY  src/ /usr/src/myapp/
COPY  . /usr/src/myapp

RUN 	cat /usr/src/myapp/pom.xml && ls -ls /usr/src/myapp &&  mvn -f /usr/src/myapp/pom.xml clean package

FROM 	openjdk:8u151-jre-alpine3.7

COPY 	--from=build /usr/src/myapp/target/location-service-1.0-SNAPSHOT.jar /location-service-1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/location-service-1.0-SNAPSHOT.jar","--spring.location.service.googleService.apiKey=${LOCATION_SERVICE_GOOGLE_SERVICE_API_KEY}"]
