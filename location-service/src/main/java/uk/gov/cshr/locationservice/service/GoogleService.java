package uk.gov.cshr.locationservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LocationType;
import java.io.IOException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.cshr.locationservice.LocationServiceException;
import uk.gov.cshr.locationservice.controller.Coordinates;

@Service
public class GoogleService implements CoordinatesService {

    private static final Logger log = LoggerFactory.getLogger(GoogleService.class);

    @Value("${spring.location.service.googleService.apiKey}")
    private String apiKey;

    /**
     *
     * @param searchTerm
     * @return
     * @throws LocationServiceException if the searchTerm does not match a place
     * or postcode
     */
    @Override
    public Coordinates findCoordinates(String searchTerm) throws LocationServiceException {

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return null;
        }

        searchTerm = searchTerm.toLowerCase();

        // contains a number so assume postcode
        if (isPostcode(searchTerm)) {
            return postcodeNameLookup(searchTerm);
        }
        else {
            return placeNameLookup(searchTerm);
        }
    }

    public static boolean isPostcode(String searchTerm) {
        return searchTerm.matches(".*\\d+.*");
    }

    public Coordinates postcodeNameLookup(String postcode) throws LocationServiceException {

        try {

            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build();
            
            GeocodingResult[] results = GeocodingApi.newRequest(context)
                    .locationType(LocationType.GEOMETRIC_CENTER)
                    .components(
                            ComponentFilter.country("GB"),
                            ComponentFilter.postalCode(postcode))
                    .await();

            if (results == null || results.length == 0) {
                throw new LocationServiceException("for postcode " + postcode, HttpStatus.NO_CONTENT.value());
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            log.debug(gson.toJson(results[0].addressComponents));
            
            return new Coordinates(
                    results[0].geometry.location.lat,
                    results[0].geometry.location.lng, findRegion(results[0].geometry.location.lat, results[0].geometry.location.lng));
        }
        catch (ApiException | InterruptedException | IOException e) {
            throw new LocationServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public Coordinates placeNameLookup(String placeName) throws LocationServiceException {

        try {

            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build();

            GeocodingResult[] results = GeocodingApi.newRequest(context)
                    .locationType(LocationType.GEOMETRIC_CENTER)
                    .address(placeName)
                    .components(ComponentFilter.country("GB"))
                    .await();

            if (results == null || results.length == 0) {
                throw new LocationServiceException("for placeName " + placeName, HttpStatus.NO_CONTENT.value());
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            log.debug(gson.toJson(results[0].addressComponents));

            return getCoordinates(results[0].geometry.location.lat, results[0].geometry.location.lng);
        }
        catch (ApiException | InterruptedException | IOException e) {
            throw new LocationServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public Coordinates getCoordinates(double latitude, double longitude) {
        return new Coordinates(latitude, longitude, findRegion(latitude, longitude));
    }

    public String findRegion(double latitude, double longitude) {

        try {
            String postcodeIOURL = "https://api.postcodes.io/postcodes?lat=%s&lon=%s";

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsnonNode = objectMapper.readTree(new URL(String.format(postcodeIOURL, latitude, longitude)));
            String regionString = jsnonNode.get("result").get(0).get("region").asText();

            return regionString;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
