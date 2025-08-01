package com.ppmdev.agriman.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ppmdev.agriman.R;
import com.ppmdev.agriman.fragment.BlogFragment;
import com.ppmdev.agriman.model.BlogModel;
import com.ppmdev.agriman.utils.AndroidUtil;
import com.ppmdev.agriman.utils.FirebaseUtil;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class CreateNewBlog extends AppCompatActivity {

    private EditText blogTitle;
    private EditText blogContent;
    private EditText blogWriterName;
    private ImageView blogImage;
    ActivityResultLauncher<Intent> imagePickerLauncher;
    Uri selectedImgUri;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView btnSubmitBlog;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private BlogModel newBlogModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_new_blog);


        init();
        setInProgress(false);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {
                            selectedImgUri = result.getData().getData();
                            //blogImage.setImageURI(selectedImgUri);
                            AndroidUtil.setBlogPicture(getApplicationContext(),selectedImgUri,blogImage);
                        }
                    }
                }
        );

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateNewBlog.this, HomePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        blogImage.setOnClickListener(view -> openImagePicker());


        btnSubmitBlog.setOnClickListener(view -> {
            setInProgress(true);
            if(selectedImgUri != null){

                String newBlogTitle = blogTitle.getText().toString();
                String newBlogContent = blogContent.getText().toString();
                String newBlogWriterName = blogWriterName.getText().toString();
                String newBlogImageUri = selectedImgUri.toString();

                newBlogModel = new BlogModel(newBlogTitle,newBlogContent, newBlogWriterName, newBlogImageUri);

                updateToFireStore(newBlogModel);
            }
        });





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



    private void updateToFireStore(BlogModel blogModel) {

        uploadImageToFirebase();

        FirebaseUtil.allBolgDetails().set(blogModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    setInProgress(false);
                    AndroidUtil.showToast(getApplicationContext(),"Blog created Successfully");
                    Intent intent = new Intent(getApplicationContext(),HomePage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else{
                    setInProgress(false);
                    AndroidUtil.showToast(getApplicationContext(),"Blog created failed");
                }
            }
        });
    }
    private void uploadImageToFirebase() {
        if(selectedImgUri!=null){
            FirebaseUtil.getBlogPictureStorageRef().putFile(selectedImgUri)
                    .addOnCompleteListener(task -> {
                        //AndroidUtil.showToast(getApplicationContext(),"blog Image uploaded");
                    });
        }else{
            //AndroidUtil.showToast(getApplicationContext(),"Invalid Picture");
        }
    }



    private void openImagePicker() {
        ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                .createIntent(new Function1<Intent, Unit>() {
                    @Override
                    public Unit invoke(Intent intent) {
                        imagePickerLauncher.launch(intent);
                        return null;
                    }
                });
    }

    private void init (){
        toolbar=findViewById(R.id.toolbar_createNewBlog);
        blogTitle=findViewById(R.id.et_createNewBlog_title);
        blogContent=findViewById(R.id.et_createNewBlog_content);
        blogWriterName=findViewById(R.id.et_createNewBlog_writerName);
        blogImage=findViewById(R.id.iv_createNewBlog_image);
        progressBar=findViewById(R.id.pg_createNewBlog);
        btnSubmitBlog=findViewById(R.id.tv_button_write_blog);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
    }
    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            btnSubmitBlog.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnSubmitBlog.setVisibility(View.VISIBLE);
        }

    }
}