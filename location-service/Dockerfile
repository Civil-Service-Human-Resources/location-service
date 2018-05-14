FROM 	cshrrpg.azurecr.io/maven-3-base as dependencies

ARG 	build_artefact=location-service-1.0-SNAPSHOT.jar

COPY 	pom.xml /usr/src/myapp/pom.xml

RUN 	mvn -f /usr/src/myapp/pom.xml clean verify --fail-never

FROM 	cshrrpg.azurecr.io/maven-3-base as build
COPY 	--from=dependencies /root/.m2 /root/.m2

COPY  . /usr/src/myapp

RUN 	cat /usr/src/myapp/pom.xml && ls -ls /usr/src/myapp &&  mvn -f /usr/src/myapp/pom.xml clean package

FROM 	cshrrpg.azurecr.io/java-8-base

COPY 	--from=build /usr/src/myapp/target/location-service-1.0-0.jar /app/location-service-1.0-0.jar

USER 	appuser

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/location-service-1.0-0.jar", \
              "--spring.location.service.googleService.apiKey=${LOCATION_SERVICE_GOOGLE_SERVICE_API_KEY}", \
              "--spring.location.security.username=${LOCATION_SERVICE_USERNAME}", \
              "--spring.location.security.password=${LOCATION_SERVICE_PASSWORD}" ]
