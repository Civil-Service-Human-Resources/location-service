package uk.gov.cshr.locationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import uk.gov.cshr.locationservice.service.RegionLookup;

@SpringBootApplication
@EnableCaching
public class LocationServiceApplication {

    public static void main(String[] args) {
        RegionLookup.initialise();
        SpringApplication.run(LocationServiceApplication.class, args);
    }
}
