package com.example.logger.ui.syncstruct;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.example.logger.LoggerApplication;
import com.example.logger.R;
import com.example.logger.Structure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;

public class SyncStructFragment extends Fragment {

    private SyncStructViewModel syncStructViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        syncStructViewModel =
                ViewModelProviders.of(this).get(SyncStructViewModel.class);
        View root = inflater.inflate(R.layout.fragment_syncstruct, container, false);
        final TextView textView = root.findViewById(R.id.text_syncstruct);
        syncStructViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                fetch_data(textView);
            }
        });
        return root;
    }

    public void fetch_data(final TextView textView) {
        final LoggerApplication loggerApp = ((LoggerApplication) getActivity().getApplicationContext());
        String server_ip = loggerApp.get_Server_IP();
        final String url = "http://" + server_ip + "/api/structure";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            final RealmResults<Structure> structures = realm.where(Structure.class).findAll();
                            structures.deleteAllFromRealm();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                realm.createObjectFromJson(Structure.class, jsonObject.toString());
                                textView.setText("Sync completed from server");
                            }
                            realm.commitTransaction();
                            realm.close();
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error Occured", Toast.LENGTH_SHORT)
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
        RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
}