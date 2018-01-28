package uk.gov.cshr.locationservice.service;

import java.nio.charset.Charset;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.cshr.locationservice.controller.LocationServiceController;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = LocationServiceApplication.class)
//@ContextConfiguration
//@WebAppConfiguration
@RunWith(MockitoJUnitRunner.class)
public class LocationServiceControllerTest {


    @Mock
    private MapITService mapITService;

    @InjectMocks
    private LocationServiceController locationServiceController;

    private MockMvc mockMvc;

    final private MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(locationServiceController).build();
    }


    @Test
    public void testFindLocation() throws Exception {

        // Given
        String path = "/findlocation/BRIS";

        // When
        ResultActions sendRequest = mockMvc.perform(get(path));

        // Then
        sendRequest
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.coordinates.latitude", is(1)))
                .andExpect(jsonPath("$.coordinates.longitude", is(1)));
    }
}