package uk.gov.cshr.locationservice.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.cshr.locationservice.LocationServiceException;
import uk.gov.cshr.locationservice.service.CoordinatesService;

@RestController
@RequestMapping(value = "/findlocation", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class LocationServiceController {
    private CoordinatesService googleService;

    public LocationServiceController(CoordinatesService googleService) {
        this.googleService = googleService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{searchTerm}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "findCoordinates", nickname = "Find Coordinates")
    public ResponseEntity<Coordinates> findCoordinates(@PathVariable String searchTerm) {
        try {
            Coordinates coordinates = googleService.findCoordinates(searchTerm);
            return ResponseEntity.ok().body(coordinates);
        } catch (LocationServiceException ex) {
            log.error(searchTerm, ex);
            return ResponseEntity.noContent().build();
        }
    }
}
