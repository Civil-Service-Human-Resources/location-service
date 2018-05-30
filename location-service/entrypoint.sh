#! /bin/bash
##
# Due to some restrictions with the Azure platform we need to 
# run filebeat alongside the application in the same container.
# In order to achieve this we need an entrypoint file
# Heap set to 1GB as getting out of memory errors when loading the nuts map

echo "location-service: In entrypoint.sh"

echo "entrypoint.sh: command passed: " ${1}

if [[ ${#} -eq 0 ]]; then
    # -E to preserve the environment
    echo "Starting filebeat"
    sudo -E filebeat -e -c /etc/filebeat/filebeat.yml &
    echo "Starting application"
    java -Djava.security.egd=file:/dev/./urandom -Xmx1024m -jar /app/location-service-1.0.0.jar \
        --spring.location.service.googleService.apiKey=${LOCATION_SERVICE_GOOGLE_SERVICE_API_KEY} \
        --spring.location.security.username=${LOCATION_SERVICE_USERNAME} \
        --spring.location.security.password=${LOCATION_SERVICE_PASSWORD}
else
    echo "Running command:"
    exec "$@"
fi



