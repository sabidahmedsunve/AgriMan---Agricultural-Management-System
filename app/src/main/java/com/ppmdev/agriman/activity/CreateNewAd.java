package com.ppmdev.agriman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ppmdev.agriman.R;
import com.ppmdev.agriman.model.MarketPlaceAdModel;
import com.ppmdev.agriman.utils.AndroidUtil;
import com.ppmdev.agriman.utils.FirebaseUtil;

public class CreateNewAd extends AppCompatActivity {

    private TextView btnCreateNewAd;
    private EditText newProductName;
    private EditText newProductValue;
    private EditText newProductDescription;
    private EditText newPhoneNumber;
    private ProgressBar progressBar;
    private ImageView backBtn;
    private EditText newSellerName;

    private Toolbar toolbar;

    private MarketPlaceAdModel marketPlaceAdModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_new_ad);

        init();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateNewAd.this,MarketPlacePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        btnCreateNewAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInProgress(true);
                String productName = newProductName.getText().toString();
                String productValue = newProductValue.getText().toString();
                String productDescription = newProductDescription.getText().toString();
                String phoneNumber = newPhoneNumber.getText().toString();
                String sellerName = newSellerName.getText().toString();

                marketPlaceAdModel=new MarketPlaceAdModel(productName,productDescription,productValue,sellerName,phoneNumber);
                FirebaseUtil.allMarketAd().set(marketPlaceAdModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            setInProgress(false);
                            //AndroidUtil.showToast(getApplicationContext(),"New Ad created successfully");
                            Intent intent = new Intent(CreateNewAd.this,MarketPlacePage.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            setInProgress(false);
                            //AndroidUtil.showToast(getApplicationContext(),"New Ad created failed");
                        }
                    }
                });
            }
        });

        /*backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateNewAd.this,MarketPlacePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });*/
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void init(){
        btnCreateNewAd=findViewById(R.id.et_btnCreateNewAd);
        newProductName=findViewById(R.id.et_createNewAd_productName);
        newProductValue=findViewById(R.id.et_createNewAd_productValue);
        newProductDescription=findViewById(R.id.et_createNewAd_Descriptio);
        newPhoneNumber=findViewById(R.id.et_createNewAd_phoneNumber);
        progressBar=findViewById(R.id.pg_CreateNewAd);
        //backBtn=findViewById(R.id.iv_createNewAd_backBtn);
        newSellerName=findViewById(R.id.et_CreateNewAd_sellrName);

        toolbar=findViewById(R.id.toolbar_createNewAd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }
    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            btnCreateNewAd.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnCreateNewAd.setVisibility(View.VISIBLE);
        }

    }

}