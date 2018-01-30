package uk.gov.cshr.locationservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.assertTrue;
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
import uk.gov.cshr.locationservice.controller.Coordinates;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = LocationServiceApplication.class)
@ContextConfiguration
@WebAppConfiguration
public class LocationServiceControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testPlacename() throws Exception {

        Coordinates coordinates = findCoordinates("London");

        assertTrue("Latitude", coordinates.getLatitude().equals(51.5073509));
        assertTrue("Longitude", coordinates.getLongitude().equals(-0.1277583));
    }

    @Test
    public void testPartialPostcode() throws Exception {

        Coordinates coordinates = findCoordinates("EC2V");
        assertTrue("Latitude", coordinates.getLatitude().equals(51.5156278));
        assertTrue("Longitude", coordinates.getLongitude().equals(-0.0931996));
    }

    @Test
    public void testFullPostcode() throws Exception {

        Coordinates coordinates = findCoordinates("BS16JS");

        assertTrue("Latitude", coordinates.getLatitude().equals(51.4511671));
        assertTrue("Longitude", coordinates.getLongitude().equals(-2.5881766));
    }

    @Test
    public void testNoResults() throws Exception {

        ResultActions sendRequest = mockMvc.perform(get("/findlocation/noresult"));
        sendRequest.andExpect(status().isNoContent());
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