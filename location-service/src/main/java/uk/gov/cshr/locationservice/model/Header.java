package uk.gov.cshr.locationservice.model;



public class Header{

    private static final String FIELD_MAXRESULTS = "maxresults";
    private static final String FIELD_OFFSET = "offset";
    private static final String FIELD_QUERY = "query";
    private static final String FIELD_URI = "uri";
    private static final String FIELD_FORMAT = "format";
    private static final String FIELD_TOTALRESULTS = "totalresults";


    private int mMaxresult;
    private int mOffset;
    private String mQuery;
    private String mUri;
    private String mFormat;
    private int mTotalresult;


    public Header(){

    }

    public void setMaxresult(int maxresult) {
        mMaxresult = maxresult;
    }

    public int getMaxresult() {
        return mMaxresult;
    }

    public void setOffset(int offset) {
        mOffset = offset;
    }

    public int getOffset() {
        return mOffset;
    }

    public void setQuery(String query) {
        mQuery = query;
    }

    public String getQuery() {
        return mQuery;
    }

    public void setUri(String uri) {
        mUri = uri;
    }

    public String getUri() {
        return mUri;
    }

    public void setFormat(String format) {
        mFormat = format;
    }

    public String getFormat() {
        return mFormat;
    }

    public void setTotalresult(int totalresult) {
        mTotalresult = totalresult;
    }

    public int getTotalresult() {
        return mTotalresult;
    }


}