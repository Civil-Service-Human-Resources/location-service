package uk.gov.cshr.locationservice.model.postcode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostcodeResult {

    private String northings;

    private String outcode;

    private String[] admin_ward;

    private String[] parish;

    private String[] admin_district;

    private String longitude;

    private String eastings;

    private String latitude;

    private String[] admin_county;

    private String[] country;

    public String getNorthings() {
        return northings;
    }

    public void setNorthings(String northings) {
        this.northings = northings;
    }

    public String getOutcode() {
        return outcode;
    }

    public void setOutcode(String outcode) {
        this.outcode = outcode;
    }

    public String[] getAdmin_ward() {
        return admin_ward;
    }

    public void setAdmin_ward(String[] admin_ward) {
        this.admin_ward = admin_ward;
    }

    public String[] getParish() {
        return parish;
    }

    public void setParish(String[] parish) {
        this.parish = parish;
    }

    public String[] getAdmin_district() {
        return admin_district;
    }

    public void setAdmin_district(String[] admin_district) {
        this.admin_district = admin_district;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEastings() {
        return eastings;
    }

    public void setEastings(String eastings) {
        this.eastings = eastings;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String[] getAdmin_county() {
        return admin_county;
    }

    public void setAdmin_county(String[] admin_county) {
        this.admin_county = admin_county;
    }

    public String[] getCountry() {
        return country;
    }

    public void setCountry(String[] country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "ClassPojo [northings = " + northings + ", outcode = " + outcode + ", admin_ward = " + admin_ward + ", parish = " + parish + ", admin_district = " + admin_district + ", longitude = " + longitude + ", eastings = " + eastings + ", latitude = " + latitude + ", admin_county = " + admin_county + ", country = " + country + "]";
    }
}
