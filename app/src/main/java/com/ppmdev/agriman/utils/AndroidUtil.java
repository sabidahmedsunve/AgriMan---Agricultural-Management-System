package com.ppmdev.agriman.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class AndroidUtil {
    public static void showToast(Context context, String messege){
        Toast.makeText(context, messege, Toast.LENGTH_SHORT).show();
    }

    public static void setProfilePicture(Context context, Uri imgUri, ImageView imageView){
        Glide.with(context).load(imgUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    public static void setImageFromFirebase(Context context, Uri imgUri,ImageView imageView){
        Glide.with(context).load(imgUri).apply(RequestOptions.centerCropTransform()).into(imageView);
        //Picasso.get().load(imgUri).into(imageView);
    }



    public static void setBlogPicture(Context context, Uri imgUri, ImageView imageView){
        Glide.with(context).load(imgUri).apply(RequestOptions.centerCropTransform()).into(imageView);
    }
}
