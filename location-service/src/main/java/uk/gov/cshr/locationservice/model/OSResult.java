package uk.gov.cshr.locationservice.model;



public class OSResult{

    private static final String FIELD_GAZETTEER_ENTRY = "GAZETTEER_ENTRY";


    private GAZETTEERENTRY mGAZETTEERENTRY;


    public OSResult(){

    }

    public void setGAZETTEERENTRY(GAZETTEERENTRY gAZETTEERENTRY) {
        mGAZETTEERENTRY = gAZETTEERENTRY;
    }

    public GAZETTEERENTRY getGAZETTEERENTRY() {
        return mGAZETTEERENTRY;
    }


}