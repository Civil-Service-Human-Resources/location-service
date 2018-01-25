package uk.gov.cshr.locationservice.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/locationservice", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationServiceController {

    private static final Logger log = LoggerFactory.getLogger(LocationServiceController.class);

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "test", nickname = "test")
    public ResponseEntity test(Pageable pageable) {
        return ResponseEntity.ok().build();
    }
}
