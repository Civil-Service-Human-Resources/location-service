package uk.gov.cshr.locationservice.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.cshr.locationservice.LocationServiceApplication;
import uk.gov.cshr.locationservice.controller.Coordinates;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = LocationServiceApplication.class)
@ContextConfiguration
@WebAppConfiguration
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class LocationServiceControllerTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void testPlacename() throws Exception {

        Coordinates coordinates = findCoordinates("London");

        assertTrue("Latitude", coordinates.getLatitude().equals(51.5073509));
        assertTrue("Longitude", coordinates.getLongitude().equals(-0.1277583));
        assertEquals("Region", "Greater London", coordinates.getRegion());
    }

    @Test
    public void testPartialPostcode() throws Exception {

        Coordinates coordinates = findCoordinates("EC2V");
        assertTrue("Latitude", coordinates.getLatitude().equals(51.5156278));
        assertTrue("Longitude", coordinates.getLongitude().equals(-0.0931996));
        assertEquals("Region", "Greater London", coordinates.getRegion());
    }

    @Test
    public void testFullPostcode() throws Exception {

        Coordinates coordinates = findCoordinates("BS16JS");

        assertTrue("Latitude", coordinates.getLatitude().equals(51.4511671));
        assertTrue("Longitude", coordinates.getLongitude().equals(-2.5881766));
        assertEquals("Region", "South West", coordinates.getRegion());
    }

    @Test
    public void testRegions() throws Exception {
        assertEquals("Region", "Greater London", findCoordinates("London").getRegion());
        assertEquals("Region", "South West", findCoordinates("BS1").getRegion());
        assertEquals("Region", "Greater London", findCoordinates("SW1A 2BQ").getRegion());
        assertEquals("Region", "Scotland", findCoordinates("EH12 9DN").getRegion());
        assertEquals("Region", "Scotland", findCoordinates("Edinburgh").getRegion());
        assertEquals("Region", "East Midlands", findCoordinates("LE16").getRegion());
        assertEquals("Region", "South West", findCoordinates("yeovil").getRegion());
        assertEquals("Region", "South West", findCoordinates("wherrytown").getRegion());
        assertEquals("Region", "South West", findCoordinates("penzance").getRegion());
        assertEquals("Region", "South West", findCoordinates("penzance").getRegion());
        assertEquals("Region", "Scotland", findCoordinates("kirkwall").getRegion());
    }

    @Test
    public void testNoResults() throws Exception {

        ResultActions sendRequest = mockMvc.perform(get("/findlocation/noresult")
                .with(user("username")
                        .password("password")
                        .roles("USER")));
        sendRequest.andExpect(status().isOk());
    }

    @Test
    public void testInvalidLocations() throws Exception {

        ResultActions sendRequest = mockMvc.perform(get("/findlocation/b1x")
                .with(user("username")
                        .password("password")
                        .roles("USER")));
        sendRequest.andExpect(status().isNoContent());

        sendRequest = mockMvc.perform(get("/findlocation/bs1invalid")
                .with(user("username")
                        .password("password")
                        .roles("USER")));
        sendRequest.andExpect(status().isNoContent());
    }

    Coordinates findCoordinates(String searchTerm) throws Exception {

        String path = "/findlocation/" + searchTerm;

        ResultActions sendRequest = mockMvc.perform(get(path)
                .with(user("username")
                        .password("password")
                        .roles("USER")));

        sendRequest.andExpect(status().isOk());
        MvcResult mvcResult = sendRequest.andReturn();
        String result = mvcResult.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        Coordinates actualObj = mapper.readValue(result, Coordinates.class);
        return actualObj;
    }
}