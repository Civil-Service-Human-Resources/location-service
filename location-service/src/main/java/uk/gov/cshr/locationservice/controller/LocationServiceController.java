package uk.gov.cshr.locationservice.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.cshr.locationservice.LocationServiceException;
import uk.gov.cshr.locationservice.service.CoordinatesService;
import uk.gov.cshr.locationservice.service.GoogleService;

@RestController
@RequestMapping(value = "/findlocation", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class LocationServiceController {
    @Autowired
    private CoordinatesService locationService;

    @RequestMapping(method = RequestMethod.GET, value = "/{searchTerm}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "findCoordinates", nickname = "Find Coordinates")
    public ResponseEntity<Coordinates> findCoordinates(@PathVariable String searchTerm) {
        try {
            Coordinates coordinates = locationService.findCoordinates(searchTerm);
            return ResponseEntity.ok().body(coordinates);
        }
        catch (LocationServiceException ex) {
            log.error(searchTerm, ex);
            return ResponseEntity.noContent().build();
        }
    }
}
