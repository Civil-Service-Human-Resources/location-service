# location-service

RPG location service provides a restful endpoint on which to search for locations by name or postcode.

## Docker

### Parameters

#### location-service parameters

| Name                                    | Description           |
| -------------                           | -------------         |
| LOCATION_SERVICE_GOOGLE_SERVICE_API_KEY | API Key used by the location service to call the Google API |
| LOCATION_SERVICE_USERNAME               | Access to the location service is secured with HTTP Basic auth.  This is the username to access the service             |
| LOCATION_SERVICE_PASSWORD               | Access to the location service is secured with HTTP Basic auth.  This is the password to access the service   |

#### filebeat parameters

| Name                      | Description           |
| -------------             | -------------         |
| FILEBEAT_PLATFORM         | Cloud platform on which the service is deployed (eg, aws or azure) |
| FILEBEAT_ENVIRONMENT      | dev, test, prod|
| FILEBEAT_COMPONENT        | location-service   |
| FILEBEAT_HOSTS            | URL for the logit service   |

Setting/passing the LOG_DIR system value/param will specify where log files are written


### Running locally

```
    docker run -p 80:8989 \
        -e LOCATION_SERVICE_GOOGLE_SERVICE_API_KEY=<insert google key> \
        -e LOCATION_SERVICE_USERNAME=user \
        -e LOCATION_SERVICE_PASSWORD=pass \
        -e FILEBEAT_PLATFORM=mac \
        -e FILEBEAT_ENVIRONMENT=dev \
        -e FILEBEAT_COMPONENT=location-service \
        -e FILEBEAT_HOSTS='<insert filebeat host>' \
        location-service
```