package com.example.logger;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class NodeL1_Activity extends AppCompatActivity {
    private String parent_nodeId;
    private String grandparent_nodeId;
    private String grandparent_name;
    private String parent_name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_l1);

        if(getIntent().hasExtra("node_id")){
            parent_nodeId = getIntent().getExtras().getString("node_id");
        }
        else{
            parent_nodeId = "na";
        }
        if(getIntent().hasExtra("parent_id")){
            grandparent_nodeId = getIntent().getExtras().getString("parent_id");
        }
        else{
            grandparent_nodeId = "na";
        }
        if(getIntent().hasExtra("parent_name")){
            grandparent_name = getIntent().getExtras().getString("parent_name");
        }
        else{
            grandparent_name = "na";
        }
        if(getIntent().hasExtra("node_name")){
            parent_name = getIntent().getExtras().getString("node_name");
        }
        else {
            parent_name = "";
        }

        TextView nodeL1TextView = findViewById(R.id.nodel1_title);
        nodeL1TextView.setText(parent_name);

        TextView nodeTextView = findViewById(R.id.nodeL1_text);
        nodeTextView.setText("parent_node : " + parent_nodeId);
        get_nodeL0(parent_nodeId);

        final Button proceedBtnView = findViewById(R.id.nodeL1_proceed);
        proceedBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedBtnView.setFocusable(true);
                proceedBtnView.setFocusableInTouchMode(true);///add this line
                proceedBtnView.requestFocus();
                Intent nodeL2Intent = new Intent(NodeL1_Activity.this, NodeL2_Activity.class);
                nodeL2Intent.putExtra("node_id", grandparent_nodeId);
                nodeL2Intent.putExtra("node_name", grandparent_name);
                startActivity(nodeL2Intent);
            }
        });

        setRoundedDrawable(proceedBtnView, getResources().getColor(R.color.buttonColor));
    }

    //Function to create rounded rectangles
    public static void setRoundedDrawable(View view, int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(20f);
        shape.setColor(backgroundColor);
        view.setBackgroundDrawable(shape);
    }

    public void get_nodeL0(final String parent_node_id){
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final RealmResults<Structure> structures = realm.where(Structure.class).equalTo("parent", parent_node_id).equalTo("level_leaf","L0").findAll();
        if(structures.size()>0){
            ArrayList<NodeL0> nodeList = new ArrayList<NodeL0>();
            for(int i=0; i<structures.size(); i++){
                Structure currentStructure = structures.get(i);
                String id = currentStructure.getId();
                String name = currentStructure.getName();
                String dtype = currentStructure.getDtype();
                String slider_entries = currentStructure.getSliderEntries();
                String lim_low = currentStructure.getLowLim();
                String lim_high = currentStructure.getHighLim();
                String disable_entry = currentStructure.getDisable_entry();
                String hint_text = currentStructure.getHintText();
                String default_value = currentStructure.getDefault_value();
                String value = currentStructure.getValue();
                String unit = currentStructure.getUnit();
                NodeL0 n1 = new NodeL0(id, name, dtype, slider_entries, lim_low, lim_high, disable_entry, hint_text, default_value, value, unit);
                nodeList.add(n1);
            }
            realm.close();
            NodeL0Adapter nodeAdapter = new NodeL0Adapter(getApplicationContext(), nodeList, "0");
            ListView nodelistView = (ListView) findViewById(R.id.nodeL1_L0_list);
            nodelistView.setAdapter(nodeAdapter);
            Utilities.setListViewHeightBasedOnItems(nodelistView);
        }
    }

}
