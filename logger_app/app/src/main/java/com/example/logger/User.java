package com.example.logger;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject {

    @PrimaryKey
    private int id;

    private String phone;
    private String name;
    private String root_node;
    private String root_node_name;
    private String login_status;

    public int getId(){ return id; }
    public String getPhone(){ return phone; }
    public String getName(){ return name; }
    public String getRootNode(){ return root_node; }
    public String getRootNodeName(){ return root_node_name; }
    public String getLoginStatus(){ return login_status; }

    public void setId(int id){ this.id = id; }
    public void setPhone(String phone){ this.phone = phone; }
    public void setName(String name){ this.name = name; }
    public void setRootNode(String root_node){ this.root_node = root_node; }
    public void setRootNodeName(String root_node_name){ this.root_node_name = root_node_name; }
    public void setLoginStatus(String login_status){ this.login_status = login_status; }



}
