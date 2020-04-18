package com.figure.anothertest.repositories;

import androidx.lifecycle.MutableLiveData;

import com.figure.anothertest.models.Post;

import java.util.ArrayList;
import java.util.List;

public class PostRepository {
    private static PostRepository instance;
    private ArrayList<Post> dataset = new ArrayList<>();

    public static PostRepository getInstance(){
        if(instance == null){ instance = new PostRepository();}
        return instance;
    }

    public MutableLiveData<List<Post>> getPosts(){

        MutableLiveData<List<Post>> data = new MutableLiveData<>();
        data.setValue(dataset);

        return data;
    }

    private void SetData(Post p){
        dataset.add(p);
    }
}
