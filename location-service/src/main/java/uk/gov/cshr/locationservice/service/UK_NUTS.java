package uk.gov.cshr.locationservice.service;

public enum UK_NUTS {

    UKC("North East"),
    UKD("North West"),
    UKE("Yorkshire and the Humber"),
    UKF("East Midlands"),
    UKG("West Midlands"),
    UKH("East of England"),
    UKI("Greater London"),
    UKJ("South East"),
    UKK("South West"),
    UKL("Wales"),
    UKM("Scotland"),
    UKN("Northern Ireland");

    final private String name;

    UK_NUTS(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
