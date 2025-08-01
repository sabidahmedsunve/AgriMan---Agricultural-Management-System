package com.ppmdev.agriman.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.ppmdev.agriman.R;
import com.ppmdev.agriman.utils.AndroidUtil;
import com.ppmdev.agriman.utils.FirebaseUtil;

public class AboutUs extends AppCompatActivity {


    private ImageView ivImagePartha;
    private ImageView ivImageSunve;
    private Uri devImageUri;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_us);


        init();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutUs.this,HomePage.class));
            }
        });
        getDevImage("image_partha.jpg",ivImagePartha);
        getDevImage("image_sunve.jpg",ivImageSunve);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void init(){
        ivImagePartha=findViewById(R.id.iv_dev_image_partha);
        ivImageSunve=findViewById(R.id.iv_dev_image_sunve);
        toolbar=findViewById(R.id.toolbar_aboutus);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    private void getDevImage(String childName, ImageView imageView) {
        FirebaseUtil.getImgFromFirebaseStorage(childName).getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        devImageUri=task.getResult();
                        AndroidUtil.setImageFromFirebase(getApplicationContext(),devImageUri,imageView);
                    }
                });
    }
}