package com.example.logger.ui.syncstruct;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SyncStructViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SyncStructViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Server Sync in progress");
    }

    public LiveData<String> getText() {
        return mText;
    }
}