package com.ppmdev.agriman.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtil {



    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static boolean isLoggedIn(){
        if(currentUserId()!=null){
            return true;
        }
        return false;
    }
    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static DocumentReference allBolgDetails(){
        return FirebaseFirestore.getInstance().collection("blogs").document(currentUserId());
    }

    public static DocumentReference allMarketAd(){
        return FirebaseFirestore.getInstance().collection("ad").document(currentUserId());
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference getCurrentProfilePicStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(FirebaseUtil.currentUserId());
    }
    public static StorageReference getImgFromFirebaseStorage(String nameOfPicture){
        return FirebaseStorage.getInstance().getReference().child("app_image")
                .child(nameOfPicture);
    }

    public static StorageReference getBlogPictureStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("blog_image")
                .child(FirebaseUtil.currentUserId());
    }

    public static StorageReference getWeatherImageStorageRef(String nameOfPicture){
        return FirebaseStorage.getInstance().getReference().child("weather_image")
                .child(nameOfPicture);
    }

}
