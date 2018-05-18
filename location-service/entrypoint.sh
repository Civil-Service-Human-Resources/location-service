#! /bin/bash
##
# Due to some restrictions with the Azure platform we need to 
# run filebeat alongside the application in the same container.
# In order to achieve this we need an entrypoint file

echo "location-service: In entrypoint.sh"

echo "entrypoint.sh: command passed: " ${1}

if [[ ${#} -eq 0 ]]; then

    java -Djava.security.egd=file:/dev/./urandom -jar /app/location-service-1.0-SNAPSHOT.jar \
        --spring.location.service.googleService.apiKey=${LOCATION_SERVICE_GOOGLE_SERVICE_API_KEY} \
        --spring.location.security.username=${LOCATION_SERVICE_USERNAME} \
        --spring.location.security.password=${LOCATION_SERVICE_PASSWORD} && \
    filebeat -e -c /etc/filebeat/filebeat.yml
else
    exec "$@"
fi



