package com.example.final_proje.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.final_proje.R;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_proje.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddLabelFragment extends Fragment {

    private EditText editTextLabel;
    private Button buttonAddLabel;
    private LinearLayout linearLayoutLabels;

    private List<String> labelList = new ArrayList<>();

    private static final String PREFS_NAME = "MyPrefs";
    private static final String LABELS_KEY = "labels";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // XML dosyasındaki elementleri tanımla
        editTextLabel = root.findViewById(R.id.editTextLabel);
        buttonAddLabel = root.findViewById(R.id.buttonAddLabel);
        linearLayoutLabels = root.findViewById(R.id.linearLayoutLabels);

        // Labelları SharedPreferences'ten al
        loadLabels();

        // Ekleme tuşuna tıklandığında
        buttonAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText'ten label adını al
                String label = editTextLabel.getText().toString();

                // Eğer label boş değilse, listeye ekle ve SharedPreferences'e kaydet
                if (!label.isEmpty()) {
                    labelList.add(label);
                    addLabelToLayout(label);
                    saveLabels();

                }


            }
        });
        // Kaydedilmiş labelları göster
        for (String label : labelList) {
            addLabelToLayout(label);
        }

        return root;
    }

    private void addLabelToLayout(final String label) {
        // Yeni bir LinearLayout oluştur
        LinearLayout labelLayout = new LinearLayout(getContext());
        labelLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Yeni bir TextView oluştur
        final TextView textViewLabel = new TextView(getContext());
        textViewLabel.setText(label);
        textViewLabel.setTextColor(Color.BLACK);
        textViewLabel.setTextSize(16);
        textViewLabel.setPadding(0, 0, 16, 0);

        // Yeni bir Button oluştur
        Button buttonDeleteLabel = new Button(getContext());
        buttonDeleteLabel.setText("Sil");
        buttonDeleteLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Silme butonuna tıklanınca label'ı kaldır ve SharedPreferences'e kaydet
                labelList.remove(label);
                linearLayoutLabels.removeView(labelLayout);
                saveLabels();
            }
        });

        labelLayout.addView(textViewLabel);
        labelLayout.addView(buttonDeleteLabel);
        linearLayoutLabels.addView(labelLayout);


    }

    // SharedPreferences'e labelları kaydet
    private void saveLabels() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Set tipindeki labelları kaydet
        Set<String> labelSet = new HashSet<>(labelList);
        editor.putStringSet(LABELS_KEY, labelSet);

        editor.apply();
    }

    // SharedPreferences'ten labelları yükle
    private void loadLabels() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Set tipindeki labelları yükle
        Set<String> labelSet = preferences.getStringSet(LABELS_KEY, new HashSet<>());
        labelList = new ArrayList<>(labelSet);
    }
}
