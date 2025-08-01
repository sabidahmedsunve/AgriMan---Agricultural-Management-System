package com.ppmdev.agriman.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.ppmdev.agriman.R;
import com.ppmdev.agriman.activity.SplashScreen;
import com.ppmdev.agriman.model.UserModel;
import com.ppmdev.agriman.utils.AndroidUtil;
import com.ppmdev.agriman.utils.FirebaseUtil;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {

    private ImageView profilePic;
    private TextView userName;
    private EditText userEmail;
    private EditText userPhone;
    private TextView btnLogOut;
    private TextView btnUpdateProfile;
    private UserModel currentUserModel;
    private ProgressBar progressBar;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ActivityResultLauncher<Intent> imagePickerLauncher;
    Uri  selectedImgUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result ->{
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImgUri=data.getData();
                            AndroidUtil.setProfilePicture(getContext(),selectedImgUri,profilePic);
                        }
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profilePic = view.findViewById(R.id.iv_profile_Photo);
        userName = view.findViewById(R.id.tv_profilePage_username);
        userEmail = view.findViewById(R.id.et_profilePage_email);
        userEmail.setEnabled(false);
        userPhone = view.findViewById(R.id.et_profilePage_phoneNumber);
        btnLogOut = view.findViewById(R.id.tv_btnLogOut);
        btnUpdateProfile = view.findViewById(R.id.et_btnUpdateProfile);
        progressBar = view.findViewById(R.id.pg_updateProfileProgressBar);

        getUserData();



        btnUpdateProfile.setOnClickListener((v ->{
            updateBtnClick();
        }));
        btnLogOut.setOnClickListener((view1 -> {
            FirebaseUtil.logout();
            Intent intent = new Intent(getContext(), SplashScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }));

        profilePic.setOnClickListener((v)->{
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickerLauncher.launch(intent);
                            return null;
                        }
                    });
        });
        return view;
    }

    private void updateBtnClick() {

        String newUserEmail = userEmail.getText().toString();
        String newUserPhone = userPhone.getText().toString();

        if(TextUtils.isEmpty(newUserEmail) || !newUserEmail.matches(emailPattern)){
            userEmail.setError("Enter valid email");
            return;
        }
        setInProgress(true);
        if(newUserPhone.length()>14 || newUserPhone.length()<14){
            userPhone.setError("Enter phone number with country code");
            return;
        }
        currentUserModel.setUserEmail(newUserEmail);
        currentUserModel.setUserPhone(newUserPhone);
        if(selectedImgUri!=null){
            FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImgUri)
                    .addOnCompleteListener(task -> {
                        updateToFirestore();
                    });
        }else{
            updateToFirestore();
        }
    }

    private void updateToFirestore(){
        FirebaseUtil.currentUserDetails().set(currentUserModel)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        AndroidUtil.showToast(getContext(),"Updated successfully");
                        setInProgress(false);
                    }else{
                        AndroidUtil.showToast(getContext(),"Updated failed");
                        setInProgress(false);
                    }
                });
    }


    private void getUserData() {
        setInProgress(true);

        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri  = task.getResult();
                        AndroidUtil.setProfilePicture(getContext(),uri,profilePic);
                    }
                });

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            currentUserModel = task.getResult().toObject(UserModel.class);
            userName.setText(currentUserModel.getUserName());
            userPhone.setText(currentUserModel.getUserPhone());
            userEmail.setText(currentUserModel.getUserEmail());
        });
    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            btnUpdateProfile.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            btnUpdateProfile.setVisibility(View.VISIBLE);
        }

    }
}