package com.example.logger;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.realm.Realm;

public class HistoryActivity extends AppCompatActivity {

    private String node_name;
    private String node_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        if(getIntent().hasExtra("node_id")){
            node_id = getIntent().getExtras().getString("node_id");
        }
        else{
            node_id = "na";
        }
        if(getIntent().hasExtra("node_name")){
            node_name = getIntent().getExtras().getString("node_name");
        }
        else{
            node_name = "na";
        }
        fetch_data(node_id, node_name);
    }

    public void fetch_data(final String node_id, final String node_name) {
        final LoggerApplication loggerApp = ((LoggerApplication) getApplicationContext());
        String server_ip = loggerApp.get_Server_IP();
        final String url = "http://" + server_ip + "/api/data/history";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        TextView historyTitleView = findViewById(R.id.history_title);
                        historyTitleView.setText(node_name);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<History> histories = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                String id = jsonObject.getString("_id");
                                String value = jsonObject.getString("value");
                                String datetime = jsonObject.getString("entry_time");
                                String logger_id = jsonObject.getString("logger_id");
                                String logger_name = jsonObject.getString("logger_name");
                                try {
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                    Date date = dateFormat.parse(datetime);
                                    DateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    datetime = newFormat.format(date);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                History history = new History(id, value, datetime, logger_id, logger_name);
                                histories.add(history);
                            }
                            HistoryItemAdapter historyAdapter = new HistoryItemAdapter(HistoryActivity.this, histories);
                            ListView historyListView = findViewById(R.id.history_list);
                            historyListView.setAdapter(historyAdapter);
//
                        } catch (JSONException e) {
                            Toast.makeText(HistoryActivity.this, "Error Occured : " + e.toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HistoryActivity.this, "Server issue", Toast.LENGTH_SHORT)
                                .show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", node_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(HistoryActivity.this);
        requestQueue.add(stringRequest);
    }
}
