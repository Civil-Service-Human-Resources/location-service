package uk.gov.cshr.locationservice.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates {

    private Double latitude;

    private Double longitude;
}
