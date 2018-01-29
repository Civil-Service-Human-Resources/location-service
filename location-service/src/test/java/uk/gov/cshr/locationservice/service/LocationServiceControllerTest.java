package uk.gov.cshr.locationservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import uk.gov.cshr.locationservice.LocationServiceApplication;
import uk.gov.cshr.locationservice.LocationServiceException;
import uk.gov.cshr.locationservice.controller.Coordinates;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = LocationServiceApplication.class)
@ContextConfiguration
@WebAppConfiguration
//@RunWith(MockitoJUnitRunner.class)
public class LocationServiceControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testAreaPrefix() throws Exception {

        Coordinates coordinates = findCoordinates("BRISTO");

        assertTrue("Latitude", coordinates.getLatitude().equals(51.4538601656348));
        assertTrue("Longitude", coordinates.getLongitude().equals(-2.591766955851817));
    }

    @Test
    public void testPartialPostcode() throws Exception {

        Coordinates coordinates = findCoordinates("BS1");

        assertTrue("Latitude", coordinates.getLatitude().equals(51.4538601656348));
        assertTrue("Longitude", coordinates.getLongitude().equals(-2.591766955851817));
    }

    @Test
    public void testFullPostcode() throws Exception {

        Coordinates coordinates = findCoordinates("BS16JS");

        assertTrue("Latitude", coordinates.getLatitude().equals(51.4538601656348));
        assertTrue("Longitude", coordinates.getLongitude().equals(-2.591766955851817));
    }

    @Test
    @Ignore
    public void testTwoCharSearch() throws Exception {

        try {
            Coordinates coordinates = findCoordinates("BA");
        }
        catch (LocationServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNoResults() throws Exception {

        ResultActions sendRequest = mockMvc.perform(get("/findlocation/xyz"));
        sendRequest.andExpect(status().isNotFound());
    }

    Coordinates findCoordinates(String searchTerm) throws Exception {

        String path = "/findlocation/" + searchTerm;

        ResultActions sendRequest = mockMvc.perform(get(path));

        sendRequest.andExpect(status().isOk());
        MvcResult mvcResult = sendRequest.andReturn();
        String result = mvcResult.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(result);

        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(actualObj.get("latitude").asDouble());
        coordinates.setLongitude(actualObj.get("longitude").asDouble());

        return coordinates;
    }
}