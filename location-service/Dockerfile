FROM 	cshrrpg.azurecr.io/maven-3-base as dependencies

ARG 	build_artefact=location-service.jar
ARG	    arg_maven_repo
ENV	    MAVEN_REPO=$arg_maven_repo

COPY 	pom.xml /usr/src/myapp/pom.xml

RUN 	mvn -f /usr/src/myapp/pom.xml clean verify --fail-never

FROM 	cshrrpg.azurecr.io/maven-3-base as build
COPY 	--from=dependencies /root/.m2 /root/.m2

COPY  . /usr/src/myapp

RUN 	cat /usr/src/myapp/pom.xml && ls -ls /usr/src/myapp &&  mvn -f /usr/src/myapp/pom.xml clean package

FROM 	cshrrpg.azurecr.io/java-8-base-filebeat

COPY 	--from=build /usr/src/myapp/target/location-service.jar /app/location-service.jar

COPY    --chown=appuser:appuser entrypoint.sh /usr/local/bin/entrypoint.sh
RUN     chmod 755 /usr/local/bin/entrypoint.sh

COPY    --chown=appuser:appuser entrypoint.sh /usr/local/bin/entrypoint.sh
RUN     chmod 755 /usr/local/bin/entrypoint.sh

USER 	appuser

ENTRYPOINT [  "/usr/local/bin/entrypoint.sh" ]
