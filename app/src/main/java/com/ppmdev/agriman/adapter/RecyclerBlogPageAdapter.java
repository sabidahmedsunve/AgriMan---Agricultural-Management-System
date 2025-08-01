package com.ppmdev.agriman.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ppmdev.agriman.R;
import com.ppmdev.agriman.callbacks.RecyclerBlogPageAdapterCallbacks;
import com.ppmdev.agriman.model.BlogModel;

import java.net.URI;
import java.util.ArrayList;

public class RecyclerBlogPageAdapter extends RecyclerView.Adapter<RecyclerBlogPageAdapter.BlogViewHolder> {

    Context context;
    ArrayList<BlogModel> blogList;
    RecyclerBlogPageAdapterCallbacks callbacks;


    public RecyclerBlogPageAdapter(Context context, ArrayList<BlogModel> blogList, RecyclerBlogPageAdapterCallbacks blogItemCallbacks) {
        this.context = context;
        this.blogList = blogList;
        this.callbacks =blogItemCallbacks;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_blog,parent,false);
        BlogViewHolder blogViewHolder = new BlogViewHolder(view);

        return blogViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        Glide.with(context)
                .load(Uri.parse(blogList.get(position).getBlogImageUrl()))
                .apply(RequestOptions.centerCropTransform())
                .into(holder.blogImage);
        //holder.blogTime.setText(blogList.get(position).getblogTime()); // will be update in future
        holder.blogWriterName.setText(blogList.get(position).getBlogWriterName());
        holder.blogTitle.setText(blogList.get(position).getBlogTitle());

        holder.cardBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbacks.onBlogItemClick(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }


    public class BlogViewHolder extends RecyclerView.ViewHolder{

         ImageView blogImage;
         TextView blogWriterName;
         TextView blogTitle;
         TextView blogTime;
        ConstraintLayout cardBlog;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);

            blogImage=itemView.findViewById(R.id.iv_blog_card);
            blogWriterName=itemView.findViewById(R.id.tv_card_blog_writerName);
            blogTitle=itemView.findViewById(R.id.tv_card_blog_title);
            //blogTime=itemView.findViewById(R.id.tv_card_blog_date);

            cardBlog=itemView.findViewById(R.id.card_blog);

        }
    }
}
