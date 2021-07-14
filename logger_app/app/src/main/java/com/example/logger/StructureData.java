package com.example.logger;

public class StructureData {
    private String mid;
    private String mname;
    private String mvalue;

    public StructureData(String id, String name, String value){
        mid = id;
        mname = name;
        mvalue = value;
    }

    public String getId() { return mid; }
    public String getName() { return mname; }
    public String getValue() { return mvalue; }

}
