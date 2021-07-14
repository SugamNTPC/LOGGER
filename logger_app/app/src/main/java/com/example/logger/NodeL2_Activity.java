package com.example.logger;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class NodeL2_Activity extends AppCompatActivity {
    private String parent_nodeId;
    private String parent_name;
    private String view_only;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_l2);

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
        else {
            view_only = "";
        }

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetch_all_L0s();
                ProgressBar refreshBar = (ProgressBar) findViewById(R.id.nodeL2_refresh_bar);
                refreshBar.setVisibility(View.VISIBLE);
                pullToRefresh.setRefreshing(false);
            }
        });

        fetch_all_L0s();
        TextView nodeL2TextView = findViewById(R.id.nodel2_title);
        nodeL2TextView.setText(parent_name);

        TextView nodeTextView = findViewById(R.id.nodeL2_text);
        nodeTextView.setText("parent_node : " + parent_nodeId);
        get_nodeL1(parent_nodeId, parent_name, view_only);
        get_nodeL0(parent_nodeId, view_only);

        final Button nodeL2submit_btnView = findViewById(R.id.nodeL2_submit);
        final ProgressBar nodeL2progressBar = findViewById(R.id.nodeL2_progressBar);

        if(view_only.equals("1")){
            nodeL2submit_btnView.setVisibility(View.GONE);
        }

        setRoundedDrawable(nodeL2submit_btnView, getResources().getColor(R.color.buttonColor));
        nodeL2submit_btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nodeL2submit_btnView.setEnabled(false);
                nodeL2progressBar.setVisibility(View.VISIBLE);
                nodeL2submit_btnView.setFocusable(true);
                nodeL2submit_btnView.setFocusableInTouchMode(true);///add this line
                nodeL2submit_btnView.requestFocus();
                Log.v("NODEL2TAG", "Submit " + parent_nodeId);
                final Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                ArrayList<StructureData> structureData = new ArrayList<>();
                final RealmResults<Structure> structures = realm.where(Structure.class).equalTo("parent", parent_nodeId).equalTo("level_leaf","L0").findAll();
                final RealmResults<Structure> structures1 = realm.where(Structure.class).equalTo("parent", parent_nodeId).equalTo("level_leaf","L1").findAll();
                for (int j=0;j<structures1.size();j++){
                    String node_id = structures1.get(j).getId();
                    final RealmResults<Structure> structures2 = realm.where(Structure.class).equalTo("parent", node_id).equalTo("level_leaf","L0").findAll();
                    for(int l=0; l<structures2.size();l++){
                        Structure currentStructure = structures2.get(l);
                        String id = currentStructure.getId();
                        String name = currentStructure.getName();
                        String value = currentStructure.getValue();
                        structureData.add(new StructureData(id, name, value));
                    }
                }
                for(int i=0; i<structures.size();i++){
                    Structure currentStructure = structures.get(i);
                    String id = currentStructure.getId();
                    String name = currentStructure.getName();
                    String value = currentStructure.getValue();
                    structureData.add(new StructureData(id, name, value));
                }
                User user = realm.where(User.class).equalTo("id", 1).findFirst();
                String logger_id = user.getPhone();
                String logger_name = user.getName();
                realm.close();
                for(int k=0;k<structureData.size();k++){
                    Log.v("NODEL2TAG", structureData.get(k).getName());
                }

                JSONArray array=new JSONArray();
                for(int k=0;k<structureData.size();k++){
                    JSONObject obj=new JSONObject();
                    try {
                        StructureData structureData1 = structureData.get(k);
                        obj.put("id",structureData1.getId());
                        obj.put("value",structureData1.getValue());
                        obj.put("logger_id", logger_id);
                        obj.put("logger_name", logger_name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    array.put(obj);
                }

                Log.v("NODEL2TAG",array.toString());
//                update_data(array, nodeL2submit_btnView, nodeL2progressBar);
                check_access(array, nodeL2submit_btnView, nodeL2progressBar);
            }
        });

    }

    //Function to create rounded rectangles
    public static void setRoundedDrawable(View view, int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(20f);
        shape.setColor(backgroundColor);
        view.setBackgroundDrawable(shape);
    }

    public void get_nodeL0(final String parent_node_id, final String view_only){
        final Realm realm = Realm.getDefaultInstance();
        String logger_name = "default";
        String logger_phone = "default";
        String entry_time = "default";
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
                logger_name = currentStructure.getLoggerName();
                logger_phone = currentStructure.getLoggerPhone();
                entry_time = currentStructure.getEntryTime();
                NodeL0 n1 = new NodeL0(id, name, dtype, slider_entries, lim_low, lim_high, disable_entry, hint_text, default_value, value, unit);
                nodeList.add(n1);
//                Toast.makeText(NodeL2_Activity.this, id + "Created", Toast.LENGTH_LONG).show();
                Log.v("NODETAG", id);
            }
            realm.close();
            TextView nodeL2LoggerName = findViewById(R.id.nodeL2_loggerName);
            final TextView nodeL2LoggerPhone = findViewById(R.id.nodeL2_loggerPhone);
            TextView nodeL2EntryTime = findViewById(R.id.nodeL2_updationTime);
            nodeL2LoggerName.setText(logger_name);
            nodeL2LoggerPhone.setText(logger_phone);
            nodeL2EntryTime.setText("Last updated at " +  entry_time);
            nodeL2LoggerPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+91" + nodeL2LoggerPhone.getText()));// Initiates the Intent
                    startActivity(intent);
                }
            });

            if(view_only.equals("1")){
                NodeL0DispAdapter nodeAdapter = new NodeL0DispAdapter(NodeL2_Activity.this, nodeList);
                ListView nodelistView = (ListView) findViewById(R.id.nodeL2_L0_list);
                nodelistView.setAdapter(nodeAdapter);
                Utilities.setListViewHeightBasedOnItems(nodelistView);
            }
            else {
                NodeL0Adapter nodeAdapter = new NodeL0Adapter(NodeL2_Activity.this, nodeList, view_only);
                ListView nodelistView = (ListView) findViewById(R.id.nodeL2_L0_list);
                nodelistView.setAdapter(nodeAdapter);
                Utilities.setListViewHeightBasedOnItems(nodelistView);
            }
        }
        realm.close();
    }

    public void get_nodeL1(final String parent_node_id, final String parent_name, final String view_only){
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final RealmResults<Structure> structures = realm.where(Structure.class).equalTo("parent", parent_node_id).equalTo("level_leaf","L1").findAll();
        if(structures.size()>0){
            ArrayList<NodeL1> nodeList = new ArrayList<NodeL1>();
            for(int i=0; i<structures.size(); i++){
                Structure currentStructure = structures.get(i);
                String id = currentStructure.getId();
                String name = currentStructure.getName();
                String level_leaf = currentStructure.getLevelLeaf();
                String parent_id = currentStructure.getParent();
                NodeL1 n1 = new NodeL1(id, name, level_leaf, parent_id, parent_name);
                nodeList.add(n1);
            }
            realm.close();
            NodeL1Adapter nodeAdapter = new NodeL1Adapter(NodeL2_Activity.this, nodeList, view_only);
            ListView nodelistView = (ListView) findViewById(R.id.nodeL2_L1_list);
            nodelistView.setAdapter(nodeAdapter);
            Utilities.setListViewHeightBasedOnItems(nodelistView);
        }
        realm.close();
    }

    public void update_data(final JSONArray array, final Button nodeL2SubmitButtonView, final ProgressBar nodeL2ProgressBar) {
        final LoggerApplication loggerApp = ((LoggerApplication) getApplicationContext());
        String server_ip = loggerApp.get_Server_IP();
        final String url = "http://" + server_ip + "/api/data/update";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("NODEL2TAG", response);
                        if(response.equals("\"Success\"")) {
                            Toast.makeText(NodeL2_Activity.this, "Success", Toast.LENGTH_SHORT).show();
                            nodeL2SubmitButtonView.setEnabled(true);
                            nodeL2ProgressBar.setVisibility(View.GONE);
                            final LoggerApplication loggerApp = ((LoggerApplication) getApplicationContext());
                            final String init_node_id = loggerApp.get_InitNode_Id();
                            Intent nodeIntent = new Intent(getApplicationContext(), NodeActivity.class);
                            nodeIntent.putExtra("node_id", init_node_id);
                            nodeIntent.putExtra("view_only", "1");
                            startActivity(nodeIntent);
                        }
                        else{
                            Toast.makeText(NodeL2_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                            nodeL2SubmitButtonView.setEnabled(true);
                            nodeL2ProgressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NodeL2_Activity.this, "Server issue", Toast.LENGTH_SHORT)
                                .show();
                        nodeL2SubmitButtonView.setEnabled(true);
                        nodeL2ProgressBar.setVisibility(View.GONE);
//                        homeTextView.setText("Server issue on " + url + "\n" + error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("array", array.toString());
                return params;
            }
        }
                ;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void check_access(final JSONArray array, final Button nodeL2SubmitButtonView, final ProgressBar nodeL2ProgressBar) {
        final LoggerApplication loggerApp = ((LoggerApplication) getApplicationContext());
        String server_ip = loggerApp.get_Server_IP();
        final String url = "http://" + server_ip + "/api/user/check_access";
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        User user = realm.where(User.class).equalTo("id", 1).findFirst();
        final String phone = user.getPhone();
        realm.close();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("null")) {
                            Toast.makeText(NodeL2_Activity.this, "App access revoked from user", Toast.LENGTH_LONG).show();
                            final Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            User user = realm.where(User.class).equalTo("id", 1).findFirst();
                            user.setLoginStatus("N");
                            realm.copyToRealm(user);
                            realm.commitTransaction();
                            realm.close();
                            Intent loginIntent = new Intent(NodeL2_Activity.this , LoginActivity.class);
                            startActivity(loginIntent);
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String app_access = jsonObject.getString("app_access");
                                if (app_access.equals("Y")) {
                                    update_data(array, nodeL2SubmitButtonView, nodeL2ProgressBar);
                                }
                                else{
                                    Toast.makeText(NodeL2_Activity.this, "App access revoked from user", Toast.LENGTH_LONG).show();
                                    final Realm realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                    User user = realm.where(User.class).equalTo("id", 1).findFirst();
                                    user.setLoginStatus("N");
                                    realm.copyToRealm(user);
                                    realm.commitTransaction();
                                    realm.close();
                                    Intent loginIntent = new Intent(NodeL2_Activity.this , LoginActivity.class);
                                    startActivity(loginIntent);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(NodeL2_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NodeL2_Activity.this, "Server issue", Toast.LENGTH_SHORT)
                                .show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", phone);
                return params;
            }
        }
                ;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void fetch_all_L0s() {
        final LoggerApplication loggerApp = ((LoggerApplication) getApplicationContext());
        String server_ip = loggerApp.get_Server_IP();
        final String url = "http://" + server_ip + "/api/structure/get_all_L0_from_L2";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(NodeL2_Activity.this, response.toString(), Toast.LENGTH_SHORT).show();
                          try {
                                JSONArray jsonArray = new JSONArray(response);
                                fetch_L0_data(jsonArray);
                            } catch (JSONException e) {
                                Toast.makeText(NodeL2_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NodeL2_Activity.this, "Server issue", Toast.LENGTH_SHORT)
                                .show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parent", parent_nodeId);
                return params;
            }
        }
                ;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void fetch_L0_data(final JSONArray L0_resp) {
        final LoggerApplication loggerApp = ((LoggerApplication) getApplicationContext());
        String server_ip = loggerApp.get_Server_IP();
//        Toast.makeText(NodeL2_Activity.this, L0_resp.toString(), Toast.LENGTH_LONG).show();
        final String url = "http://" + server_ip + "/api/data/get_latest_data_id";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            Toast.makeText(NodeL2_Activity.this, response.toString(), Toast.LENGTH_LONG).show();
                            final Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            JSONArray jsonArray = new JSONArray(response);
                            String resp = "";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                String id = jsonObject.getString("id");
                                String value = jsonObject.getString("value");
                                String logger_name = jsonObject.getString("logger_name");
                                String logger_phone = jsonObject.getString("logger_id");
                                String entry_time = jsonObject.getString("entry_time");
                                try {
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                    Date date = dateFormat.parse(entry_time);
                                    DateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    entry_time = newFormat.format(date);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                resp += id + " : " + value + "\n";
                                Structure str_edit = realm.where(Structure.class).equalTo("_id", id).findFirst();
                                if(str_edit!=null){
                                    str_edit.setValue(value);
                                    str_edit.setLoggerName(logger_name);
                                    str_edit.setLoggerPhone(logger_phone);
                                    str_edit.setEntryTime(entry_time);
                                }
                            }
                            realm.commitTransaction();
                            realm.close();
                            get_nodeL1(parent_nodeId, parent_name, view_only);
                            get_nodeL0(parent_nodeId, view_only);
                            ProgressBar refreshBar = (ProgressBar) findViewById(R.id.nodeL2_refresh_bar);
                            refreshBar.setVisibility(View.GONE);
//
                        } catch (JSONException e) {
                            Toast.makeText(NodeL2_Activity.this, "Error Occured" + e.toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NodeL2_Activity.this, "Server issue", Toast.LENGTH_SHORT)
                                .show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("l0_ids", L0_resp.toString());
                return params;
            }
        }
                ;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}
