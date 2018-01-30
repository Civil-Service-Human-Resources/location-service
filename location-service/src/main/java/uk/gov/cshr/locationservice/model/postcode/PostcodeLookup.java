package uk.gov.cshr.locationservice.model.postcode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostcodeLookup {

    private PostcodeResult result;

    private String status;

    public PostcodeResult getResult() {
        return result;
    }

    public void setResult(PostcodeResult result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassPojo [result = " + result + ", status = " + status + "]";
    }
}
