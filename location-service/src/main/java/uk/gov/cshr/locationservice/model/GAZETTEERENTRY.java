package uk.gov.cshr.locationservice.model;



public class GAZETTEERENTRY{

    private static final String FIELD_GEOMETRY_Y = "GEOMETRY_Y";
    private static final String FIELD_REGION = "REGION";
    private static final String FIELD_GEOMETRY_X = "GEOMETRY_X";
    private static final String FIELD_COUNTY_UNITARY_URI = "COUNTY_UNITARY_URI";
    private static final String FIELD_COUNTRY = "COUNTRY";
    private static final String FIELD_NAMES_URI = "NAMES_URI";
    private static final String FIELD_MBR_XMIN = "MBR_XMIN";
    private static final String FIELD_POPULATED_PLACE = "POPULATED_PLACE";
    private static final String FIELD_NAME1 = "NAME1";
    private static final String FIELD_MBR_YMIN = "MBR_YMIN";
    private static final String FIELD_LOCAL_TYPE = "LOCAL_TYPE";
    private static final String FIELD_MBR_YMAX = "MBR_YMAX";
    private static final String FIELD_MBR_XMAX = "MBR_XMAX";
    private static final String FIELD_REGION_URI = "REGION_URI";
    private static final String FIELD_ID = "ID";
    private static final String FIELD_POSTCODE_DISTRICT = "POSTCODE_DISTRICT";
    private static final String FIELD_POSTCODE_DISTRICT_URI = "POSTCODE_DISTRICT_URI";
    private static final String FIELD_COUNTY_UNITARY = "COUNTY_UNITARY";
    private static final String FIELD_MOST_DETAIL_VIEW_RES = "MOST_DETAIL_VIEW_RES";
    private static final String FIELD_LEAST_DETAIL_VIEW_RES = "LEAST_DETAIL_VIEW_RES";
    private static final String FIELD_TYPE = "TYPE";
    private static final String FIELD_COUNTRY_URI = "COUNTRY_URI";
    private static final String FIELD_COUNTY_UNITARY_TYPE = "COUNTY_UNITARY_TYPE";
    private static final String FIELD_POPULATED_PLACE_TYPE = "POPULATED_PLACE_TYPE";
    private static final String FIELD_POPULATED_PLACE_URI = "POPULATED_PLACE_URI";


    private double mGEOMETRYY;
    private String mREGION;
    private double mGEOMETRYX;
    private String mCOUNTYUNITARYURI;
    private String mCOUNTRY;
    private String mNAMESURI;
    private double mMBRXMIN;
    private String mPOPULATEDPLACE;
    private String mNAME1;
    private double mMBRYMIN;
    private String mLOCALTYPE;
    private double mMBRYMAX;
    private double mMBRXMAX;
    private String mREGIONURI;
    private String mID;
    private String mPOSTCODEDISTRICT;
    private String mPOSTCODEDISTRICTURI;
    private String mCOUNTYUNITARY;
    private int mMOSTDETAILVIEWRE;
    private int mLEASTDETAILVIEWRE;
    private String mTYPE;
    private String mCOUNTRYURI;
    private String mCOUNTYUNITARYTYPE;
    private String mPOPULATEDPLACETYPE;
    private String mPOPULATEDPLACEURI;


    public GAZETTEERENTRY(){

    }

    public void setGEOMETRYY(double gEOMETRYY) {
        mGEOMETRYY = gEOMETRYY;
    }

    public double getGEOMETRYY() {
        return mGEOMETRYY;
    }

    public void setREGION(String rEGION) {
        mREGION = rEGION;
    }

    public String getREGION() {
        return mREGION;
    }

    public void setGEOMETRYX(double gEOMETRYX) {
        mGEOMETRYX = gEOMETRYX;
    }

    public double getGEOMETRYX() {
        return mGEOMETRYX;
    }

    public void setCOUNTYUNITARYURI(String cOUNTYUNITARYURI) {
        mCOUNTYUNITARYURI = cOUNTYUNITARYURI;
    }

    public String getCOUNTYUNITARYURI() {
        return mCOUNTYUNITARYURI;
    }

    public void setCOUNTRY(String cOUNTRY) {
        mCOUNTRY = cOUNTRY;
    }

    public String getCOUNTRY() {
        return mCOUNTRY;
    }

    public void setNAMESURI(String nAMESURI) {
        mNAMESURI = nAMESURI;
    }

