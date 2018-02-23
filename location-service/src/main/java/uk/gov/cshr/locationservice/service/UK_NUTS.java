package uk.gov.cshr.locationservice.service;

/**
 * The Nomenclature of Territorial Units for Statistics (NUTS) UK Regions only
 * https://en.wikipedia.org/wiki/NUTS_1_statistical_regions_of_England
 */
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

    private UK_NUTS(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
