package com.example.logger;

public class History {

    private String mid;
    private String mvalue;
    private String mdatetime;
    private String mloggerId;
    private String mloggerName;


    public History(String id, String value, String datetime, String loggerId, String loggerName){
        mid = id;
        mvalue = value;
        mdatetime = datetime;
        mloggerId = loggerId;
        mloggerName = loggerName;
    }

    public String getId(){ return mid; }
    public String getValue(){ return mvalue; }
    public String getDatetime(){ return mdatetime; }
    public String getLoggerId(){ return mloggerId; }
    public String getLoggerName() { return mloggerName; }

}