    public String getNAMESURI() {
        return mNAMESURI;
    }

    public void setMBRXMIN(double mBRXMIN) {
        mMBRXMIN = mBRXMIN;
    }

    public double getMBRXMIN() {
        return mMBRXMIN;
    }

    public void setPOPULATEDPLACE(String pOPULATEDPLACE) {
        mPOPULATEDPLACE = pOPULATEDPLACE;
    }

    public String getPOPULATEDPLACE() {
        return mPOPULATEDPLACE;
    }

    public void setNAME1(String nAME1) {
        mNAME1 = nAME1;
    }

    public String getNAME1() {
        return mNAME1;
    }

    public void setMBRYMIN(double mBRYMIN) {
        mMBRYMIN = mBRYMIN;
    }

    public double getMBRYMIN() {
        return mMBRYMIN;
    }

    public void setLOCALTYPE(String lOCALTYPE) {
        mLOCALTYPE = lOCALTYPE;
    }

    public String getLOCALTYPE() {
        return mLOCALTYPE;
    }

    public void setMBRYMAX(double mBRYMAX) {
        mMBRYMAX = mBRYMAX;
    }

    public double getMBRYMAX() {
        return mMBRYMAX;
    }

    public void setMBRXMAX(double mBRXMAX) {
        mMBRXMAX = mBRXMAX;
    }

    public double getMBRXMAX() {
        return mMBRXMAX;
    }

    public void setREGIONURI(String rEGIONURI) {
        mREGIONURI = rEGIONURI;
    }

    public String getREGIONURI() {
        return mREGIONURI;
    }

    public void setID(String iD) {
        mID = iD;
    }

    public String getID() {
        return mID;
    }

    public void setPOSTCODEDISTRICT(String pOSTCODEDISTRICT) {
        mPOSTCODEDISTRICT = pOSTCODEDISTRICT;
    }

    public String getPOSTCODEDISTRICT() {
        return mPOSTCODEDISTRICT;
    }

    public void setPOSTCODEDISTRICTURI(String pOSTCODEDISTRICTURI) {
        mPOSTCODEDISTRICTURI = pOSTCODEDISTRICTURI;
    }

    public String getPOSTCODEDISTRICTURI() {
        return mPOSTCODEDISTRICTURI;
    }

    public void setCOUNTYUNITARY(String cOUNTYUNITARY) {
        mCOUNTYUNITARY = cOUNTYUNITARY;
    }

    public String getCOUNTYUNITARY() {
        return mCOUNTYUNITARY;
    }

    public void setMOSTDETAILVIEWRE(int mOSTDETAILVIEWRE) {
        mMOSTDETAILVIEWRE = mOSTDETAILVIEWRE;
    }

    public int getMOSTDETAILVIEWRE() {
        return mMOSTDETAILVIEWRE;
    }

    public void setLEASTDETAILVIEWRE(int lEASTDETAILVIEWRE) {
        mLEASTDETAILVIEWRE = lEASTDETAILVIEWRE;
    }

    public int getLEASTDETAILVIEWRE() {
        return mLEASTDETAILVIEWRE;
    }

    public void setTYPE(String tYPE) {
        mTYPE = tYPE;
    }

    public String getTYPE() {
        return mTYPE;
    }

    public void setCOUNTRYURI(String cOUNTRYURI) {
        mCOUNTRYURI = cOUNTRYURI;
    }

    public String getCOUNTRYURI() {
        return mCOUNTRYURI;
    }

    public void setCOUNTYUNITARYTYPE(String cOUNTYUNITARYTYPE) {
        mCOUNTYUNITARYTYPE = cOUNTYUNITARYTYPE;
    }

    public String getCOUNTYUNITARYTYPE() {
        return mCOUNTYUNITARYTYPE;
    }

    public void setPOPULATEDPLACETYPE(String pOPULATEDPLACETYPE) {
        mPOPULATEDPLACETYPE = pOPULATEDPLACETYPE;
    }

    public String getPOPULATEDPLACETYPE() {
        return mPOPULATEDPLACETYPE;
    }

    public void setPOPULATEDPLACEURI(String pOPULATEDPLACEURI) {
        mPOPULATEDPLACEURI = pOPULATEDPLACEURI;
    }

    public String getPOPULATEDPLACEURI() {
        return mPOPULATEDPLACEURI;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof GAZETTEERENTRY){
            return ((GAZETTEERENTRY) obj).getID() == mID;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return mID.hashCode();
    }


}