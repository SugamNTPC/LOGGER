package com.example.logger;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class NodeActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private String parent_nodeId;
    private String parent_name;
    private String view_only;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getIntent().hasExtra("node_id")){
            parent_nodeId = getIntent().getExtras().getString("node_id");
        }
        else{
            parent_nodeId = "na";
        }

        if(getIntent().hasExtra("node_name")){
            parent_name = getIntent().getExtras().getString("node_name");
        }
        else {
            parent_name = "";
        }

        if(getIntent().hasExtra("view_only")){
            view_only = getIntent().getExtras().getString("view_only");
        }
        else{
            view_only = "N";
        }
//        String server_ip = "3.134.88.27:3000";

        TextView nodeTitleView = findViewById(R.id.node_title);
        nodeTitleView.setText(parent_name);

        TextView nodeTextView = findViewById(R.id.node_text);
        nodeTextView.setText("parent_node : " + parent_nodeId);

        get_node(parent_nodeId, view_only);
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }

    public void get_node(final String parent_node_id, final String view_only){
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final RealmResults<Structure> structures = realm.where(Structure.class).equalTo("parent", parent_node_id).findAll();
        if(structures.size()>0){
            ArrayList<Node> nodeList = new ArrayList<Node>();
            for(int i=0; i<structures.size(); i++){
                Node n1 = new Node(structures.get(i).getId(), structures.get(i).getName(), structures.get(i).getLevelLeaf());
                nodeList.add(n1);
            }
            NodeAdapter nodeAdapter = new NodeAdapter(NodeActivity.this, nodeList, view_only);
            GridView nodegridView = (GridView) findViewById(R.id.node_grid);
            nodegridView.setAdapter(nodeAdapter);
            setGridViewHeightBasedOnChildren(nodegridView, 3);
        }
        realm.close();
    }
}
