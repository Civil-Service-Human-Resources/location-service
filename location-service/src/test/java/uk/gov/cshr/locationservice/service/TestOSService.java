package uk.gov.cshr.locationservice.service;

import org.apache.commons.io.IOUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.cshr.locationservice.controller.Coordinates;

@RunWith(MockitoJUnitRunner.class)
public class TestOSService {

    @Test
    public void testExtractPostcodeDistrictFromOSNamesQuery() throws Exception {

        String json = IOUtils.toString(this
                .getClass()
                .getResourceAsStream("/os/openNamesQueryResult.json"), "UTF-8");

        String postcodeDistrict = OSService.extractPostcodeDistrictFromOSNamesQuery(json);

        assertEquals("poscode district", "EC2V", postcodeDistrict);
    }

    @Test
    public void testExtractCoordinatesFromPostcodeLookup() throws Exception {

        String json = IOUtils.toString(this
                .getClass()
                .getResourceAsStream("/os/postcodeResult.json"), "UTF-8");

        Coordinates coordinates = OSService.extractCoordinatesFromPostcodeQuery(json);
        assertEquals("latitude", 51.4513231916218, coordinates.getLatitude(), 0);
        assertEquals("longitude", -2.58836608825299, coordinates.getLongitude(), 0);
    }

    @Test
    public void testExtractCoordinatesFromOutcodeLookup() throws Exception {

        String json = IOUtils.toString(this
                .getClass()
                .getResourceAsStream("/os/outcodeResult.json"), "UTF-8");

        Coordinates coordinates = OSService.extractCoordinatesFromPostcodeQuery(json);
        assertEquals("latitude", 51.4865211345912, coordinates.getLatitude(), 0);
        assertEquals("longitude", -2.51026139341739, coordinates.getLongitude(), 0);
    }
}
