package com.ppmdev.agriman.activity;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ppmdev.agriman.R;
import com.ppmdev.agriman.adapter.RecyclerMarketPlaceAdAdapter;
import com.ppmdev.agriman.model.MarketPlaceAdModel;
import com.ppmdev.agriman.utils.AndroidUtil;
import com.ppmdev.agriman.utils.FirebaseUtil;

import java.util.ArrayList;


public class MarketPlacePage extends AppCompatActivity{

    private RecyclerView recyclerViewAd;
    private ArrayList<MarketPlaceAdModel> marketPlaceAdArr;

    private Toolbar toolbar;
    private ImageView backBtn;
    private RecyclerMarketPlaceAdAdapter adapter;

    private FirebaseFirestore db;

    private ProgressBar progressBar;
    private ImageView banner;
    private Uri bannerImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_marketplace);

        init();

        getBannerImage("agriman_poster.jpg");



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MarketPlacePage.this,HomePage.class));
            }
        });
        /*backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MarketPlacePage.this, HomePage.class));
            }
        });*/
        
        getAllMarketAd();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
    }

    private void getBannerImage(String childName) {
        FirebaseUtil.getImgFromFirebaseStorage(childName).getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        bannerImgUri=task.getResult();
                        AndroidUtil.setImageFromFirebase(getApplicationContext(),bannerImgUri,banner);
                    }
                });
    }

    private void getAllMarketAd() {
        db.collection("ad")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            //AndroidUtil.showToast(getApplicationContext(),"Database error");
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED || dc.getType() == DocumentChange.Type.MODIFIED){
                                marketPlaceAdArr.add(dc.getDocument().toObject(MarketPlaceAdModel.class));
                            }
                            adapter.notifyDataSetChanged();

                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.marketplace_more_option,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.marketPlace_moreOptin_createNewAd){
            //AndroidUtil.showToast(getApplicationContext(),"clicked");
            startActivity(new Intent(MarketPlacePage.this, CreateNewAd.class));

        }
        /*else if(itemId == R.id.marketPlace_moreOptin_activeAd){

        }*/

        return true;
    }
    private void init() {
        //backBtn=findViewById(R.id.iv_marketplace_backbtn);
        db = FirebaseFirestore.getInstance();
        recyclerViewAd = findViewById(R.id.marketPlace_recyclerviewAd);
        recyclerViewAd.setLayoutManager(new LinearLayoutManager(this));

        marketPlaceAdArr = new ArrayList<>();

        adapter = new RecyclerMarketPlaceAdAdapter(this, marketPlaceAdArr);
        recyclerViewAd.setAdapter(adapter);

        progressBar=findViewById(R.id.pg_marketPlace);
        toolbar=findViewById(R.id.toolbar_marketplace);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        banner=findViewById(R.id.iv_marketplace_banner);

    }

    private void setProgressBar(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }

}