package uk.gov.cshr.locationservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import uk.gov.cshr.locationservice.LocationServiceException;
import uk.gov.cshr.locationservice.controller.Coordinates;

@Service
public class MapITService implements CoordinatesService {

    private static final String AREAS_ENDPOINT = "https://mapit.mysociety.org/areas/";
    private static final String PARTIAL_POSTCODE_ENDPOINT = "https://mapit.mysociety.org/postcode/partial/";
    private static final String EXAMPLE_POSTCODE_ENDPOINT = "https://mapit.mysociety.org/area/%s/example_postcode";

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

        // contains a number and is less than 9 chars so assume postcode
        if (searchTerm.matches(".*\\d+.*") && searchTerm.length() < 9) {

            String partialPostcodeSearchResult = getEndpoint(PARTIAL_POSTCODE_ENDPOINT + searchTerm);
            return extractCoordinatesFromPartialPostcodeSearch(partialPostcodeSearchResult);
        }
        else if (!searchTerm.matches(".*\\d+.*")) {

            String areaPrefixSearchResult = getEndpoint(AREAS_ENDPOINT + searchTerm);
            String areaCode = MapITService.extractFirstAreaCode(areaPrefixSearchResult);

            String examplePostcodeURL = String.format(EXAMPLE_POSTCODE_ENDPOINT, areaCode);

            String examplePostcode = getEndpoint(examplePostcodeURL);

            examplePostcode = examplePostcode.replaceAll("\"", "").trim();

            // B45 0HF - keep first two digits
            examplePostcode = examplePostcode.substring(0, 3);

            // B4 - remove any numbers
            examplePostcode = examplePostcode.replaceAll("\\d", "").trim();

            // B - is the area code.  Append '1' to get town centre
            // B1
            examplePostcode += "1";

            String partialPostcodeSearchResult = getEndpoint(PARTIAL_POSTCODE_ENDPOINT + examplePostcode);
            return extractCoordinatesFromPartialPostcodeSearch(partialPostcodeSearchResult);
        }
        else {
            throw new LocationServiceException("No locations found for " + searchTerm, 404);
        }
    }

    public String getEndpoint(String targetURL) throws LocationServiceException {

        try {

            URL restServiceURL = new URL(targetURL);
            HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Accept", "application/json");

            if (httpConnection.getResponseCode() != 200) {
                throw new LocationServiceException("HTTP GET Request Failed with Error code : "
                        + httpConnection.getResponseCode(), httpConnection.getResponseCode());
            }

            String contents = IOUtils.toString(httpConnection.getInputStream(), "UTF-8");
            return contents;
        }
        catch (IOException e) {
            throw new LocationServiceException(e.getMessage(), 500);
        }
    }

    /**
     * Extracts the first parent_area from the json result of an AREAS_ENDPOINT
     * query.
     *
     * @param json
     * @return
     * @throws IOException
     */
    static String extractFirstAreaCode(String json) throws LocationServiceException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(json);

            Iterator<String> it = actualObj.fieldNames();
            while (it.hasNext()) {
                String key = it.next();
                return key;
            }

            return null;
        }
        catch (IOException ex) {
            throw new LocationServiceException(ex.getMessage(), 500);
        }
    }

    /**
     * Extracts coordinates from an area code geometry search.
     *
     * @param json
     * @return
     * @throws IOException
     */
    static Coordinates extractCoordinatesFromAreaCodeGeometrySearch(String json) throws LocationServiceException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(json);

            Coordinates coordinates = new Coordinates();
            coordinates.setLatitude(actualObj.get("centre_lat").asDouble());
            coordinates.setLongitude(actualObj.get("centre_lon").asDouble());

            return coordinates;
        }
        catch (IOException ex) {
            throw new LocationServiceException(ex.getMessage(), 500);
        }
    }

    /**
     * Extracts coordinates from a partial postcode search.
     *
     * @param json
     * @return
     * @throws IOException
     */
    static Coordinates extractCoordinatesFromPartialPostcodeSearch(String json) throws LocationServiceException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(json);

            Coordinates coordinates = new Coordinates();
            coordinates.setLatitude(actualObj.get("wgs84_lat").asDouble());
            coordinates.setLongitude(actualObj.get("wgs84_lon").asDouble());

            return coordinates;
        }
        catch (IOException ex) {
            throw new LocationServiceException(ex.getMessage(), 500);
        }
    }

}
