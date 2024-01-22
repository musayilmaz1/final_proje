package com.example.final_proje;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.final_proje.ui.AddPhotoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button btn_login,btn_register;
    EditText et_email,et_password;
    private FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        // splash screende kullan kullanici girmismi kontrol
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_register=findViewById(R.id.btn_register);
        btn_login=findViewById(R.id.btn_login);
        et_email=findViewById(R.id.et_email);
        et_password=findViewById(R.id.et_password);
        mAuth = FirebaseAuth.getInstance();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password;
                email=et_email.getText().toString();
                password=et_password.getText().toString();
                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"Email ve sifre alanini doldurunuz",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(LoginActivity.this, "Geçerli bir e-posta adresi girin", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Authentication sucsess.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(getApplicationContext(), DrawerActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {

                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }
}