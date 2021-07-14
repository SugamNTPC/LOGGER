package com.example.logger;

public class Node {
    private String mnodeId = "";
    private String mnodeName="";
    private String mlevelLeaf="";

    public Node(String nodeId, String nodeName, String levelLeaf){
        mnodeId = nodeId;
        mnodeName = nodeName;
        mlevelLeaf = levelLeaf;
    }

    public String getNodeId(){ return mnodeId; }
    public String getNodeName(){ return mnodeName; }
    public String getLevelLeaf(){ return mlevelLeaf; }
}
