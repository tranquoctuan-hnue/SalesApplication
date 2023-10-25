package com.example.ungdungbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ungdungbanhang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class LienHeActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 999;
    private EditText edt_fullName,edt_email;
    private Button btn_UpdateProfile;
    private ImageView img_avatar;
    Uri filePath;
    private MainActivity mMainActivity;
    ProgressDialog progressDialog;
    FirebaseUser user;
    androidx.appcompat.widget.Toolbar toolbarInfor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lien_he);
        progressDialog = new ProgressDialog(this);
        initUi();
        setUserInfor();
        initListener();
        ActionBar();
    }
    private void ActionBar() {
        setSupportActionBar(toolbarInfor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarInfor.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initListener() {
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        btn_UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
//                Glide.with(getApplicationContext()).load(user.getPhotoUrl()).error(R.drawable.error).into(img_avatar);
            }
        });



    }

    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
        {
            return;
        }
        progressDialog.show();
        String strFullName = edt_fullName.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strFullName)
                .setPhotoUri(filePath)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(LienHeActivity.this,"Update thành công !!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void setUserInfor() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
        {
            return;
        }
        edt_fullName.setText(user.getDisplayName());
        edt_email.setText(user.getEmail());
//        try {
//            InputStream inputStream = getContentResolver().openInputStream(Uri.parse(uri));
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//            img_avatar.setImageBitmap(bitmap);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        Glide.with(getApplicationContext()).load(user.getPhotoUrl()).error(R.drawable.avatar).into(img_avatar);
    }
    private void initUi() {
        edt_fullName = findViewById(R.id.edt_fullName);
        edt_email = findViewById(R.id.edt_email);
        btn_UpdateProfile = findViewById(R.id.btn_UpdateProfile);
        img_avatar = findViewById(R.id.img_avatar);
        toolbarInfor = findViewById(R.id.toobarInfor);
    }
    private void SelectImage() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img_avatar.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    }