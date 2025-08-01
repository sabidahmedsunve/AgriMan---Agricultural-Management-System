package com.ppmdev.agriman.model;

import android.net.Uri;

public class BlogModel {
    private String blogTitle;
    private String blogContent;
    private String blogWriterName;
    private String blogImageUrl;

    public BlogModel(){

    }

    public BlogModel(String blogTitle, String blogContent, String blogWriterName, String blogImageUrl) {
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
        this.blogWriterName = blogWriterName;
        this.blogImageUrl = blogImageUrl;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public String getBlogWriterName() {
        return blogWriterName;
    }

    public void setBlogWriterName(String blogWriterName) {
        this.blogWriterName = blogWriterName;
    }

    public String getBlogImageUrl() {
        return blogImageUrl;
    }

    public void setBlogImageUrl(String blogImageUrl) {
        this.blogImageUrl = blogImageUrl;
    }
}
