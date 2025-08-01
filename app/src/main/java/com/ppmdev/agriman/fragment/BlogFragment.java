package com.ppmdev.agriman.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.ppmdev.agriman.R;
import com.ppmdev.agriman.activity.CreateNewBlog;
import com.ppmdev.agriman.activity.IndividualBlogPage;
import com.ppmdev.agriman.adapter.RecyclerBlogPageAdapter;
import com.ppmdev.agriman.callbacks.RecyclerBlogPageAdapterCallbacks;
import com.ppmdev.agriman.model.BlogModel;
import com.ppmdev.agriman.utils.AndroidUtil;

import java.util.ArrayList;

public class BlogFragment extends Fragment implements RecyclerBlogPageAdapterCallbacks {


    private View view;
    private FloatingActionButton floatingActionButton;
    private RecyclerView blogRecyclerView;
    private ArrayList<BlogModel> blogModelArrayList;
    private RecyclerBlogPageAdapter blogAdapter;

    private FirebaseFirestore db;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_blog, container, false);

        init();


        getAllBlogs();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateNewBlog.class);
                startActivity(intent);
            }
        });

        return view;

    }
    private void saveBlogListToSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(blogModelArrayList);
        editor.putString("blogList", json);
        editor.apply();
    }

    private void getAllBlogs() {
        //AndroidUtil.showToast(getContext(),"Database Called");
        db.collection("blogs")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            //AndroidUtil.showToast(getContext(),"Database error");
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED || dc.getType() == DocumentChange.Type.MODIFIED){

                                blogModelArrayList.add(dc.getDocument().toObject(BlogModel.class));
                                //AndroidUtil.showToast(getContext(),"Data Found");
                            }
                            blogAdapter.notifyDataSetChanged();
                            saveBlogListToSharedPreferences();
                        }
                    }
                });
    }

    private void init() {
        floatingActionButton= view.findViewById(R.id.fab);
        blogRecyclerView= view.findViewById(R.id.blog_recyclerview);
        blogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        blogModelArrayList=new ArrayList<>();

        blogAdapter = new RecyclerBlogPageAdapter(getContext(),blogModelArrayList, (RecyclerBlogPageAdapterCallbacks) getContext());
        blogRecyclerView.setAdapter(blogAdapter);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onBlogItemClick(int index) {
        Intent intent = new Intent(getContext(), IndividualBlogPage.class);
        intent.putExtra("blogTitle",  blogModelArrayList.get(index).getBlogTitle().toString());
        //AndroidUtil.showToast(getContext(),"Item- "+blogModelArrayList.size());
        //intent.putExtra("blog", (Parcelable) blogModelArrayList.get(index));
        startActivity(intent);
    }
}
