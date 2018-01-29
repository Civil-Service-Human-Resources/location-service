package uk.gov.cshr.locationservice.service;

import org.apache.commons.io.IOUtils;
import org.hamcrest.junit.ExpectedException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.cshr.locationservice.LocationServiceException;
import uk.gov.cshr.locationservice.controller.Coordinates;

@RunWith(MockitoJUnitRunner.class)
public class TestMapITService {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testExtractAreaCodeFromAreaPrefixSearch() throws Exception {

        String json = IOUtils.toString(this
                .getClass()
                .getResourceAsStream("/areaPrefixSearchSingleResult.json"), "UTF-8");

        String areaCode = MapITService.extractAreaCode(json);

        assertEquals("parentCode", "53741", areaCode);
    }

    @Test
    public void testMultipleResultsFromAreaFrefixSearch() throws Exception {

        String json = IOUtils.toString(this
                .getClass()
                .getResourceAsStream("/areaPrefixSearchMultipleResult.json"), "UTF-8");

        expectedException.expect(LocationServiceException.class);
        expectedException.expectMessage("Multiple results");
        String areaCode = MapITService.extractAreaCode(json);

        assertEquals("parentCode", "53741", areaCode);
    }

    @Test
    public void testExtractCoordinatesFromAreaCodeGeometrySearch() throws Exception {

        String json = IOUtils.toString(this
                .getClass()
                .getResourceAsStream("/areaCodeGeometrySearchResult.json"), "UTF-8");

        Coordinates coo = MapITService.extractCoordinatesFromAreaCodeGeometrySearch(json);

        assertTrue("Latitude", coo.getLatitude().equals(51.72924834163592));
        assertTrue("Longitude", coo.getLongitude().equals(-2.3000726521821115));
    }

    @Test
    public void testExtractCoordinatesFromPartialPostcodeSearch() throws Exception {

        String json = IOUtils.toString(this
                .getClass()
                .getResourceAsStream("/partialPostcodeSearchResult.json"), "UTF-8");

        Coordinates coo = MapITService.extractCoordinatesFromPartialPostcodeSearch(json);

        assertTrue("Latitude", coo.getLatitude().equals(51.4538601656348));
        assertTrue("Longitude", coo.getLongitude().equals(-2.591766955851817));
    }

}
