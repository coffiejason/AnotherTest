package com.figure.anothertest.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.figure.anothertest.models.Post;
import com.figure.anothertest.repositories.PostRepository;

import java.util.List;

public class PostViewModel extends ViewModel {
    private MutableLiveData<List<Post>> mPosts;

    public void init(){
        if(mPosts!= null){
            return;
        }
        PostRepository mRepo = PostRepository.getInstance();
        mPosts = mRepo.getPosts();
    }

    public LiveData<List<Post>> getPosts(){
        return mPosts;
    }


}
