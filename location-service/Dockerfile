FROM 	cshrrpg.azurecr.io/maven-3-base as dependencies

ARG	    arg_maven_repo=https://nexus.dev.cshr-gov.uk/nexus
ENV 	MAVEN_REPO=$arg_maven_repo

ARG 	build_artefact=location-service.jar
ARG	    arg_maven_repo=https://nexus.dev.cshr-gov.uk/nexus
ENV	    MAVEN_REPO=$arg_maven_repo

COPY 	pom.xml /usr/src/myapp/pom.xml
COPY 	.settings.xml /usr/share/maven/ref/settings.xml

RUN 	mvn -f /usr/src/myapp/pom.xml -s /usr/share/maven/ref/settings.xml clean verify --fail-never

FROM 	cshrrpg.azurecr.io/maven-3-base as build

COPY 	--from=dependencies /usr/share/maven/ref/ /usr/share/maven/ref/

COPY  . /usr/src/myapp

RUN 	mvn -f /usr/src/myapp/pom.xml -s /usr/share/maven/ref/settings.xml clean package

FROM 	cshrrpg.azurecr.io/java-8-base-filebeat

COPY 	--from=build /usr/src/myapp/target/location-service.jar /app/$build_artefact

COPY    --chown=appuser:appuser entrypoint.sh /usr/local/bin/entrypoint.sh
RUN     chmod 755 /usr/local/bin/entrypoint.sh

USER 	appuser

ENTRYPOINT [  "/usr/local/bin/entrypoint.sh" ]
