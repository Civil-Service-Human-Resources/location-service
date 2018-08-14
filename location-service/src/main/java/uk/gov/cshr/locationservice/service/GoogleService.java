package uk.gov.cshr.locationservice.service;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LocationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.cshr.locationservice.LocationServiceException;
import uk.gov.cshr.locationservice.controller.Coordinates;

@Service
public class GoogleService implements CoordinatesService {

    private static final Logger log = LoggerFactory.getLogger(GoogleService.class);

    @Value("${spring.location.service.googleService.apiKey}")
    private String apiKey;

    /**
     * @param searchTerm
     * @return
     * @throws LocationServiceException if the searchTerm does not match a place
     *                                  or postcode
     */
    @Override
    @Cacheable("coordinates")
    public Coordinates findCoordinates(String searchTerm) throws LocationServiceException {

        log.debug("findCoordinates: " + searchTerm);

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return null;
        }

        searchTerm = searchTerm.toLowerCase();

        // contains a number so assume postcode
        if (isPostcode(searchTerm)) {
            return postcodeNameLookup(searchTerm);
        } else {
            return placeNameLookup(searchTerm);
        }
    }

    private static boolean isPostcode(String searchTerm) {
        return searchTerm.matches(".*\\d+.*");
    }

    private Coordinates postcodeNameLookup(String postcode) throws LocationServiceException {

        //e.g. BS16JS
        if (postcode.length() > 4) {
            postcode = new StringBuilder(postcode).insert(postcode.length() - 3, " ").toString();
        }

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
                    results[0].geometry.location.lng,
                    RegionLookup.findRegion(results[0].geometry.location.lat,
                            results[0].geometry.location.lng).getName());
        } catch (ApiException | InterruptedException | IOException e) {
            throw new LocationServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private Coordinates placeNameLookup(String placeName) throws LocationServiceException {

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
        } catch (ApiException | InterruptedException | IOException e) {
            throw new LocationServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private Coordinates getCoordinates(double latitude, double longitude) {
        UK_NUTS ukNuts = RegionLookup.findRegion(latitude, longitude);
        return new Coordinates(latitude, longitude, ukNuts != null ? ukNuts.getName() : null);
    }

    @CacheEvict(value = "coordinates", allEntries = true)
    @Scheduled(fixedDelayString = "${spring.location.service.googleService.cacheTime}")
    public void cacheEvict() {
        log.info("Evict coordinates cache");
    }
}
