package com.example.final_proje.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.final_proje.DrawerActivity;
import com.example.final_proje.R;
import com.example.final_proje.databinding.FragmentGalleryBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import io.grpc.Context;

public class AddPhotoFragment extends Fragment {
    ImageView selectedImage;
    Button btn_camera ,btn_upload;
    private Uri ImageUri;
    FirebaseStorage Storage;
    FirebaseFirestore firestore;
    StorageReference mStorageref;
    private ImageView Image;
    private Bitmap bitmap,image;
    private String PhotoUri;
    TextView tv_add_pho;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery,container,false);
        btn_camera=root.findViewById(R.id.btn_camera);
        selectedImage=root.findViewById(R.id.iv_camera);
        btn_upload=root.findViewById(R.id.btn_upload);
        tv_add_pho=root.findViewById(R.id.tv_addpho);
        firestore=FirebaseFirestore.getInstance();
        Storage=FirebaseStorage.getInstance();
        mStorageref=Storage.getReference();

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }
        });



        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();

            }
        });




        return root;

    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 100);
        }else{
            openCamera();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(requireActivity(), "Camera izni verilmedi. Bazı özellikler devre dışı olabilir.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,102);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==102){

            image= (Bitmap) data.getExtras().get("data");
            selectedImage.setImageBitmap(image);
            ImageUri = getImageUriFromBitmap(image);

        }
    }
    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        String uniqueFileName = "temp_image_" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(requireContext().getExternalFilesDir(null), uniqueFileName);

        try {
            imageFile.createNewFile();
            // Dosyaya bitmap'i yaz
            imageFile = bitmapToFile(bitmap, imageFile);

            // Dosyanın URI'sini al
            return Uri.fromFile(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Bitmap'i dosyaya yazmak için yardımcı metot
    private File bitmapToFile(Bitmap bitmap, File file) {
        try {
            // Dosyayı aç
            file.createNewFile();
            // Dosyayı yazma işlemi
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapData = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void UploadImage(){
        if(ImageUri!=null){
            final StorageReference myRef=mStorageref.child("photo/" + ImageUri.getLastPathSegment());
            myRef.putFile(ImageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    myRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(uri!=null){
                                PhotoUri=uri.toString();
                                Toast.makeText(getContext(),"Yuklendi",Toast.LENGTH_SHORT).show();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"Hataaaa",Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"Bir hata olustu",Toast.LENGTH_SHORT).show();

                }
            });

        }else{
            Toast.makeText(getContext(),"Bosssss",Toast.LENGTH_SHORT).show();
        }

    }
}