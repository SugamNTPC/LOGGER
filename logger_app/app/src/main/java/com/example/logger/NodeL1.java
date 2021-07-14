package com.example.logger;

public class NodeL1 {
    private String mnodeId = "";
    private String mnodeName="";
    private String mlevelLeaf="";
    private String mparentId = "";
    private String mparentName = "";

    public NodeL1(String nodeId, String nodeName, String levelLeaf, String parent_id, String parent_name){
        mnodeId = nodeId;
        mnodeName = nodeName;
        mlevelLeaf = levelLeaf;
        mparentId = parent_id;
        mparentName = parent_name;
    }

    public String getNodeId(){ return mnodeId; }
    public String getNodeName(){ return mnodeName; }
    public String getLevelLeaf(){ return mlevelLeaf; }
    public String getParentId() { return mparentId; }
    public String getParentName(){ return mparentName; }
}
