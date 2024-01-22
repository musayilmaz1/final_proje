package com.example.final_proje.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_proje.LoginActivity;
import com.example.final_proje.R;
import com.google.firebase.auth.FirebaseAuth;


public class LogoutnewFragment extends Fragment {


    FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        return inflater.inflate(R.layout.fragment_logoutnew, container, false);
    }
}