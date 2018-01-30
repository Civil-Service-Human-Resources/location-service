package uk.gov.cshr.locationservice.service;

import uk.gov.cshr.locationservice.LocationServiceException;
import uk.gov.cshr.locationservice.controller.Coordinates;

public interface CoordinatesService {

    Coordinates findCoordinates(String searchTerm) throws LocationServiceException;
}
