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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText et_regemail, et_regpassword,et_isim,et_soyisim;
    Button btn_regreg,btn_reglogin;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_reglogin=findViewById(R.id.btn_reglogin);
        btn_regreg=findViewById(R.id.btn_regreg);
        et_isim=findViewById(R.id.et_isim);
        et_soyisim=findViewById(R.id.et_soyisim);
        et_regemail=findViewById(R.id.et_regemail);
        et_regpassword=findViewById(R.id.et_regpassword);
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        btn_reglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_regreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password,isim,soyisim;
                email=et_regemail.getText().toString();
                password=et_regpassword.getText().toString();
                isim=et_isim.getText().toString();
                soyisim=et_soyisim.getText().toString();


                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this,"Email ve sifre alanini doldurunuz",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegisterActivity.this, "Geçerli bir e-posta adresi girin", Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<String,Object> user=new HashMap<>();
                                    user.put("name: ", isim);
                                    user.put("lastname: ",soyisim);
                                    firebaseFirestore.collection("users").document(mAuth.getUid()).set(user);
                                    Toast.makeText(RegisterActivity.this, "Hesap olusturuldu",
                                            Toast.LENGTH_SHORT).show();

                                } else {

                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



            }
        });
    }
}