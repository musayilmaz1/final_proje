package com.example.final_proje.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.final_proje.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class GaleryFragment extends Fragment {
    FirebaseStorage Storage;
    FirebaseFirestore firestore;
    StorageReference mStorageref;
    LinearLayout linearLayout;
    

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow,container,false);
        firestore=FirebaseFirestore.getInstance();
        Storage=FirebaseStorage.getInstance();
        mStorageref=Storage.getReference();
        linearLayout=root.findViewById(R.id.linearLayout);
        downloadImages();
        return root;
    }

    private void downloadImages() {
        final StorageReference myRef = mStorageref.child("photo/");

        myRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                // Resimleri LinearLayout'a ekle
                for (StorageReference item : listResult.getItems()) {
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            addImageView(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure to get download URL
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure to list files in the "photo" folder
            }
        });
    }

    private void addImageView(String imageUrl) {
        ImageView imageView = new ImageView(requireContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 8, 8, 8);
        imageView.setLayoutParams(layoutParams);
        // Picasso veya Glide gibi kütüphaneleri kullanarak ImageView'e resmi yükleyin
        // Örneğin:
        Picasso.get()
                .load(imageUrl)
                .resize(800, 800) // İstenen genişlik ve yüksekliği burada belirtin
                .centerCrop()
                .into(imageView);


        linearLayout.addView(imageView);
    }
}