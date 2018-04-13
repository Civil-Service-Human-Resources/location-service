package uk.gov.cshr.locationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.gov.cshr.locationservice.service.RegionLookup;

@SpringBootApplication
public class LocationServiceApplication {

	public static void main(String[] args) {
        RegionLookup.initialise();
        SpringApplication.run(LocationServiceApplication.class, args);        
	}
}
