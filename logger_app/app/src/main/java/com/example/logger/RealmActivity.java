package com.example.logger;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RealmActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm);
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Button realmButton = findViewById(R.id.realm_btn);
        realmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Structure str1 = new Structure();
                    str1.setName("Hello");
                    final Structure strfin = realm.copyToRealm(str1);
                    final RealmResults<Structure> structures = realm.where(Structure.class).findAll();
                    TextView realmTextView = findViewById(R.id.realm_text);
                    realmTextView.setText(structures.asJSON());
                    realm.commitTransaction();
                    Toast.makeText(RealmActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } finally {
//                    Toast.makeText(RealmActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    realm.close();
                }
            }
        });


    }
}
