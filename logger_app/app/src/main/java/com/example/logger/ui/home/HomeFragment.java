package com.example.logger.ui.home;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.logger.HomeActivity;
import com.example.logger.LoggerApplication;
import com.example.logger.LoginActivity;
import com.example.logger.NodeActivity;
import com.example.logger.NodeL2_Activity;
import com.example.logger.R;
import com.example.logger.Structure;
import com.example.logger.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        final ImageView fillDataButtonView = root.findViewById(R.id.home_fetch_btn);
        final ImageView syncDataButtonView = root.findViewById(R.id.home_sync_button);
        final LinearLayout homeProgressView = root.findViewById(R.id.home_progressBarView);
        final TextView homeProgressTextView = root.findViewById(R.id.home_progress_label);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                final LoggerApplication loggerApp = ((LoggerApplication) getActivity().getApplicationContext());
                final String init_node_id = loggerApp.get_InitNode_Id();
                fillDataButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        int selectedColor = R.color.colorAccent;
//                        setRoundedDrawable(fillDataButtonView, getContext().getResources().getColor(selectedColor));
                        Intent nodeIntent = new Intent(getContext(), NodeActivity.class);
                        nodeIntent.putExtra("node_id", init_node_id);
                        startActivity(nodeIntent);
                    }
                });

                syncDataButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        homeProgressView.setVisibility(View.VISIBLE);
                        homeProgressTextView.setText("Checking User access");
//                        fetch_data(homeProgressView, homeProgressTextView, init_node_id);
                        check_access(homeProgressView, homeProgressTextView, init_node_id);
                    }
                });

            }
        });

        setRoundedDrawable(fillDataButtonView, getResources().getColor(R.color.uptickBackground));
        setRoundedDrawable(syncDataButtonView, getResources().getColor(R.color.downtickBackground));
        return root;
    }

    public void fetch_data(final LinearLayout homeProgressView, final TextView homeProgressTextView, final String init_node_id) {
        final LoggerApplication loggerApp = ((LoggerApplication) getActivity().getApplicationContext());
        String server_ip = loggerApp.get_Server_IP();
        final String url = "http://" + server_ip + "/api/data/get_latest_data";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.v("HOMEFRAG", response.toString());
                            final Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            JSONArray jsonArray = new JSONArray(response);
                            String resp = "";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                String id = jsonObject.getString("_id");
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
//                            textView.setText(resp);
                            homeProgressView.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Data received from server",Toast.LENGTH_SHORT).show();
                            realm.commitTransaction();
                            realm.close();
                            Intent nodeIntent = new Intent(getContext(), NodeActivity.class);
                            nodeIntent.putExtra("node_id", init_node_id);
                            nodeIntent.putExtra("view_only", "1");
                            startActivity(nodeIntent);
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error Occured" + e.toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Server issue", Toast.LENGTH_SHORT)
                                .show();
//                        homeTextView.setText("Server issue on " + url + "\n" + error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void check_access(final LinearLayout homeProgressView, final TextView homeProgressTextView, final String init_node_id) {
        final LoggerApplication loggerApp = ((LoggerApplication) getActivity().getApplicationContext());
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
                            Toast.makeText(getContext(), "App access revoked from user", Toast.LENGTH_LONG).show();
                            final Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            User user = realm.where(User.class).equalTo("id", 1).findFirst();
                            user.setLoginStatus("N");
                            realm.copyToRealm(user);
                            realm.commitTransaction();
                            realm.close();
                            Intent loginIntent = new Intent(getContext() , LoginActivity.class);
                            startActivity(loginIntent);
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String app_access = jsonObject.getString("app_access");
                                if (app_access.equals("Y")) {
//                                    fetch_data(homeProgressView, homeProgressTextView, init_node_id);
                                    Intent nodeIntent = new Intent(getContext(), NodeActivity.class);
                                    nodeIntent.putExtra("node_id", init_node_id);
                                    nodeIntent.putExtra("view_only", "1");
                                    startActivity(nodeIntent);

                                }
                                else{
                                    Toast.makeText(getContext(), "App access revoked from user", Toast.LENGTH_LONG).show();
                                    final Realm realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                    User user = realm.where(User.class).equalTo("id", 1).findFirst();
                                    user.setLoginStatus("N");
                                    realm.copyToRealm(user);
                                    realm.commitTransaction();
                                    realm.close();
                                    Intent loginIntent = new Intent(getContext() , LoginActivity.class);
                                    startActivity(loginIntent);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Server issue", Toast.LENGTH_SHORT)
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }



    //Function to create rounded rectangles
    public static void setRoundedDrawable(View view, int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(120f);
        shape.setColor(backgroundColor);
        view.setBackgroundDrawable(shape);
    }
}