package uk.gov.cshr.locationservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.cshr.locationservice.LocationServiceException;
import uk.gov.cshr.locationservice.controller.Coordinates;
import uk.gov.cshr.locationservice.model.OSResult;
import uk.gov.cshr.locationservice.model.postcode.PostcodeResult;

@Service
public class OSService implements CoordinatesService {

    private static final String API_KEY = "W2oKnTWJGhkfGikHdLPhC22VkAxK9VhU";

    private static final String PLACE_LOOKUP = "https://api.ordnancesurvey.co.uk/opennames/v1/find?query=%s&maxresults=1&key=%s";

    private static final String PARTIAL_POSTCODE_LOOKUP = "http://api.postcodes.io/outcodes/";

    private static final String FULL_POSTCODE_LOOKUP = "https://api.postcodes.io/postcodes/";


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
        if (searchTerm.matches(".*\\d+.*")) {

            if (searchTerm.length() > 5) {
//                postcodeResult = getEndpoint(FULL_POSTCODE_LOOKUP + searchTerm);
                RestTemplate restTemplate = new RestTemplate();
                PostcodeResult result = restTemplate.getForObject(FULL_POSTCODE_LOOKUP + searchTerm, PostcodeResult.class);
                return new Coordinates(Double.parseDouble(result.getLatitude()), Double.parseDouble(result.getLongitude()));
            }
            else {
//                postcodeResult = getEndpoint(PARTIAL_POSTCODE_LOOKUP + searchTerm);
                RestTemplate restTemplate = new RestTemplate();
                PostcodeResult result = restTemplate.getForObject(PARTIAL_POSTCODE_LOOKUP + searchTerm, PostcodeResult.class);
                return new Coordinates(Double.parseDouble(result.getLatitude()), Double.parseDouble(result.getLongitude()));
            }
        }
        else {

            // place lookup
            String url = String.format(PLACE_LOOKUP, searchTerm, API_KEY);

//            String placeLookupresult = getEndpoint(url);
//            String postcodeDistrict = extractPostcodeDistrictFromOSNamesQuery(placeLookupresult);
            RestTemplate restTemplate = new RestTemplate();
            OSResult result = restTemplate.getForObject(url, OSResult.class);
            String postcodeDistrict = result.getGAZETTEERENTRY().getPOSTCODEDISTRICT();

//            String outcoderesult = getEndpoint(PARTIAL_POSTCODE_LOOKUP + postcodeDistrict);
//            return extractCoordinatesFromPostcodeQuery(outcoderesult);
            restTemplate = new RestTemplate();
            PostcodeResult postcodeResult = restTemplate.getForObject(PARTIAL_POSTCODE_LOOKUP + postcodeDistrict, PostcodeResult.class);

//            String postcodeDistrict = result.getGAZETTEERENTRY().getPOSTCODEDISTRICT();
            return new Coordinates(Double.parseDouble(postcodeResult.getLatitude()), Double.parseDouble(postcodeResult.getLongitude()));
        }

    }

//    public String getEndpoint(String url) throws LocationServiceException {
//
//
//
//
//    }

    public String getEndpoint1(String targetURL) throws LocationServiceException {

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


    static String extractPostcodeDistrictFromOSNamesQuery(String json) throws LocationServiceException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json);

            String result = jsonNode.get("header").get("query").asText();

            if ("noresult".equalsIgnoreCase(result)) {
                throw new LocationServiceException("No results found", 204);
            }

            JsonNode gazetteerEntry = jsonNode.get("results").get(0).get("GAZETTEER_ENTRY");

            if ("Postcode".equalsIgnoreCase(gazetteerEntry.get("LOCAL_TYPE").asText())) {
                String postcode = gazetteerEntry.get("NAME1").asText();
                return postcode.substring(0, postcode.indexOf(" "));
            }
            else if (gazetteerEntry.get("POSTCODE_DISTRICT").asText() != null) {
                return gazetteerEntry.get("POSTCODE_DISTRICT").asText();
            }
            else {
                throw new LocationServiceException("No results found", 204);
            }

        }
        catch (IOException ex) {
            throw new LocationServiceException(ex.getMessage(), 500);
        }
    }

    static Coordinates extractCoordinatesFromPostcodeQuery(String json) throws LocationServiceException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json);

            Coordinates coordinates = new Coordinates();
            coordinates.setLatitude(jsonNode.get("result").get("latitude").asDouble());
            coordinates.setLongitude(jsonNode.get("result").get("longitude").asDouble());

            return coordinates;
        }
        catch (IOException ex) {
            throw new LocationServiceException(ex.getMessage(), 500);
        }
    }

}
