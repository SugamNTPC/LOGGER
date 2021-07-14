package com.example.logger;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
//import android.support.v
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.view.View.MeasureSpec;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class NodeL1Adapter extends ArrayAdapter<NodeL1> {

    private String view_only;
    public NodeL1Adapter(Context context, ArrayList<NodeL1> nodes, String view_only)
    {
        super(context,0, nodes);
        this.view_only = view_only;
    }

    //    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.node_l1_item, parent,false);
        }
        final NodeL1 currentNode = getItem(position);

        int selectedColor = 0;
        if (position % 7 == 0) {
//            selectedColor = R.color.colorRed;
            selectedColor = R.color.colorListItem1;
        }
        if (position % 7 == 1) {
            selectedColor = R.color.colorListItem2;
        }
        if (position % 7 == 2) {
            selectedColor = R.color.colorListItem3;
        }
        if (position % 7 == 3) {
            selectedColor = R.color.colorListItem4;
        }
        if (position % 7 == 4) {
            selectedColor = R.color.colorListItem5;
        }
        if (position % 7 == 5) {
            selectedColor = R.color.colorListItem6;
        }
        if (position % 7 == 6) {
            selectedColor = R.color.colorListItem7;
        }

        TextView nodeId = listItemView.findViewById(R.id.nodeL1_Item_id);
        nodeId.setText(currentNode.getNodeId());

        TextView nodeName = listItemView.findViewById(R.id.nodeL1_Item_name);
        nodeName.setText(currentNode.getNodeName());

//        final ListView nodeL0List = listItemView.findViewById(R.id.nodel1_list);
        String node_id = nodeId.getText().toString();
        get_nodeL0(node_id, listItemView);

        final LinearLayout nodelistParentLayout = listItemView.findViewById(R.id.nodeL1_Item_parentLayout);


        if(view_only.equals("1"));
        else {
            listItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView nodeId = v.findViewById(R.id.nodeL1_Item_id);
                    String node_id = nodeId.getText().toString();
                    TextView nodeName = v.findViewById(R.id.nodeL1_Item_name);
                    String node_name = nodeName.getText().toString();
                    Intent nodeActivityIntent = new Intent(getContext(), NodeL1_Activity.class);
                    nodeActivityIntent.putExtra("parent_id", currentNode.getParentId());
                    nodeActivityIntent.putExtra("parent_name", currentNode.getParentName());
                    nodeActivityIntent.putExtra("node_name", node_name);
                    nodeActivityIntent.putExtra("node_id", node_id);
                    getContext().startActivity(nodeActivityIntent);
                   }
            });
        }
        setRoundedDrawable(nodelistParentLayout,getContext().getResources().getColor(selectedColor));


        return listItemView;
    }

    public void get_nodeL0(final String parent_node_id, View listItemView){
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
            NodeL0DispAdapter nodeAdapter = new NodeL0DispAdapter(getContext(), nodeList);
            ListView nodelistView = listItemView.findViewById(R.id.nodel1_list);
            nodelistView.setAdapter(nodeAdapter);
            Utilities.setListViewHeightBasedOnItems(nodelistView);
        }
    }

    //Function to create rounded rectangles
    public static void setRoundedDrawable(View view, int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(20f);
        shape.setColor(backgroundColor);
        view.setBackgroundDrawable(shape);
    }
}
