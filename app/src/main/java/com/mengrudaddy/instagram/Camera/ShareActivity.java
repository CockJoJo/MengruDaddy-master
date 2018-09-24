package com.mengrudaddy.instagram.Camera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mengrudaddy.instagram.Home.MainActivity;
import com.mengrudaddy.instagram.Login.LoginActivity;
import com.mengrudaddy.instagram.Profile.ProfileActivity;
import com.mengrudaddy.instagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.widget.ProgressBar;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShareActivity extends AppCompatActivity {

    private FirebaseStorage storage;
    private FirebaseUser authUser;
    private StorageReference storageReference;
    private byte[] imageByte;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        //receive image intent
        Bundle extras = getIntent().getExtras();
        imageByte = extras.getByteArray("PostImage");
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

        ImageView image =  (ImageView)findViewById(R.id.imageShare);
        ImageView btnPost =  (ImageView)findViewById(R.id.icon_next);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //set imageView
        image.setImageBitmap(bitmap);

        //send image to fireBase
        storage = FirebaseStorage.getInstance();
        authUser =FirebaseAuth.getInstance().getCurrentUser();
        //post/userId/postid
        String userRef = "posts/"+authUser.getUid();
        storageReference = storage.getReference(userRef).child(UUID.randomUUID().toString());



        btnPost.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                uploadImage();
            }
        });
    }


    //upload image
    private void uploadImage() {

        if(storageReference != null)
        {
            progressBar.setVisibility(View.VISIBLE);
            //disable user interaction
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            storageReference.putBytes(imageByte)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ShareActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Intent i = new Intent(ShareActivity.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(ShareActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }



}
