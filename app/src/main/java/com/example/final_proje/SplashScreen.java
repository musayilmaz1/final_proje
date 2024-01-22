package com.example.final_proje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.final_proje.ui.AddLabelFragment;
import com.example.final_proje.ui.AddPhotoFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    Button btn_splogin,btn_spregister;
    private FirebaseAuth mAuth;
    public void onStart() {
        super.onStart();
        // splash screende kullan kullanici girmismi kontrol
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        btn_splogin=findViewById(R.id.btn_splogin);
        btn_spregister=findViewById(R.id.btn_spregister);
        mAuth = FirebaseAuth.getInstance();
        btn_spregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_splogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}