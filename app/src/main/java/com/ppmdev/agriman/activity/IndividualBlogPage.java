package com.ppmdev.agriman.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ppmdev.agriman.R;
import com.ppmdev.agriman.fragment.BlogFragment;
import com.ppmdev.agriman.model.BlogModel;
import com.ppmdev.agriman.utils.AndroidUtil;

public class IndividualBlogPage extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView blogImage;
    private TextView blogTitle;
    private TextView blogWriterName;
    private TextView blogContent;
    private BlogModel newBlogModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_individual_blog_page);


        init();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndividualBlogPage.this, HomePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        if (newBlogModel!=null){
            updateUi(newBlogModel);
        }else{
            AndroidUtil.showToast(getApplicationContext(),"Null value");
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void init(){
        toolbar=findViewById(R.id.toolbar_individual_blog_view);
        blogImage=findViewById(R.id.iv_individual_blog_image);
        blogTitle=findViewById(R.id.tv_individual_blog_title);
        blogWriterName=findViewById(R.id.tv_individual_bolg_writerName);
        blogContent=findViewById(R.id.tv_individual_blog_content);

        newBlogModel = new BlogModel();
        newBlogModel.setBlogTitle(getIntent().getStringExtra("blogTitle"));
        newBlogModel.setBlogImageUrl(getIntent().getStringExtra("blogImage"));
        newBlogModel.setBlogContent(getIntent().getStringExtra("blogContent"));
        newBlogModel.setBlogWriterName(getIntent().getStringExtra("blogWriterName"));



    }

    private void updateUi(BlogModel blogModel) {
        Glide.with(this)
                .load(Uri.parse(blogModel.getBlogImageUrl()))
                .apply(RequestOptions.centerCropTransform())
                .into(blogImage);
        blogTitle.setText(blogModel.getBlogTitle());
        blogContent.setText(blogModel.getBlogContent());
        blogWriterName.setText("By, "+blogModel.getBlogWriterName());
    }
}